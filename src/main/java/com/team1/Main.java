package main.java.com.team1;

import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.server.VehicleServer;
import main.java.com.team1.service.VehicleService;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // VehicleRepositoryImpl vehicleRepository = new VehicleRepositoryImpl();
        // VehicleService vehicleService = new VehicleService(vehicleRepository);
        // System.out.println(vehicleService.getAllVehicles());
        try {
            VehicleServer.StartServer();
        } catch (IOException e) {
            FileUtil.logError(e);
        }
    }
}
