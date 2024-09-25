package main.java.com.team1.service;

import main.java.com.team1.dto.*;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.AgencyRepository;
import main.java.com.team1.repository.AgencyRepositporyImpl;

import java.util.List;

public class AgencyService {
    private final AgencyRepository agencyRepository = new AgencyRepositporyImpl();

    public void addAgency(AgencyDTO agencyDTO) throws DuplicateEntityException {
        agencyRepository.save(agencyDTO);
    }

    public List<AgencyDTO> getAllAgencies() {
        return agencyRepository.findAll();
    }

    public List<AgencyDTO> searchAgencyByName(String namePart) {
        List<AgencyDTO> allAgencies = getAllAgencies();
        assert allAgencies != null;
        return allAgencies.stream()
                .filter(agency -> agency.name().contains(namePart))
                .toList();
    }

    public void updateAgency(AgencyDTO agencyDTO, String name, String address) throws EntityNotFoundException {
        agencyRepository.update(agencyDTO, name, address);
    }

    public void deleteAgency(String name, String address) throws EntityNotFoundException {
        agencyRepository.delete(name, address);
    }

    public List<AgencyDTO> getAgenciesByPage(int pageNumber, int pageSize) {
        List<AgencyDTO> allAgencies = getAllAgencies();
        int fromIndex = Math.min(pageNumber * pageSize, allAgencies.size());
        int toIndex = Math.min(fromIndex + pageSize, allAgencies.size());
        return allAgencies.subList(fromIndex, toIndex);
    }
}
