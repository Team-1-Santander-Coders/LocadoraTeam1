package main.java.com.team1.test;

import main.java.com.team1.dto.CarDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleServiceTest {
    private MockVehicleRepository vehicleRepository;
    private VehicleService vehicleService;

    @BeforeEach
    public void setUp() {
        vehicleRepository = new MockVehicleRepository();
        vehicleService = new VehicleService(vehicleRepository);
    }

    @Test
    public void testAddVehicle() throws DuplicateEntityException {
        CarDTO car = new CarDTO("ABC1234", "Modelo", "Marca", 2020, true, 100.00);
        vehicleService.addVehicle(car);
        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    public void testAddVehicleDuplicate() {
        CarDTO car = new CarDTO("ABC1234", "Modelo", "Marca", 2020, true, 100.00);
        Exception exception = assertThrows(DuplicateEntityException.class, () -> {
            vehicleService.addVehicle(car);
            vehicleService.addVehicle(car);
        });
        assertEquals("Veículo com a placa ABC1234 já existe.", exception.getMessage());
    }

    @Test
    public void testUpdateVehicle() throws EntityNotFoundException, DuplicateEntityException {
        CarDTO car = new CarDTO("ABC1234", "Modelo", "Marca", 2020, true, 100.00);
        vehicleService.addVehicle(car);
        CarDTO updatedCar = new CarDTO("ABC1234", "Modelo Atualizado", "Marca", 2022, true, 100.00);
        vehicleService.updateVehicle(updatedCar);
        CarDTO foundCar = (CarDTO) vehicleRepository.findByPlaca("ABC1234");
        assertEquals(2022, foundCar.getAno());
    }

    @Test
    public void testUpdateVehicleNotFound() {
        CarDTO car = new CarDTO("XYZ5678", "Modelo", "Marca", 2020, true, 100.00);
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.updateVehicle(car);
        });
        assertEquals("Veículo com a placa XYZ5678 não encontrado.", exception.getMessage());
    }

    @Test
    public void testDeleteVehicle() throws EntityNotFoundException, DuplicateEntityException {
        CarDTO car = new CarDTO("ABC1234", "Modelo", "Marca", 2020, true, 100.00);
        vehicleService.addVehicle(car);
        vehicleService.deleteVehicle("ABC1234");
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleRepository.findByPlaca("ABC1234");
        });
    }

    @Test
    public void testGetVehicleByPlaca() throws EntityNotFoundException, DuplicateEntityException {
        CarDTO car = new CarDTO("ABC1234", "Modelo", "Marca", 2020, true, 100.00);
        vehicleService.addVehicle(car);
        CarDTO foundCar = (CarDTO) vehicleService.getVehicleByPlaca("ABC1234");
        assertEquals(car.getPlaca(), foundCar.getPlaca());
    }

    @Test
    public void testGetVehicleByPlacaNotFound() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.getVehicleByPlaca("XYZ5678");
        });
        assertEquals("Veículo com a placa XYZ5678 não encontrado.", exception.getMessage());
    }
}
