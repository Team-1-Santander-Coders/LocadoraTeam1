package main.java.com.team1.repository;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface que define os métodos CRUD para gerenciar aluguéis.
 * Inclui métodos para salvar, buscar, atualizar e excluir aluguéis.
 */

public interface RentalRepository {
    void save(RentalDTO rentalDTO) throws DuplicateEntityException; // Salva um aluguel, lançando exceção se já existir
    List<RentalDTO> findAll(); // Recupera todos os aluguéis
    void update(RentalDTO updatedRentalDTO) throws EntityNotFoundException; // Atualiza o aluguel existente
    void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException; // Remove um aluguel
    List<RentalDTO> findByDate(String date); // Busca aluguéis por data
}
