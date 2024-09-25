package main.java.com.team1.repository;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

public interface AgencyRepository {
    void save(AgencyDTO agencyDTO) throws DuplicateEntityException;

    List<AgencyDTO> findAll();

    void update(AgencyDTO agencyDTO, String newName, String newAddress) throws EntityNotFoundException;

    void delete(String name, String address) throws EntityNotFoundException;

    AgencyDTO findByNameAndAddress(String name, String address) throws EntityNotFoundException;
}
