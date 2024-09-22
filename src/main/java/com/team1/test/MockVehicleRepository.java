package main.java.com.team1.test;

import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;

public class MockVehicleRepository implements VehicleRepository {
    private final List<VehicleDTO> vehicles = new ArrayList<>();

    @Override
    public void save(VehicleDTO vehicleDTO) throws DuplicateEntityException {
        if (vehicles.stream().anyMatch(v -> v.getPlaca().equals(vehicleDTO.getPlaca()))) {
            throw new DuplicateEntityException("Veículo com a placa " + vehicleDTO.getPlaca() + " já existe.");
        }
        vehicles.add(vehicleDTO);
    }

    @Override
    public List<VehicleDTO> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public void update(VehicleDTO updatedVehicleDTO) throws EntityNotFoundException {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getPlaca().equals(updatedVehicleDTO.getPlaca())) {
                vehicles.set(i, updatedVehicleDTO);
                return;
            }
        }
        throw new EntityNotFoundException("Veículo com a placa " + updatedVehicleDTO.getPlaca() + " não encontrado.");
    }

    @Override
    public void delete(String placa) throws EntityNotFoundException {
        boolean removed = vehicles.removeIf(vehicle -> vehicle.getPlaca().equals(placa));
        if (!removed) {
            throw new EntityNotFoundException("Veículo com a placa " + placa + " não encontrado.");
        }
    }

    @Override
    public VehicleDTO findByPlaca(String placa) throws EntityNotFoundException {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getPlaca().equals(placa))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Veículo com a placa " + placa + " não encontrado."));
    }

    @Override
    public List<VehicleDTO> findByNome(String nome) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getModelo().contains(nome))
                .toList();
    }
}