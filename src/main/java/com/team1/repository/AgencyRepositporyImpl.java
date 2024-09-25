package main.java.com.team1.repository;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgencyRepositporyImpl implements AgencyRepository {
    private static final String FILE_NAME = "src/resources/data/agencies.dat";

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

    @Override
    public List<AgencyDTO> findAll() {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        return agencies != null ? agencies : new ArrayList<>();
    }

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

    @Override
    public void delete(String name, String address) throws EntityNotFoundException {
        List<AgencyDTO> agencies = FileUtil.readFromFile(FILE_NAME);
        assert agencies != null;
        AgencyDTO agencyDTO = findByNameAndAddress(name, address);
        agencies.removeIf(agency -> agency.equals(agencyDTO));
        FileUtil.writeToFile(agencies, FILE_NAME);
    }

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
