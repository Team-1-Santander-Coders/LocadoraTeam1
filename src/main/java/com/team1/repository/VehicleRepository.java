package main.java.com.team1.repository;

import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface para o repositório de veículos. Define os métodos essenciais para
 * a manipulação de dados de veículos, incluindo operações de salvar, atualizar,
 * excluir e buscar veículos.
 */
public interface VehicleRepository {

    /**
     * Salva um novo veículo no repositório.
     *
     * @param vehicleDTO O veículo a ser salvo.
     * @throws DuplicateEntityException Se já existir um veículo com a mesma placa.
     */
    void save(VehicleDTO vehicleDTO) throws DuplicateEntityException;

    /**
     * Retorna todos os veículos armazenados no repositório.
     *
     * @return Uma lista de veículos. Se não houver veículos, retorna uma lista vazia.
     */
    List<VehicleDTO> findAll();

    /**
     * Atualiza as informações de um veículo existente.
     *
     * @param updatedVehicleDTO O veículo atualizado.
     * @throws EntityNotFoundException Se o veículo com a placa fornecida não for encontrado.
     */
    void update(VehicleDTO updatedVehicleDTO) throws EntityNotFoundException;

    /**
     * Exclui um veículo com base na placa.
     *
     * @param placa A placa do veículo a ser excluído.
     * @throws EntityNotFoundException Se o veículo com a placa fornecida não for encontrado.
     */
    void delete(String placa) throws EntityNotFoundException;

    /**
     * Encontra um veículo com base na placa.
     *
     * @param placa A placa do veículo a ser encontrado.
     * @return O veículo correspondente.
     * @throws EntityNotFoundException Se o veículo com a placa fornecida não for encontrado.
     */
    VehicleDTO findByPlaca(String placa) throws EntityNotFoundException;

    /**
     * Encontra veículos com base no nome do modelo.
     *
     * @param nome O nome ou parte do nome do modelo do veículo a ser encontrado.
     * @return Uma lista de veículos que correspondem ao nome.
     */
    List<VehicleDTO> findByNome(String nome);
}

