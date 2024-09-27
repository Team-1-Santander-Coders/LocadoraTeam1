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
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private final RentalRepositoryImpl rentalRepository = new RentalRepositoryImpl();
    private final VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());

    public void addRental(RentalDTO rentalDTO) throws DuplicateEntityException, RentIllegalUpdateException {
        vehicleService.rentVehicle(rentalDTO.getVehicle());
        rentalRepository.save(rentalDTO);
    }

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Este método busca todas as locações associadas a um determinado usuário (cliente) com base no documento.
     *
     * @param name String que representa o nome do cliiente.
     * @return Uma lista de locações (RentalDTO) do cliente, se encontradas.
     */
    public List<RentalDTO> getRentalByUsers(String name) {
        List<RentalDTO> allRentals = getAllRentals();
        List<RentalDTO> rentalsByUser = new ArrayList<>();

        for (RentalDTO rental : allRentals) {
            if (rental.getCustomer().getName().equals(name)) {
                rentalsByUser.add(rental);
            }
        }

        return rentalsByUser;
    }

    public List<RentalDTO> getRentalByDate(String date) {
        List<RentalDTO> allRentals = getAllRentals();
        assert allRentals != null;
        LocalDate dataConvertida = DateUtil.converterTextoParaData(date);
        return allRentals.stream()
                .filter(rental -> rental.getRentalDate() == dataConvertida || rental.getReturnDate() == dataConvertida)
                .toList();
    }

    public String returnRental(RentalDTO rentalDTO, AgencyDTO returnAgency, String returnDate) throws EntityNotFoundException, RentIllegalUpdateException {
        vehicleService.returnVehicle(rentalDTO.getVehicle());
        RentalDTO updatedRentalDTO = new RentalDTO(rentalDTO.getVehicle(), rentalDTO.getCustomer(), rentalDTO.getAgencyRental(), rentalDTO.getRentalDate(), returnAgency, DateUtil.converterTextoParaData(returnDate));
        rentalRepository.update(updatedRentalDTO);
        return updatedRentalDTO.generateReceipt();
    }

    public void deleteRental(String placa, String document) throws EntityNotFoundException {
        rentalRepository.delete(placa, document);
    }

    public String getReceipt(String placa, String document) {
        RentalDTO rentalDTO = getAllRentals().stream()
                                             .filter(r -> r.vehiclePlate().equals(placa) && r.customerDocument().equals(document))
                                             .toList().getFirst();

        return rentalDTO.generateReceipt();
    }

    public List<RentalDTO> getRentalsByPage(int pageNumber, int pageSize) {
        List<RentalDTO> allRentals = getAllRentals();
        int fromIndex = Math.min(pageNumber * pageSize, allRentals.size());
        int toIndex = Math.min(fromIndex + pageSize, allRentals.size());
        return allRentals.subList(fromIndex, toIndex);
    }
}
