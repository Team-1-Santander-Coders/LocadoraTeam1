package main.java.com.team1;
import main.java.com.team1.dto.CarDTO;
import main.java.com.team1.dto.TruckDTO;
import main.java.com.team1.entities.Car;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.service.VehicleService;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.util.FileUtil;

public class Main {
    public static void main(String[] args) {
        Car car = new Car("XYZ1236", "Model S", "Tesla", 2022);
        CarDTO carDTO = new CarDTO(car.getPlaca(), car.getModelo(), car.getMarca(), car.getAno(), car.isDisponivel(), car.getPrecoDiaria());
        VehicleRepositoryImpl vehicleRepository = new VehicleRepositoryImpl();
        try {
            vehicleRepository.save(carDTO);
        } catch (DuplicateEntityException e) {
            FileUtil.logError(e);
        }
        VehicleService vehicles = new VehicleService(vehicleRepository);
        System.out.println(vehicles.getAllVehicles());
    }
}
