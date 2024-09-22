package main.java.com.team1.test;

import static org.junit.jupiter.api.Assertions.*;

import main.java.com.team1.dto.CarDTO;
import main.java.com.team1.dto.MotorcycleDTO;
import main.java.com.team1.dto.TruckDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.VehicleRepositoryImpl;

import java.io.*;

public class VehicleRepositoryImplTest {
    private VehicleRepositoryImpl vehicleRepository;
    private static final String TEST_FILE = "src/resources/data/vehicles.dat";

    @BeforeEach
    public void setUp() {
        vehicleRepository = new VehicleRepositoryImpl();
        clearTestFile();
    }

    public static void clearTestFile() {
        File file = new File(TEST_FILE).getAbsoluteFile();
        if (file.exists()) {
            try {
                new FileWriter(file, false).close();
                System.out.println("Arquivo limpo.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Arquivo não encontrado.");
        }
    }


    // Testes para Car
    @Test
    public void testSaveCar() throws DuplicateEntityException {
        CarDTO car = new CarDTO("ABC1234", "Fusca", "Volkswagen", 1970, true, 150);
        vehicleRepository.save(car);
        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    public void testFindCarByPlaca() throws EntityNotFoundException, DuplicateEntityException {
        CarDTO car = new CarDTO("ABC1236", "Civic", "Honda", 2020, true, 200);
        vehicleRepository.save(car);
        CarDTO foundCar = (CarDTO) vehicleRepository.findByPlaca("ABC1236");
        assertEquals(car.getPlaca(), foundCar.getPlaca());
    }

    @Test
    public void testUpdateCar() throws EntityNotFoundException, DuplicateEntityException {
        CarDTO car = new CarDTO("JKL9012", "Corolla", "Toyota", 2020, true, 180);
        vehicleRepository.save(car);
        CarDTO updatedCar = new CarDTO("JKL9012", "Corolla", "Toyota", 2022, true, 180);
        vehicleRepository.update(updatedCar);
        CarDTO foundCar = (CarDTO) vehicleRepository.findByPlaca("JKL9012");
        assertEquals(2022, foundCar.getAno());
    }

    @Test
    public void testDeleteCar() throws EntityNotFoundException, DuplicateEntityException {
        CarDTO car = new CarDTO("MNO3456", "Civic", "Honda", 2019, true, 200);
        vehicleRepository.save(car);
        vehicleRepository.delete("MNO3456");
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleRepository.findByPlaca("MNO3456");
        });
    }

    @Test
    public void testSaveDuplicateCar() throws DuplicateEntityException {
        CarDTO car1 = new CarDTO("ABC1235", "Fusca", "Volkswagen", 1970, true, 150);
        CarDTO car2 = new CarDTO("ABC1235", "Civic", "Honda", 2020, true, 200);
        vehicleRepository.save(car1);
        Exception exception = assertThrows(DuplicateEntityException.class, () -> {
            vehicleRepository.save(car2);
        });
        assertEquals("Veículo com a placa ABC1235 já existe.", exception.getMessage());
    }

