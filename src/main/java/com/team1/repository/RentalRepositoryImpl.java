package main.java.com.team1.repository;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RentalRepositoryImpl implements RentalRepository {
    private static final String RENTAL_FILE = "src/resources/data/rentals.dat";

    @Override
    public void save(RentalDTO rentalDTO) throws DuplicateEntityException {
        List<RentalDTO> rentals = FileUtil.readFromFile(RENTAL_FILE);
        if (rentals == null) rentals = new ArrayList<>();
        if (rentals.stream().anyMatch(rental -> rental.equals(rentalDTO))) {
            throw new DuplicateEntityException("Aluguel já cadastrado.");
        }
        rentals.add(rentalDTO);
        FileUtil.writeToFile(rentals, RENTAL_FILE);
    }

    @Override
    public List<RentalDTO> findAll() {
        List<RentalDTO> rentals = FileUtil.readFromFile(RENTAL_FILE);
        return rentals != null ? rentals : new ArrayList<>();
    }

    @Override
    public void update(RentalDTO updatedRentalDTO) throws EntityNotFoundException {
        List<RentalDTO> rentals = FileUtil.readFromFile(RENTAL_FILE);
        if (rentals == null) rentals = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < Objects.requireNonNull(rentals).size(); i++) {
            if (rentals.get(i).equals(updatedRentalDTO)) {
                rentals.set(i, updatedRentalDTO);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new EntityNotFoundException("Aluguel não encontrado.");
        }
        FileUtil.writeToFile(rentals, RENTAL_FILE);
    }

    @Override
    public void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException {
        List<RentalDTO> rentals = FileUtil.readFromFile(RENTAL_FILE);
        assert rentals != null;
        rentals.removeIf(rental -> rental.vehiclePlate().equals(vehiclePlate) && rental.customerDocument().equals(customerDocument));
        FileUtil.writeToFile(rentals, RENTAL_FILE);
    }
}
