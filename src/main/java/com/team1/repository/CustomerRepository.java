package main.java.com.team1.repository;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

public interface CustomerRepository {

    public void save(CustomerDTO customerDTO) throws DuplicateEntityException;

    public List<CustomerDTO> findAll();

    public void update(CustomerDTO customer, String newName, String newAddress, String newPhone) throws EntityNotFoundException;

    public void delete(CustomerDTO customerDTO) throws EntityNotFoundException;

    public CustomerDTO findByDocument(String document);
}