package main.java.com.team1.repository;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface que define as operações de persistência para a entidade {@link AgencyDTO}.
 * <p>
 * Esta interface contém métodos para salvar, buscar, atualizar e deletar agências
 * no repositório de dados.
 * </p>
 */

public interface AgencyRepository {
    /**
     * Salva uma nova agência no repositório.
     *
     * @param agencyDTO O objeto {@link AgencyDTO} que representa a agência a ser salva.
     * @throws DuplicateEntityException Se já existir uma agência com os mesmos dados.
     */
    void save(AgencyDTO agencyDTO) throws DuplicateEntityException;

    /**
     * Retorna uma lista de todas as agências presentes no repositório.
     *
     * @return Uma lista contendo todos os objetos {@link AgencyDTO}.
     */
    List<AgencyDTO> findAll();

    /**
     * Atualiza as informações de uma agência existente no repositório.
     *
     * @param agencyDTO O objeto {@link AgencyDTO} que representa a agência a ser atualizada.
     * @param newName O novo nome da agência.
     * @param newAddress O novo endereço da agência.
     * @throws EntityNotFoundException Se a agência a ser atualizada não for encontrada.
     */
    void update(AgencyDTO agencyDTO, String newName, String newAddress) throws EntityNotFoundException;

    /**
     * Remove uma agência do repositório com base no nome e endereço.
     *
     * @param name O nome da agência a ser removida.
     * @param address O endereço da agência a ser removida.
     * @throws EntityNotFoundException Se a agência a ser removida não for encontrada.
     */
    void delete(String name, String address) throws EntityNotFoundException;

    /**
     * Busca uma agência no repositório com base no nome e endereço.
     *
     * @param name O nome da agência a ser buscada.
     * @param address O endereço da agência a ser buscada.
     * @return O objeto {@link AgencyDTO} que representa a agência encontrada.
     * @throws EntityNotFoundException Se a agência não for encontrada.
     */
    AgencyDTO findByNameAndAddress(String name, String address) throws EntityNotFoundException;
}
