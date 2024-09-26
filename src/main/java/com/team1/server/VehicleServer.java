package main.java.com.team1.server;

import main.java.com.team1.dto.CarDTO;
import main.java.com.team1.dto.MotorcycleDTO;
import main.java.com.team1.dto.TruckDTO;
import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.entities.Car;
import main.java.com.team1.entities.Motorcycle;
import main.java.com.team1.entities.Truck;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.service.VehicleService;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.List;

import main.java.com.team1.server.MainServer.StaticFileHandler;

public class VehicleServer {
    private static VehicleService vehicleService;

    private static final String page = "adminPage";

    public static void createContexts() throws IOException {
        HttpServer server = MainServer.getServer();
        vehicleService = new VehicleService(new VehicleRepositoryImpl());
        server.createContext("/adminPage", new StaticFileHandler(page,"index.html"));
        server.createContext("/adminPage/script.js", new StaticFileHandler(page,"script.js"));
        server.createContext("/vehicles", new VehicleListHandler());
        server.createContext("/vehicle", new VehicleCreateHandler());
        server.createContext("/vehicle/edit", new VehicleEditHandler());
        server.createContext("/vehicle/delete", new VehicleDeleteHandler());
    }

    static class VehicleListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            String response = vehicles.stream()
                    .map(vehicle -> vehicle.getTipo() + " - " + vehicle.getPlaca() + " - " + vehicle.getModelo() + " - " + vehicle.getAno() +
                            " - " + (vehicle.isDisponivel() ? "Disponível" : "Indisponível"))
                    .collect(Collectors.joining("\n"));
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class VehicleCreateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                String[] vehicleData = body.split(",");
                String tipo = vehicleData[0];
                String placa = vehicleData[1];
                String modelo = vehicleData[2];
                String marca = vehicleData[3];
                int ano = Integer.parseInt(vehicleData[4]);

                try {
                    if ("Carro".equals(tipo)) {
                        Car car = new Car(placa, modelo, marca, ano);
                        CarDTO carDTO = new CarDTO(car.getPlaca(), car.getModelo(), car.getMarca(), car.getAno(), car.isDisponivel(), car.getPrecoDiaria());
                        vehicleService.addVehicle(carDTO);
                    } else if ("Moto".equals(tipo)) {
                        Motorcycle motorcycle = new Motorcycle(placa, modelo, marca, ano);
                        MotorcycleDTO motorcycleDTO = new MotorcycleDTO(motorcycle.getPlaca(), motorcycle.getModelo(), motorcycle.getMarca(), motorcycle.getAno(), motorcycle.isDisponivel(), motorcycle.getPrecoDiaria());
                        vehicleService.addVehicle(motorcycleDTO);
                    } else if ("Caminhão".equals(tipo)) {
                        Truck truck = new Truck(placa, modelo, marca, ano);
                        TruckDTO truckDTO = new TruckDTO(truck.getPlaca(), truck.getModelo(), truck.getMarca(), truck.getAno(), truck.isDisponivel(), truck.getPrecoDiaria());
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
            }
        }
    }

    static class VehicleEditHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                String[] vehicleData = body.split(",");
                String placa = vehicleData[0];
                String modelo = vehicleData[1];
                String marca = vehicleData[2];
                int ano = Integer.parseInt(vehicleData[3]);
                boolean disponivel = Boolean.parseBoolean(vehicleData[4]);
                double precoDiaria = Double.parseDouble(vehicleData[5]);

                try {
                    VehicleDTO updatedVehicle = new CarDTO(placa, modelo, marca, ano, disponivel, precoDiaria);
                    vehicleService.updateVehicle(updatedVehicle);
                    String response = "Veículo editado com sucesso!";
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
            }
        }
    }
}
