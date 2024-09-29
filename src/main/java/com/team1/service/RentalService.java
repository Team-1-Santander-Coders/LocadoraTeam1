package main.java.com.team1.service;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.exception.RentIllegalUpdateException;
import main.java.com.team1.repository.RentalRepositoryImpl;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * A classe {@code RentalService} gerencia operações relacionadas a alugueis.
 * Ela encapsula a lógica de negócios para adicionar e recuperar alugueis do repositório de alugueis.
 */
public class RentalService {
    private final RentalRepositoryImpl rentalRepository = new RentalRepositoryImpl();
    private final VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());

    /**
     * Adiciona um novo aluguel.
     * @param rentalDTO
     * @throws DuplicateEntityException caso o aluguel já exista.
     * @throws RentIllegalUpdateException caso o veículo não esteja disponível.
     * @throws EntityNotFoundException caso o veículo não exista
     */
    public void addRental(RentalDTO rentalDTO) throws DuplicateEntityException, RentIllegalUpdateException, EntityNotFoundException {
        vehicleService.rentVehicle(rentalDTO.getVehicle().getPlaca());
        rentalRepository.save(rentalDTO);
    }

    /**
     * Lista todos os alugueis cadastrados.
     * @return lista de alugueis.
     */
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Retorna os alugueis baseados em uma data de retirada especifica.
     * @param date Data de retirada
     * @return RentalDTO
     */
    public List<RentalDTO> getRentalByDate(String date) {
        List<RentalDTO> allRentals = getAllRentals();
        assert allRentals != null;
        LocalDate dataConvertida = DateUtil.converterTextoParaData(date);
        return allRentals.stream()
                .filter(rental -> rental.getRentalDate() == dataConvertida || rental.getReturnDate() == dataConvertida)
                .toList();
    }

    /**
     * Método responsável pela devolução do veículo.
     *
     * @param rentalDTO
     * @param returnAgency Agência em que o veículo foi devolvido
     * @param returnDate Data em que o veículo foi devolvido
     * @return o recibo de aluguel.
     * @throws EntityNotFoundException caso o aluguel não seja encontrado.
     * @throws RentIllegalUpdateException caso a data de devolução seja anterior à data de retirada.
     */

    public String returnRental(RentalDTO rentalDTO, AgencyDTO returnAgency, LocalDate returnDate) throws EntityNotFoundException, RentIllegalUpdateException {
        if (returnDate.isBefore(rentalDTO.getRentalDate())) {
            throw new RentIllegalUpdateException("Data de devolução não pode ser menor que data de retirada.");
        }
        vehicleService.returnVehicle(rentalDTO.getVehicle().getPlaca(), returnAgency);
        RentalDTO updatedRentalDTO = new RentalDTO(rentalDTO.getVehicle(), rentalDTO.getCustomer(), rentalDTO.getAgencyRental(), rentalDTO.getRentalDate(), returnAgency, returnDate);
        rentalRepository.update(updatedRentalDTO);
        return updatedRentalDTO.generateReceipt();
    }

    /**
     * Deleta um aluguel com base na placa e no documento do usuário.
     * @param placa Placa do veículo.
     * @param document CPF ou CNPJ do cliente.
     * @throws EntityNotFoundException Caso o aluguel não seja encontrado.
     */
    public void deleteRental(String placa, String document) throws EntityNotFoundException {
        rentalRepository.delete(placa, document);
    }

    /**
     * Retorna um aluguel com base na placa do veículo, documento do cliente e data de retirada do veículo.
     * @param placa Placa do veículo
     * @param document CPF ou CNPJ do cliente.
     * @param rentalDate Data de retirada do veículo.
     * @return RentalDTO
     */
    public RentalDTO getRental(String placa, String document, LocalDate rentalDate) {
        return getAllRentals().stream()
                .filter(r -> r.vehiclePlate().equals(placa)
                        && r.customerDocument().equals(document)
                        && r.getRentalDate().isEqual(rentalDate))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna um recibo de aluguel com base na placa do veículo, documento do cliente e data de retirada do veículo.
     * @param placa Placa do veículo.
     * @param document Documento do veículo.
     * @param rentalDate Data de retirada do veículo.
     * @return o recibo de aluguel.
     */
    public String getReceipt(String placa, String document, LocalDate rentalDate) {
        return getRental(placa, document, rentalDate).generateReceipt();
    }

    /**
     * Método de paginação dos alugueis.
     * @param pageNumber Numero da página.
     * @param pageSize Tamanho da página.
     * @return página com alugueis.
     */
    public List<RentalDTO> getRentalsByPage(int pageNumber, int pageSize) {
        List<RentalDTO> allRentals = getAllRentals();
        int fromIndex = Math.min(pageNumber * pageSize, allRentals.size());
        int toIndex = Math.min(fromIndex + pageSize, allRentals.size());
        return allRentals.subList(fromIndex, toIndex);
    }
}
