package main.java.com.team1.repository;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface para o repositório de locações, fornecendo métodos para
 * gerenciar operações relacionadas a locações de veículos na aplicação.
 * <p>
 * Esta interface define os métodos necessários para salvar, buscar,
 * atualizar e deletar locações, permitindo a manipulação eficiente
 * dos dados de locação de veículos.
 * </p>
 */
public interface RentalRepository {

    /**
     * Salva uma nova locação no repositório.
     *
     * @param rentalDTO O objeto {@link RentalDTO} que representa a locação a ser salva.
     * @throws DuplicateEntityException Se já existir uma locação com os mesmos dados.
     */
    void save(RentalDTO rentalDTO) throws DuplicateEntityException;

    /**
     * Retorna uma lista de todas as locações presentes no repositório.
     *
     * @return Uma lista contendo todos os objetos {@link RentalDTO}.
     */
    List<RentalDTO> findAll();

    /**
     * Atualiza as informações de uma locação existente no repositório.
     *
     * @param updatedRentalDTO O objeto {@link RentalDTO} que representa a locação a ser atualizada.
     * @throws EntityNotFoundException Se a locação a ser atualizada não for encontrada.
     */
    void update(RentalDTO updatedRentalDTO) throws EntityNotFoundException;

    /**
     * Remove uma locação do repositório com base na placa do veículo e no documento do cliente.
     *
     * @param vehiclePlate A placa do veículo da locação a ser removida.
     * @param customerDocument O documento do cliente da locação a ser removida.
     * @throws EntityNotFoundException Se a locação a ser removida não for encontrada.
     */
    void delete(String vehiclePlate, String customerDocument) throws EntityNotFoundException;
}
