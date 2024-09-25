package main.java.com.team1.repository;

import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementação do repositório de veículos. Esta classe utiliza o arquivo `vehicles.dat`
 * para armazenar e manipular dados de veículos em formato de lista.
 */
public class VehicleRepositoryImpl implements VehicleRepository {
    private static final String FILE_NAME = "src/resources/data/vehicles.dat";

    /**
     * Salva um novo veículo no repositório.
     * Se já existir um veículo com a mesma placa, lança uma exceção.
     *
     * @param vehicleDTO O veículo a ser salvo.
     * @throws DuplicateEntityException Se já existir um veículo com a mesma placa.
     */
    @Override
    public void save(VehicleDTO vehicleDTO) throws DuplicateEntityException {
        List<VehicleDTO> vehicles = FileUtil.readFromFile(FILE_NAME);
        if (vehicles == null) {
            vehicles = new ArrayList<>();
        }
        if (vehicles.stream().anyMatch(v -> v.getPlaca().equals(vehicleDTO.getPlaca()))) {
            throw new DuplicateEntityException("Veículo com a placa " + vehicleDTO.getPlaca() + " já existe.");
        }
        vehicles.add(vehicleDTO);
        FileUtil.writeToFile(vehicles, FILE_NAME);
    }

    /**
     * Retorna todos os veículos armazenados no repositório.
     *
     * @return Uma lista de veículos. Se não houver veículos, retorna uma lista vazia.
     */
    @Override
    public List<VehicleDTO> findAll() {
        List<VehicleDTO> vehicles = FileUtil.readFromFile(FILE_NAME);
        return vehicles != null ? vehicles : new ArrayList<>();
    }

    /**
     * Atualiza as informações de um veículo existente.
     *
     * @param updatedVehicleDTO O veículo atualizado.
     * @throws EntityNotFoundException Se o veículo com a placa fornecida não for encontrado.
     */
    @Override
    public void update(VehicleDTO updatedVehicleDTO) throws EntityNotFoundException {
        List<VehicleDTO> vehicles = FileUtil.readFromFile(FILE_NAME);
        if (vehicles == null) {
            vehicles = new ArrayList<>();
        }
        boolean found = false;
        for (int i = 0; i < Objects.requireNonNull(vehicles).size(); i++) {
            if (vehicles.get(i).getPlaca().equals(updatedVehicleDTO.getPlaca())) {
                vehicles.set(i, updatedVehicleDTO);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new EntityNotFoundException("Veículo com a placa " + updatedVehicleDTO.getPlaca() + " não encontrado.");
        }
        FileUtil.writeToFile(vehicles, FILE_NAME);
    }

    /**
     * Exclui um veículo com base na placa.
     *
     * @param placa A placa do veículo a ser excluído.
     * @throws EntityNotFoundException Se o veículo com a placa fornecida não for encontrado.
     */
    @Override
    public void delete(String placa) throws EntityNotFoundException {
        List<VehicleDTO> vehicles = FileUtil.readFromFile(FILE_NAME);
        assert vehicles != null;
        vehicles.removeIf(vehicle -> vehicle.getPlaca().equals(placa));
        FileUtil.writeToFile(vehicles, FILE_NAME);
    }

    /**
     * Encontra um veículo com base na placa.
     *
     * @param placa A placa do veículo a ser encontrado.
     * @return O veículo correspondente.
     * @throws EntityNotFoundException Se o veículo com a placa fornecida não for encontrado.
     */
    @Override
    public VehicleDTO findByPlaca(String placa) throws EntityNotFoundException {
        List<VehicleDTO> vehicles = FileUtil.readFromFile(FILE_NAME);
        assert vehicles != null;
        return vehicles.stream()
                .filter(vehicle -> vehicle.getPlaca().equals(placa))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Veículo com a placa " + placa + " não encontrado."));
    }

    /**
     * Encontra veículos com base no nome do modelo.
     *
     * @param nome O nome ou parte do nome do modelo do veículo a ser encontrado.
     * @return Uma lista de veículos que correspondem ao nome.
     */
    @Override
    public List<VehicleDTO> findByNome(String nome) {
        List<VehicleDTO> vehicles = FileUtil.readFromFile(FILE_NAME);
        assert vehicles != null;
        return vehicles.stream()
                .filter(vehicle -> vehicle.getModelo().contains(nome))
                .toList();
    }
}
