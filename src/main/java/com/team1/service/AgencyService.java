package main.java.com.team1.service;

import main.java.com.team1.dto.*;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.AgencyRepository;
import main.java.com.team1.repository.AgencyRepositporyImpl;

import java.util.List;

/**
 * A classe {@code AgencyService} fornece serviços para gerenciar agências.
 * Ela encapsula a lógica de negócios relacionada a agências e interage com o repositório de agências.
 */
public class AgencyService {
    private final AgencyRepository agencyRepository = new AgencyRepositporyImpl();

    /**
     * Adiciona uma nova agência.
     *
     * @param agencyDTO O objeto {@code AgencyDTO} contendo os dados da agência a ser adicionada.
     * @throws DuplicateEntityException Se a agência já estiver cadastrada.
     */
    public void addAgency(AgencyDTO agencyDTO) throws DuplicateEntityException {
        agencyRepository.save(agencyDTO);
    }

    /**
     * Recupera todas as agências.
     *
     * @return Uma lista de objetos {@code AgencyDTO} representando todas as agências.
     */
    public List<AgencyDTO> getAllAgencies() {
        return agencyRepository.findAll();
    }

    /**
     * Pesquisa agências pelo nome.
     *
     * @param namePart Parte do nome da agência a ser pesquisada.
     * @return Uma lista de objetos {@code AgencyDTO} que contêm a parte do nome especificada.
     */
    public List<AgencyDTO> searchAgencyByName(String namePart) {
        List<AgencyDTO> allAgencies = getAllAgencies();
        assert allAgencies != null;
        return allAgencies.stream()
                .filter(agency -> agency.name().contains(namePart))
                .toList();
    }

    /**
     * Atualiza os dados de uma agência existente.
     *
     * @param agencyDTO O objeto {@code AgencyDTO} com os dados da agência a ser atualizada.
     * @param name O novo nome da agência.
     * @param address O novo endereço da agência.
     * @throws EntityNotFoundException Se a agência não for encontrada.
     */
    public void updateAgency(AgencyDTO agencyDTO, String name, String address) throws EntityNotFoundException {
        agencyRepository.update(agencyDTO, name, address);
    }

    /**
     * Exclui uma agência.
     *
     * @param name O nome da agência a ser excluída.
     * @param address O endereço da agência a ser excluída.
     * @throws EntityNotFoundException Se a agência não for encontrada.
     */
    public void deleteAgency(String name, String address) throws EntityNotFoundException {
        agencyRepository.delete(name, address);
    }

    /**
     * Recupera uma lista de agências paginada.
     *
     * @param pageNumber O número da página a ser recuperada.
     * @param pageSize O número de agências por página.
     * @return Uma lista de objetos {@code AgencyDTO} para a página especificada.
     */
    public List<AgencyDTO> getAgenciesByPage(int pageNumber, int pageSize) {
        List<AgencyDTO> allAgencies = getAllAgencies();
        int fromIndex = Math.min(pageNumber * pageSize, allAgencies.size());
        int toIndex = Math.min(fromIndex + pageSize, allAgencies.size());
        return allAgencies.subList(fromIndex, toIndex);
    }
}
