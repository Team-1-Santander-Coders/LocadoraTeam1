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

public class RentalService {
    private final RentalRepositoryImpl rentalRepository = new RentalRepositoryImpl();
    private final VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());

    public void addRental(RentalDTO rentalDTO) throws DuplicateEntityException, RentIllegalUpdateException, EntityNotFoundException {
        vehicleService.rentVehicle(rentalDTO.getVehicle().getPlaca());
        rentalRepository.save(rentalDTO);
    }

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<RentalDTO> getRentalByDate(String date) {
        List<RentalDTO> allRentals = getAllRentals();
        assert allRentals != null;
        LocalDate dataConvertida = DateUtil.converterTextoParaData(date);
        return allRentals.stream()
                .filter(rental -> rental.getRentalDate() == dataConvertida || rental.getReturnDate() == dataConvertida)
                .toList();
    }

    public String returnRental(RentalDTO rentalDTO, AgencyDTO returnAgency, LocalDate returnDate) throws EntityNotFoundException, RentIllegalUpdateException {
        vehicleService.returnVehicle(rentalDTO.getVehicle().getPlaca(), returnAgency);
        RentalDTO updatedRentalDTO = new RentalDTO(rentalDTO.getVehicle(), rentalDTO.getCustomer(), rentalDTO.getAgencyRental(), rentalDTO.getRentalDate(), returnAgency, returnDate);
        rentalRepository.update(updatedRentalDTO);
        return updatedRentalDTO.generateReceipt();
    }

    public void deleteRental(String placa, String document) throws EntityNotFoundException {
        rentalRepository.delete(placa, document);
    }

    public RentalDTO getRental(String placa, String document, LocalDate rentalDate) {
        return getAllRentals().stream()
                .filter(r -> r.vehiclePlate().equals(placa) && r.customerDocument().equals(document) && r.getRentalDate() == rentalDate)
                .toList().getFirst();
    }

    public String getReceipt(String placa, String document, LocalDate rentalDate) {
        return getRental(placa, document, rentalDate).generateReceipt();
    }

    public List<RentalDTO> getRentalsByPage(int pageNumber, int pageSize) {
        List<RentalDTO> allRentals = getAllRentals();
        int fromIndex = Math.min(pageNumber * pageSize, allRentals.size());
        int toIndex = Math.min(fromIndex + pageSize, allRentals.size());
        return allRentals.subList(fromIndex, toIndex);
    }
}
