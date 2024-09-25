package main.java.com.team1.service;

import main.java.com.team1.dto.CarDTO;
import main.java.com.team1.dto.MotorcycleDTO;
import main.java.com.team1.dto.TruckDTO;
import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.exception.RentIllegalUpdateException;
import main.java.com.team1.repository.VehicleRepository;
import main.java.com.team1.util.FileUtil;

import java.util.List;

/**
 * Classe de serviço responsável por gerenciar operações sobre veículos.
 * <p>
 * Esta classe fornece métodos para adicionar, atualizar, excluir, buscar e
 * gerenciar a disponibilidade (aluguel e devolução) de veículos.
 * </p>
 */
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    /**
     * Constrói um VehicleService com o repositório de veículos fornecido.
     *
     * @param vehicleRepository o repositório usado para operações de veículos.
     */
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Adiciona um novo veículo ao repositório.
     *
     * @param vehicleDTO os dados do veículo a serem adicionados.
     * @throws DuplicateEntityException se um veículo com a mesma placa já existir.
     */
    public void addVehicle(VehicleDTO vehicleDTO) throws DuplicateEntityException {
        vehicleRepository.save(vehicleDTO);
    }

    /**
     * Recupera todos os veículos do repositório.
     *
     * @return uma lista de todos os veículos.
     */
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Atualiza os detalhes de um veículo existente.
     *
     * @param vehicleDTO os dados atualizados do veículo.
     * @throws EntityNotFoundException se o veículo a ser atualizado não for encontrado.
     */
    public void updateVehicle(VehicleDTO vehicleDTO) throws EntityNotFoundException {
        vehicleRepository.update(vehicleDTO);
    }

    /**
     * Aluga um veículo, alterando sua disponibilidade para não disponível.
     *
     * @param vehicleDTO os dados do veículo a ser alugado.
     * @throws RentIllegalUpdateException se o veículo já estiver alugado.
     */
    public void rentVehicle(VehicleDTO vehicleDTO) throws RentIllegalUpdateException {
        if (!vehicleDTO.isDisponivel()) {
            throw new RentIllegalUpdateException(vehicleDTO.getTipo() + " já está alugado.");
        }

        switch (vehicleDTO.getTipo()) {
            case "Carro":
                CarDTO rentedCar = new CarDTO(vehicleDTO.getPlaca(), vehicleDTO.getModelo(),
                        vehicleDTO.getMarca(), vehicleDTO.getAno(), false, vehicleDTO.getPrecoDiaria());
                updateVehicleInRepository(rentedCar);
                break;
            case "Moto":
                MotorcycleDTO rentedMotorcycle = new MotorcycleDTO(vehicleDTO.getPlaca(), vehicleDTO.getModelo(),
                        vehicleDTO.getMarca(), vehicleDTO.getAno(), false, vehicleDTO.getPrecoDiaria());
                updateVehicleInRepository(rentedMotorcycle);
                break;
            case "Caminhão":
                TruckDTO rentedTruck = new TruckDTO(vehicleDTO.getPlaca(), vehicleDTO.getModelo(),
                        vehicleDTO.getMarca(), vehicleDTO.getAno(), false, vehicleDTO.getPrecoDiaria());
                updateVehicleInRepository(rentedTruck);
                break;
        }
    }

    /**
     * Devolve um veículo, alterando sua disponibilidade para disponível.
     *
     * @param vehicleDTO os dados do veículo a ser devolvido.
     * @throws RentIllegalUpdateException se o veículo já estiver disponível.
     */
    public void returnCar(VehicleDTO vehicleDTO) throws RentIllegalUpdateException {
        if (vehicleDTO.isDisponivel()) {
            throw new RentIllegalUpdateException(vehicleDTO.getTipo() + " já está disponível.");
        }

        switch (vehicleDTO.getTipo()) {
            case "Carro":
                CarDTO rentedCar = new CarDTO(vehicleDTO.getPlaca(), vehicleDTO.getModelo(),
                        vehicleDTO.getMarca(), vehicleDTO.getAno(), true, vehicleDTO.getPrecoDiaria());
                updateVehicleInRepository(rentedCar);
                break;
            case "Moto":
                MotorcycleDTO rentedMotorcycle = new MotorcycleDTO(vehicleDTO.getPlaca(), vehicleDTO.getModelo(),
                        vehicleDTO.getMarca(), vehicleDTO.getAno(), true, vehicleDTO.getPrecoDiaria());
                updateVehicleInRepository(rentedMotorcycle);
                break;
            case "Caminhão":
                TruckDTO rentedTruck = new TruckDTO(vehicleDTO.getPlaca(), vehicleDTO.getModelo(),
                        vehicleDTO.getMarca(), vehicleDTO.getAno(), true, vehicleDTO.getPrecoDiaria());
                updateVehicleInRepository(rentedTruck);
                break;
        }
    }

    /**
     * Exclui um veículo pelo número da placa.
     *
     * @param placa o número da placa do veículo a ser excluído.
     * @throws EntityNotFoundException se o veículo com a placa dada não for encontrado.
     */
    public void deleteVehicle(String placa) throws EntityNotFoundException {
        vehicleRepository.delete(placa);
    }

    /**
     * Recupera um veículo pelo número da placa.
     *
     * @param placa o número da placa do veículo a ser recuperado.
     * @return o veículo com o número da placa dado.
     * @throws EntityNotFoundException se o veículo com a placa dada não for encontrado.
     */
    public VehicleDTO getVehicleByPlaca(String placa) throws EntityNotFoundException {
        return vehicleRepository.findByPlaca(placa);
    }

    /**
     * Busca veículos pelo nome (modelo).
     *
     * @param nome o nome ou parte do nome do veículo a ser buscado.
     * @return uma lista de veículos que correspondem ao nome dado.
     */
    public List<VehicleDTO> searchVehicleByName(String nome) {
        return vehicleRepository.findByNome(nome);
    }

    /**
     * Recupera uma lista paginada de veículos.
     *
     * @param pageNumber o número da página (começando de 0).
     * @param pageSize   o número de veículos por página.
     * @return uma sublista de veículos representando a página solicitada.
     */
    public List<VehicleDTO> getVehiclesByPage(int pageNumber, int pageSize) {
        List<VehicleDTO> allVehicles = vehicleRepository.findAll();
        int fromIndex = Math.min(pageNumber * pageSize, allVehicles.size());
        int toIndex = Math.min(fromIndex + pageSize, allVehicles.size());
        return allVehicles.subList(fromIndex, toIndex);
    }

    /**
     * Atualiza as informações de um veículo no repositório.
     *
     * @param vehicleDTO O veículo a ser atualizado.
     */
    private void updateVehicleInRepository(VehicleDTO vehicleDTO) {
        try {
            vehicleRepository.update(vehicleDTO);
        } catch (EntityNotFoundException e) {
            FileUtil.logError(e);
        }
    }
}
