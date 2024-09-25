package main.java.com.team1.repository;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

public interface CustomerRepository {

    void save(CustomerDTO customerDTO) throws DuplicateEntityException;

    List<CustomerDTO> findAll();

    void update(CustomerDTO customer, String newName, String newAddress, String newPhone) throws EntityNotFoundException;

    void delete(CustomerDTO customerDTO) throws EntityNotFoundException;

    CustomerDTO findByDocument(String document);
}