    // Testes para Truck
    @Test
    public void testSaveTruck() throws DuplicateEntityException {
        TruckDTO truck = new TruckDTO("TRK5676", "Volvo FH", "Volvo", 2021, true, 200);
        vehicleRepository.save(truck);
        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    public void testFindTruckByPlaca() throws EntityNotFoundException, DuplicateEntityException {
        TruckDTO truck = new TruckDTO("TRK5679", "Scania R", "Scania", 2020, true, 250);
        vehicleRepository.save(truck);
        TruckDTO foundTruck = (TruckDTO) vehicleRepository.findByPlaca("TRK5679");
        assertEquals(truck.getPlaca(), foundTruck.getPlaca());
    }

    @Test
    public void testUpdateTruck() throws EntityNotFoundException, DuplicateEntityException {
        TruckDTO truck = new TruckDTO("TRK5680", "Volvo FH", "Volvo", 2021, true, 200);
        vehicleRepository.save(truck);
        TruckDTO updatedTruck = new TruckDTO("TRK5680", "Volvo FH", "Volvo", 2022, true, 200);
        vehicleRepository.update(updatedTruck);
        TruckDTO foundTruck = (TruckDTO) vehicleRepository.findByPlaca("TRK5680");
        assertEquals(2022, foundTruck.getAno());
    }

    @Test
    public void testDeleteTruck() throws EntityNotFoundException, DuplicateEntityException {
        TruckDTO truck = new TruckDTO("TRK5678", "Volvo FH", "Volvo", 2021, true, 200);
        vehicleRepository.save(truck);
        vehicleRepository.delete("TRK5678");
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleRepository.findByPlaca("TRK5678");
        });
    }

    @Test
    public void testSaveDuplicateTruck() throws DuplicateEntityException {
        TruckDTO truck1 = new TruckDTO("TRK5678", "Volvo FH", "Volvo", 2021, true, 200);
        TruckDTO truck2 = new TruckDTO("TRK5678", "Scania R", "Scania", 2020, true, 250);
        vehicleRepository.save(truck1);
        Exception exception = assertThrows(DuplicateEntityException.class, () -> {
            vehicleRepository.save(truck2);
        });
        assertEquals("Veículo com a placa TRK5678 já existe.", exception.getMessage());
    }

    // Testes para Motorcycle
    @Test
    public void testSaveMotorcycle() throws DuplicateEntityException {
        MotorcycleDTO motorcycle = new MotorcycleDTO("MOT1239", "CB500", "Honda", 2019, true, 100);
        vehicleRepository.save(motorcycle);
        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    public void testFindMotorcycleByPlaca() throws EntityNotFoundException, DuplicateEntityException {
        MotorcycleDTO motorcycle = new MotorcycleDTO("MOT1236", "MT-07", "Yamaha", 2020, true, 120);
        vehicleRepository.save(motorcycle);
        MotorcycleDTO foundMotorcycle = (MotorcycleDTO) vehicleRepository.findByPlaca("MOT1236");
        assertEquals(motorcycle.getPlaca(), foundMotorcycle.getPlaca());
    }

    @Test
    public void testUpdateMotorcycle() throws EntityNotFoundException, DuplicateEntityException {
        MotorcycleDTO motorcycle = new MotorcycleDTO("MOT1234", "CB500", "Honda", 2019, true, 100);
        vehicleRepository.save(motorcycle);
        MotorcycleDTO updatedMotorcycle = new MotorcycleDTO("MOT1234", "CB500", "Honda", 2021, true, 100);
        vehicleRepository.update(updatedMotorcycle);
        MotorcycleDTO foundMotorcycle = (MotorcycleDTO) vehicleRepository.findByPlaca("MOT1234");
        assertEquals(2021, foundMotorcycle.getAno());
    }

    @Test
    public void testDeleteMotorcycle() throws EntityNotFoundException, DuplicateEntityException {
        MotorcycleDTO motorcycle = new MotorcycleDTO("MOT1240", "CB500", "Honda", 2019, true, 100);
        vehicleRepository.save(motorcycle);
        vehicleRepository.delete("MOT1234");
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleRepository.findByPlaca("MOT1234");
        });
    }

    @Test
    public void testSaveDuplicateMotorcycle() throws DuplicateEntityException {
        MotorcycleDTO motorcycle1 = new MotorcycleDTO("MOT1235", "CB500", "Honda", 2019, true, 100);
        MotorcycleDTO motorcycle2 = new MotorcycleDTO("MOT1235", "MT-07", "Yamaha", 2020, true, 120);
        vehicleRepository.save(motorcycle1);
        Exception exception = assertThrows(DuplicateEntityException.class, () -> {
            vehicleRepository.save(motorcycle2);
        });
        assertEquals("Veículo com a placa MOT1235 já existe.", exception.getMessage());
    }
}
