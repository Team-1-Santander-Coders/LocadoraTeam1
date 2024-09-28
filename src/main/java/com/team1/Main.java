package main.java.com.team1;
/*
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

        CustomerService customerService = new CustomerService();
        System.out.println(customerService.getAll());
        try {
            MainServer.startServer();
        } catch (IOException e) {
            FileUtil.logError(e);
        }
    }
}
*/

import main.java.com.team1.dto.*;
import main.java.com.team1.entities.Rental;
import main.java.com.team1.entities.User;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.RentalService;
import main.java.com.team1.service.UserService;
import main.java.com.team1.service.VehicleService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());
        CustomerService customerService = new CustomerService();
        RentalService rentalService = new RentalService();


        try {
//            User user1 = new User("Joao", "Rua A", "1234","j@mail.com", "21321456987", "123456", "Fisica");
//            userService.addUser(user1);
//            System.out.println(user1 + " adicionado!");
//            User user2 = new User("Joaquim", "Rua A", "1234","j@mail.com", "21321456987", "123456", "Fisica");
//            userService.addUser(user2);
//            System.out.println(user2 + " adicionado!");
//            User user3 = new User("Joao MEI", "Rua A", "1234","CNPJ", "j@mail.com", "123456", "Juridica");
//            userService.addUser(user3);
//            System.out.println(user3 + "adicionado!");
            User user6 = User.createUser("Joaquim MEI", "Rua A", "12341234","a@jkhgggmail.com", "21965427435", "15464848111111", "Juridica");

            System.out.println(user6 + "adicionado!");

//            User userDuplicate = new User("Joao", "Rua A", "1234","RG", "j@mail.com", "123456", "Fisica");
//            userService.addUser(userDuplicate);

            customerService.getAll();
        } catch (Exception e) {
            System.out.println("Erro ao adicionar usuário: " + e.getMessage());
        }


        try {

            vehicleService.addVehicle(new CarDTO("ABC1234", "Modelo A", "Marca A", 2020, true, 150.00));
            vehicleService.addVehicle(new CarDTO("XXX1234", "Modelo A", "Marca A", 2020, true, 150.00));
            vehicleService.addVehicle(new MotorcycleDTO("XYZ5678", "Titan", "Honda", 2020, true, 50.0));
            vehicleService.addVehicle(new TruckDTO("LMN9101", "F4000", "Ford", 2018, true, 200.0));

            vehicleService.addVehicle(new MotorcycleDTO("XYZ5678", "Titan", "Honda", 2020, true, 50.0));
        } catch (Exception e) {
            System.out.println("Erro ao adicionar veículo: " + e.getMessage());
        }

        try {
            vehicleService.deleteVehicle("XXX1234");

            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            System.out.println("Veículos após remoção: " + vehicles);
        } catch (Exception e) {
            System.out.println("Erro ao remover veículo: " + e.getMessage());
        }

        try {
            List<VehicleDTO> availableVehicles = vehicleService.getAllAvailableVehicles();

            System.out.println("Veículos disponíveis:");
            for (VehicleDTO vehicle : availableVehicles) {
                System.out.println(vehicle);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar veículos disponíveis: " + e.getMessage());
        }

        try {
            List<VehicleDTO> allVehicles = vehicleService.getAllVehicles();

            System.out.println("Todos os veículos: ");
            for (VehicleDTO vehicle : allVehicles) {
                System.out.println(vehicle);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar todos os veículos: " + e.getMessage());
        }

       //RentalDTO rentalDTO = new RentalDTO("XXX1234", "João", "RJ", 27-09-24);


        try {

            List<RentalDTO> rentals = rentalService.getRentalByUsers("Joao");

            System.out.println("Alugueis do usuário: " + rentals);
        } catch (Exception e) {
            System.out.println("Erro ao listar alugueis do usuário: " + e.getMessage());
        }

    }
}
