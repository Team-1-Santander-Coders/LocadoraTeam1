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
 * A classe {@code RentalService} fornece serviços para gerenciar aluguéis de veículos.
 * Ela encapsula a lógica de negócios relacionada a aluguéis e interage com os repositórios de aluguéis e veículos.
 */
public class RentalService {
    private final RentalRepositoryImpl rentalRepository = new RentalRepositoryImpl();
    private final VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());

    /**
     * Adiciona um novo aluguel de veículo.
     *
     * @param rentalDTO O objeto {@code RentalDTO} contendo os dados do aluguel a ser adicionado.
     * @throws DuplicateEntityException Se o aluguel já estiver cadastrado.
     * @throws RentIllegalUpdateException Se a operação de aluguel não for permitida.
     */
    public void addRental(RentalDTO rentalDTO) throws DuplicateEntityException, RentIllegalUpdateException {
        vehicleService.rentVehicle(rentalDTO.getVehicle());
        rentalRepository.save(rentalDTO);
    }

    /**
     * Recupera todos os aluguéis registrados.
     *
     * @return Uma lista de objetos {@code RentalDTO} representando todos os aluguéis.
     */
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Recupera aluguéis com base em uma data específica.
     *
     * @param date A data a ser pesquisada no formato de texto.
     * @return Uma lista de objetos {@code RentalDTO} que correspondem à data especificada.
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
     * Retorna um veículo alugado e atualiza o aluguel correspondente.
     *
     * @param rentalDTO O objeto {@code RentalDTO} que representa o aluguel a ser retornado.
     * @param returnAgency O objeto {@code AgencyDTO} representando a agência de retorno.
     * @param returnDate A data de retorno no formato de texto.
     * @return Um recibo gerado para o aluguel retornado.
     * @throws EntityNotFoundException Se o aluguel não for encontrado.
     * @throws RentIllegalUpdateException Se a operação de retorno não for permitida.
     */
    public String returnRental(RentalDTO rentalDTO, AgencyDTO returnAgency, String returnDate) throws EntityNotFoundException, RentIllegalUpdateException {
        vehicleService.returnVehicle(rentalDTO.getVehicle());
        RentalDTO updatedRentalDTO = new RentalDTO(rentalDTO.getVehicle(), rentalDTO.getCustomer(), rentalDTO.getAgencyRental(), rentalDTO.getRentalDate(), returnAgency, DateUtil.converterTextoParaData(returnDate));
        rentalRepository.update(updatedRentalDTO);
        return updatedRentalDTO.generateReceipt();
    }

    /**
     * Exclui um aluguel com base na placa do veículo e no documento do cliente.
     *
     * @param placa A placa do veículo a ser excluído.
     * @param document O documento do cliente correspondente ao aluguel a ser excluído.
     * @throws EntityNotFoundException Se o aluguel não for encontrado.
     */
    public void deleteRental(String placa, String document) throws EntityNotFoundException {
        rentalRepository.delete(placa, document);
    }

    /**
     * Recupera o recibo de um aluguel com base na placa do veículo e no documento do cliente.
     *
     * @param placa A placa do veículo.
     * @param document O documento do cliente.
     * @return O recibo gerado para o aluguel correspondente.
     */
    public String getReceipt(String placa, String document) {
        RentalDTO rentalDTO = getAllRentals().stream()
                                             .filter(r -> r.vehiclePlate().equals(placa) && r.customerDocument().equals(document))
                                             .toList().getFirst();

        return rentalDTO.generateReceipt();
    }

    /**
     * Recupera uma lista de aluguéis paginada.
     *
     * @param pageNumber O número da página a ser recuperada.
     * @param pageSize O número de aluguéis por página.
     * @return Uma lista de objetos {@code RentalDTO} para a página especificada.
     */
    public List<RentalDTO> getRentalsByPage(int pageNumber, int pageSize) {
        List<RentalDTO> allRentals = getAllRentals();
        int fromIndex = Math.min(pageNumber * pageSize, allRentals.size());
        int toIndex = Math.min(fromIndex + pageSize, allRentals.size());
        return allRentals.subList(fromIndex, toIndex);
    }
}
