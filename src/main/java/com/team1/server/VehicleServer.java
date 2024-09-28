package main.java.com.team1.server;

import main.java.com.team1.dto.*;
import main.java.com.team1.entities.Car;
import main.java.com.team1.entities.Motorcycle;
import main.java.com.team1.entities.Truck;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.exception.RentIllegalUpdateException;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.service.AgencyService;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.VehicleService;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Stream;

import main.java.com.team1.util.FileUtil;

public class VehicleServer {
    private static VehicleService vehicleService;
    private static final CustomerService customerService = new CustomerService();
    private static final AgencyService agencyService = new AgencyService();

    public static void createContexts() throws IOException {
        vehicleService = new VehicleService(new VehicleRepositoryImpl());
        HttpServer server = MainServer.getServer();
        server.createContext("/vehicles", new VehicleListHandler());
        server.createContext("/vehicle", new VehicleCreateHandler());
        server.createContext("/vehicle/delete", new VehicleDeleteHandler());
    }

    static class VehicleListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            HttpCookie userIdCookie = null;
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            if (cookieHeader != null) {
                for (String cookie : cookieHeader.split("; ")) {
                    if (cookie.startsWith("userId=")) {
                        userIdCookie = HttpCookie.parse(cookie).getFirst();
                    }
                }
            }

            if (userIdCookie != null) {
                String userId = userIdCookie.getValue();
                UserDTO user = null;
                try {
                    user = customerService.findUserByID(userId);
                } catch (Exception e) {
                    System.out.println("Erro ao encontrar usuário: " + e.getMessage());
                    FileUtil.logError(e);
                }

                if (user != null) {
                    List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
                    Stream<VehicleDTO> vehicleStream = vehicles.stream();

                    if (!user.isAdmin()) {
                        vehicleStream = vehicleStream.filter(VehicleDTO::isDisponivel);
                    }

                    String response = vehicleStream
                            .map(vehicle -> vehicle.getTipo() + " - " + vehicle.getPlaca() + " - " + vehicle.getModelo() + " - " + vehicle.getAno() +
                                    " - " + (vehicle.isDisponivel() ? "Disponível" : "Indisponível") + " - {" + vehicle.getAgency().name() + " - " + vehicle.getAgency().address() + "}")
                            .collect(Collectors.joining("\n"));

                    exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.close();
                } else {
                    String response = "Usuário não encontrado.";
                    exchange.sendResponseHeaders(404, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                String response = "Usuário não autenticado.";
                exchange.sendResponseHeaders(401, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class VehicleCreateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                HttpCookie userIdCookie = null;
                String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");

                if (cookieHeader != null) {
                    for (String cookie : cookieHeader.split("; ")) {
                        if (cookie.startsWith("userId=")) {
                            userIdCookie = HttpCookie.parse(cookie).getFirst();
                        }
                    }
                }

                if (userIdCookie != null) {
                    String userId = userIdCookie.getValue();
                    UserDTO user = null;
                    try {
                        user = customerService.findUserByID(userId);
                    } catch (Exception e) {
                        System.out.println("Erro ao encontrar usuário: " + e.getMessage());
                        FileUtil.logError(e);
                    }

                    if (user != null) {
                        if (user.isAdmin()) {
                            String[] vehicleData = body.split(" / ");
                            String tipo = vehicleData[0];
                            String placa = vehicleData[1];
                            String modelo = vehicleData[2];
                            String marca = vehicleData[3];
                            int ano = Integer.parseInt(vehicleData[4]);
                            String agencyName = vehicleData[5];
                            String agencyAddress = vehicleData[6];

                            try {
                                AgencyDTO agencyDTO = agencyService.getAgencyByNameAndAddress(agencyName, agencyAddress);
                                if ("Carro".equals(tipo)) {
                                    Car car = new Car(placa, modelo, marca, ano, agencyDTO);
                                    CarDTO carDTO = new CarDTO(car.getPlaca(), car.getModelo(), car.getMarca(), car.getAno(), car.isDisponivel(), car.getPrecoDiaria(), car.getAgency());
                                    vehicleService.addVehicle(carDTO);
                                } else if ("Moto".equals(tipo)) {
                                    Motorcycle motorcycle = new Motorcycle(placa, modelo, marca, ano, agencyDTO);
                                    MotorcycleDTO motorcycleDTO = new MotorcycleDTO(motorcycle.getPlaca(), motorcycle.getModelo(), motorcycle.getMarca(), motorcycle.getAno(), motorcycle.isDisponivel(), motorcycle.getPrecoDiaria(), motorcycle.getAgency());
                                    vehicleService.addVehicle(motorcycleDTO);
                                } else if ("Caminhão".equals(tipo)) {
                                    Truck truck = new Truck(placa, modelo, marca, ano, agencyDTO);
                                    TruckDTO truckDTO = new TruckDTO(truck.getPlaca(), truck.getModelo(), truck.getMarca(), truck.getAno(), truck.isDisponivel(), truck.getPrecoDiaria(), truck.getAgency());
                                    vehicleService.addVehicle(truckDTO);
                                }
                                String response = tipo + " cadastrado com sucesso!";
                                exchange.sendResponseHeaders(200, response.length());
                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes());
                                os.close();
                            } catch (DuplicateEntityException e) {
                                String response = "Erro: Veículo com a placa já existe.";
                                exchange.sendResponseHeaders(409, response.length());
                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes());
                                os.close();
                            }
                        } else {
                            String response = "Usuário não é administrador.";
                            exchange.sendResponseHeaders(403, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        }
                    } else {
                        String response = "Usuário não encontrado.";
                        exchange.sendResponseHeaders(404, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } else {
                    String response = "Usuário não autenticado.";
                    exchange.sendResponseHeaders(401, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class VehicleDeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                String placa = body.trim();

                HttpCookie userIdCookie = null;
                String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");

                if (cookieHeader != null) {
                    for (String cookie : cookieHeader.split("; ")) {
                        if (cookie.startsWith("userId=")) {
                            userIdCookie = HttpCookie.parse(cookie).getFirst();
                        }
                    }
                }

                if (userIdCookie != null) {
                    String userId = userIdCookie.getValue();

                    UserDTO user = null;
                    try {
                        user = customerService.findUserByID(userId);
                    } catch (Exception e) {
                        System.out.println("Erro ao encontrar usuário: " + e.getMessage());
                        FileUtil.logError(e);
                    }

                    if (user != null) {
                        if (user.isAdmin()) {
                            try {
                                vehicleService.deleteVehicle(placa);
                                String response = "Veículo deletado com sucesso!";
                                exchange.sendResponseHeaders(200, response.length());
                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes());
                                os.close();
                            } catch (EntityNotFoundException e) {
                                String response = "Erro: Veículo não encontrado.";
                                exchange.sendResponseHeaders(404, response.length());
                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes());
                                os.close();
                            }
                        } else {
                            String response = "Usuário não é administrador.";
                            exchange.sendResponseHeaders(403, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        }
                    } else {
                        String response = "Usuário não encontrado.";
                        exchange.sendResponseHeaders(404, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } else {
                    String response = "Usuário não autenticado.";
                    exchange.sendResponseHeaders(401, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
