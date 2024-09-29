package main.java.com.team1.repository;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface para o repositório de clientes, fornecendo métodos para
 * gerenciar operações relacionadas a clientes na aplicação.
 * <p>
 * Esta interface define os métodos necessários para salvar, buscar,
 * atualizar e deletar clientes, permitindo a manipulação eficiente
 * dos dados de clientes.
 * </p>
 */
public interface CustomerRepository {

    /**
     * Salva um novo cliente no repositório.
     *
     * @param customerDTO O objeto {@link CustomerDTO} que representa o cliente a ser salvo.
     * @throws DuplicateEntityException Se já existir um cliente com os mesmos dados.
     */
    void save(CustomerDTO customerDTO) throws DuplicateEntityException;

    /**
     * Retorna uma lista de todos os clientes presentes no repositório.
     *
     * @return Uma lista contendo todos os objetos {@link CustomerDTO}.
     */
    List<CustomerDTO> findAll();

    /**
     * Atualiza as informações de um cliente existente no repositório.
     *
     * @param customer O objeto {@link CustomerDTO} que representa o cliente a ser atualizado.
     * @param newName O novo nome do cliente.
     * @param newAddress O novo endereço do cliente.
     * @param newPhone O novo telefone do cliente.
     * @throws EntityNotFoundException Se o cliente a ser atualizado não for encontrado.
     */
    void update(CustomerDTO customer, String newName, String newAddress, String newPhone) throws EntityNotFoundException;

    /**
     * Remove um cliente do repositório.
     *
     * @param customerDTO O objeto {@link CustomerDTO} que representa o cliente a ser removido.
     * @throws EntityNotFoundException Se o cliente a ser removido não for encontrado.
     */
    void delete(CustomerDTO customerDTO) throws EntityNotFoundException;

    /**
     * Busca um cliente no repositório com base no documento.
     *
     * @param document O documento do cliente a ser buscado.
     * @return O objeto {@link CustomerDTO} que representa o cliente encontrado,
     *         ou {@code null} se nenhum cliente for encontrado com o documento especificado.
     */
    CustomerDTO findByDocument(String document);
}