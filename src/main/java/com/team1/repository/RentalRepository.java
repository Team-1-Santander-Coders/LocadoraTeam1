package main.java.com.team1.repository;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

public interface RentalRepository {
    void save(RentalDTO rentalDTO) throws DuplicateEntityException;
    List<RentalDTO> findAll();
    void update(RentalDTO updatedRentalDTO) throws EntityNotFoundException;
    void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException;
}
