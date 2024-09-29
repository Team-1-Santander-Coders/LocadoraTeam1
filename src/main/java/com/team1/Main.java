package main.java.com.team1;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.server.MainServer;
import main.java.com.team1.service.RentalService;
import main.java.com.team1.service.VehicleService;
import main.java.com.team1.util.DateUtil;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) {
        try {
            RentalService rentalService = new RentalService();
            List<RentalDTO> rentals = rentalService.getAllRentals();
            System.out.println(rentals);
            VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());
            vehicleService.deleteVehicle("PMT3C15");
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            Stream<VehicleDTO> vehicleStream = vehicles.stream();

            String response = vehicleStream
                    .map(vehicle -> vehicle.getTipo() + " - " + vehicle.getMarca() +" - " + vehicle.getPlaca() + " - " + vehicle.getModelo() + " - " + vehicle.getAno() +
                            " - " + (vehicle.isDisponivel() ? "Disponível" : "Indisponível") + " - {" + vehicle.getAgency().name() + " - " + vehicle.getAgency().address() + "}")
                    .collect(Collectors.joining("\n"));

            System.out.println(response);
        } catch (Exception e) {
            FileUtil.logError(e);
        }

        try {
            MainServer.startServer();
        } catch (IOException e) {
            FileUtil.logError(e);
        }
    }
}
