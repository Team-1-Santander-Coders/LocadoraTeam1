package main.java.com.team1.repository;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementação da interface {@link RentalRepository}, responsável por gerenciar
 * as operações de CRUD (Create, Read, Update, Delete) para locações.
 * <p>
 * Esta classe utiliza a classe {@link FileUtil} para ler e escrever locações em um arquivo
 * de dados persistente.
 * </p>
 */
public class RentalRepositoryImpl implements RentalRepository {
    private static final String RENTAL_FILE = "src/resources/data/rentals.dat";

    /**
     * Salva uma nova locação no arquivo de dados.
     *
     * @param rentalDTO O objeto {@link RentalDTO} a ser salvo.
     * @throws DuplicateEntityException Se a locação já estiver cadastrada.
     */
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

    /**
     * Retorna todas as locações cadastradas.
     *
     * @return Uma lista de {@link RentalDTO} contendo todas as locações.
     *         Se não houver locações cadastradas, retorna uma lista vazia.
     */
    @Override
    public List<RentalDTO> findAll() {
        List<RentalDTO> rentals = FileUtil.readFromFile(RENTAL_FILE);
        return rentals != null ? rentals : new ArrayList<>();
    }

    /**
     * Atualiza uma locação existente no arquivo de dados.
     *
     * @param updatedRentalDTO O objeto {@link RentalDTO} atualizado.
     * @throws EntityNotFoundException Se a locação não for encontrada.
     */
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

    /**
     * Remove uma locação com base na placa do veículo e no documento do cliente.
     *
     * @param vehiclePlate A placa do veículo da locação.
     * @param customerDocument O documento do cliente da locação.
     * @throws EntityNotFoundException Se a locação não for encontrada.
     */
    @Override
    public void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException {
        List<RentalDTO> rentals = FileUtil.readFromFile(RENTAL_FILE);
        assert rentals != null;
        rentals.removeIf(rental -> rental.vehiclePlate().equals(vehiclePlate) && rental.customerDocument().equals(customerDocument));
        FileUtil.writeToFile(rentals, RENTAL_FILE);
    }
}
