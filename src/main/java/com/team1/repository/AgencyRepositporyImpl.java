package main.java.com.team1.repository;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementação da interface {@link AgencyRepository} para o gerenciamento
 * de agências usando um arquivo para persistência de dados.
 * <p>
 * Esta classe fornece métodos para salvar, buscar, atualizar e deletar agências,
 * utilizando o utilitário {@link FileUtil} para operações de leitura e escrita
 * em um arquivo específico.
 * </p>
 */
public class AgencyRepositporyImpl implements AgencyRepository {
    private static final String FILE_NAME = "src/resources/data/agencies.dat";

    /**
     * Salva uma nova agência no repositório.
     *
     * @param agencyDTO O objeto {@link AgencyDTO} que representa a agência a ser salva.
     * @throws DuplicateEntityException Se já existir uma agência com os mesmos dados.
     */
    @Override
    public void save(AgencyDTO agencyDTO) throws DuplicateEntityException {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        if (agencies == null) {
            agencies = new ArrayList<>();
        }
        if (agencies.stream().anyMatch(a -> a.equals(agencyDTO))) {
            throw new DuplicateEntityException("Agência já cadastrada.");
        }
        agencies.add(agencyDTO);
        FileUtil.writeToFile(agencies, FILE_NAME);
    }

    /**
     * Retorna uma lista de todas as agências presentes no repositório.
     *
     * @return Uma lista contendo todos os objetos {@link AgencyDTO}.
     */
    @Override
    public List<AgencyDTO> findAll() {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        return agencies != null ? agencies : new ArrayList<>();
    }

    /**
     * Atualiza as informações de uma agência existente no repositório.
     *
     * @param updatedAgencyDTO O objeto {@link AgencyDTO} com as novas informações.
     * @param name O novo nome da agência.
     * @param address O novo endereço da agência.
     * @throws EntityNotFoundException Se a agência a ser atualizada não for encontrada.
     */
    @Override
    public void update(AgencyDTO updatedAgencyDTO, String name, String address) throws EntityNotFoundException {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        if (agencies == null) {
            agencies = new ArrayList<>();
        }
        boolean found = false;
        for (int i = 0; i < Objects.requireNonNull(agencies).size(); i++) {
            if (agencies.get(i).equals(updatedAgencyDTO)) {
                agencies.set(i, new AgencyDTO(name, address));
                found = true;
                break;
            }
        }
        if (!found) {
            throw new EntityNotFoundException("Agência não encontrada.");
        }
        FileUtil.writeToFile(agencies, FILE_NAME);
    }

    /**
     * Remove uma agência do repositório com base no nome e endereço.
     *
     * @param name O nome da agência a ser removida.
     * @param address O endereço da agência a ser removida.
     * @throws EntityNotFoundException Se a agência a ser removida não for encontrada.
     */
    @Override
    public void delete(String name, String address) throws EntityNotFoundException {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        assert agencies != null;
        AgencyDTO agencyDTO = findByNameAndAddress(name, address);
        agencies.removeIf(agency -> agency.equals(agencyDTO));
        FileUtil.writeToFile(agencies, FILE_NAME);
    }

    /**
     * Busca uma agência no repositório com base no nome e endereço.
     *
     * @param name O nome da agência a ser buscada.
     * @param address O endereço da agência a ser buscada.
     * @return O objeto {@link AgencyDTO} que representa a agência encontrada.
     * @throws EntityNotFoundException Se a agência não for encontrada.
     */
    @Override
    public AgencyDTO findByNameAndAddress(String name, String address) throws EntityNotFoundException {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        for (AgencyDTO agency : agencies) {
            if (agency.name().equals(name) && agency.address().equals(address)) {
                return agency;
            }
        }
        throw new EntityNotFoundException("Agência não encontrada");
    }
}
