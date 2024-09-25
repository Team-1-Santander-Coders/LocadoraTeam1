package main.java.com.team1.repository;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface RentalRepository, gerencia os dados de aluguel.
 * Utiliza um arquivo para armazenar os registros de aluguel.
 */
public class RentalRepositoryImpl implements RentalRepository {
    private static final String FILE_NAME = "src/resources/data/rentals.dat";

    @Override
    public void save(RentalDTO rentalDTO) throws DuplicateEntityException {
        List<RentalDTO> rentals = FileUtil.readFromFile(FILE_NAME);
        if (rentals == null) {
            rentals = new ArrayList<>();
        }
        // Verifica se o aluguel já existe para o veículo e cliente informados.
        if (rentals.stream().anyMatch(r -> r.vehiclePlate().equals(rentalDTO.vehiclePlate()) && r.customerDocument().equals(rentalDTO.customerDocument()))) {
            throw new DuplicateEntityException("Aluguel já existe para o veículo " + rentalDTO.vehiclePlate() + " e cliente " + rentalDTO.customerDocument());
        }
        rentals.add(rentalDTO); // Adiciona o novo aluguel à lista.
        FileUtil.writeToFile(rentals, FILE_NAME); // Escreve a lista atualizada no arquivo.
    }

    @Override
    public List<RentalDTO> findAll() {
        List<RentalDTO> rentals = FileUtil.readFromFile(FILE_NAME);
        return rentals != null ? rentals : new ArrayList<>(); // Retorna todos os aluguéis ou uma lista vazia.
    }

    @Override
    public void update(RentalDTO updatedRentalDTO) throws EntityNotFoundException {
        List<RentalDTO> rentals = FileUtil.readFromFile(FILE_NAME);
        if (rentals == null) {
            rentals = new ArrayList<>();
        }
        boolean found = false; // Flag para verificar se o aluguel foi encontrado.
        for (int i = 0; i < rentals.size(); i++) {
            RentalDTO rental = rentals.get(i);
            // Verifica se o aluguel existe para o veículo e cliente informados.
            if (rental.vehiclePlate().equals(updatedRentalDTO.vehiclePlate()) && rental.customerDocument().equals(updatedRentalDTO.customerDocument())) {
                rentals.set(i, updatedRentalDTO); // Atualiza o aluguel existente.
                found = true; // Define a flag como verdadeira.
                break;
            }
        }
        if (!found) {
            throw new EntityNotFoundException("Aluguel não encontrado para o veículo " + updatedRentalDTO.vehiclePlate() + " e cliente " + updatedRentalDTO.customerDocument());
        }
        FileUtil.writeToFile(rentals, FILE_NAME); // Escreve a lista atualizada no arquivo.
    }

    @Override
    public void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException {
        List<RentalDTO> rentals = FileUtil.readFromFile(FILE_NAME);
        assert rentals != null; // Assegura que a lista não é nula.
        // Remove o aluguel correspondente ao veículo e cliente informados.
        rentals.removeIf(rental -> rental.vehiclePlate().equals(vehiclePlate) && rental.customerDocument().equals(customerDocument));
        FileUtil.writeToFile(rentals, FILE_NAME); // Escreve a lista atualizada no arquivo.
    }

    @Override
    public List<RentalDTO> findByDate(String date) {
        List<RentalDTO> rentals = FileUtil.readFromFile(FILE_NAME);
        assert rentals != null; // Assegura que a lista não é nula.
        // Filtra aluguéis pela data de aluguel.
        return rentals.stream()
                .filter(rental -> rental.rentalDate().toLocalDate().toString().equals(date))
                .toList();
    }
}

