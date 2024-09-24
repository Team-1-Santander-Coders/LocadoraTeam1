import java.util.List;
package main.java.com.team1.repository;

public interface RentalRepository {
    void save(RentalDTO rentalDTO) throws DuplicateEntityException; // Salva um aluguel, lançando exceção se já existir
    List<RentalDTO> findAll(); // Recupera todos os aluguéis
    void update(RentalDTO updatedRentalDTO) throws EntityNotFoundException; // Atualiza o aluguel existente
    void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException; // Remove um aluguel
    List<RentalDTO> findByDate(String date); // Busca aluguéis por data
}
