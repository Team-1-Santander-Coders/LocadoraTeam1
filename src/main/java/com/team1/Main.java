package main.java.com.team1;

import main.java.com.team1.dto.*;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.server.MainServer;
import main.java.com.team1.service.AgencyService;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.RentalService;
import main.java.com.team1.service.VehicleService;
import main.java.com.team1.util.DateUtil;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /*VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());
        AgencyService agencyService = new AgencyService();
        CustomerService customerService = new CustomerService();
        RentalService rentalService = new RentalService();

        try {
            System.out.println("----------------------------------------------------------------");
            CarDTO carDTO = new CarDTO("BCT6969", "Pinga", "Leornado", 2007, true, 150);
            vehicleService.addVehicle(carDTO);
            System.out.println(vehicleService.getAllVehicles());
            System.out.println("----------------------------------------------------------------");
            AgencyDTO agencyDTO = new AgencyDTO("Sim", "Rua AAAAAAAAAAAAAAAAAA, bairo ASKKASDLJASDJKL");
            agencyService.addAgency(agencyDTO);
            System.out.println(agencyService.getAllAgencies());
            System.out.println("----------------------------------------------------------------");
            UserDTO userDTO = new UserDTO("Alecsandro", "Rua sim", "27995002346", "11111111111", "a@a.com", "@Sim2469", "Física");
            customerService.addCustomer(userDTO);
            System.out.println(customerService.getAllCustomers());
            System.out.println("----------------------------------------------------------------");
            RentalDTO rentalDTO = new RentalDTO(carDTO, userDTO, agencyDTO, DateUtil.converterTextoParaData("10/09/2024"));
            rentalService.addRental(rentalDTO);
            System.out.println(rentalService.getAllRentals());
            System.out.println(rentalService.returnRental(rentalDTO,agencyDTO, "14/09/2024"));
            System.out.println("----------------------------------------------------------------");
            System.out.println(rentalService.getAllRentals());
        } catch (DuplicateEntityException | EntityNotFoundException e) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
        */

        CustomerService customerService = new CustomerService();
        System.out.println(customerService.getAll());
        try {
            MainServer.startServer();
        } catch (IOException e) {
            FileUtil.logError(e);
        }
    }
}
