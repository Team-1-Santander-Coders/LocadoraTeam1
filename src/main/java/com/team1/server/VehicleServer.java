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
    private static VehicleService vehicleService; // Serviço que manipula as operações relacionadas aos veículos.

    private static final String page = "vehicle";

    public static void createContexts() throws IOException {
        HttpServer server = MainServer.getServer();
        vehicleService = new VehicleService(new VehicleRepositoryImpl()); // Instancia o serviço de veículos com um repositório.
        // Criação de contextos (rotas). Cada rota aponta para um manipulador (handler).
        server.createContext("/veiculos", new StaticFileHandler(page,"index.html")); // Serve o arquivo HTML principal.
        server.createContext("/veiculos/script.js", new StaticFileHandler(page,"script.js")); // Serve o JavaScript.
        server.createContext("/vehicles", new VehicleListHandler()); // Rota GET para listar todos os veículos.
        server.createContext("/vehicle", new VehicleCreateHandler()); // Rota POST para criar um veículo.
        server.createContext("/vehicle/edit", new VehicleEditHandler()); // Rota PUT para editar um veículo.
        server.createContext("/vehicle/delete", new VehicleDeleteHandler()); // Rota DELETE para excluir um veículo.
    }

    // Manipulador para servir arquivos estáticos (HTML, CSS, JS)


    // Manipulador para listar os veículos (GET /vehicles)
    static class VehicleListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles(); // Obtém todos os veículos do serviço.
            // Formata a resposta com as informações de cada veículo.
            String response = vehicles.stream()
                    .map(vehicle -> vehicle.getTipo() + " - " + vehicle.getPlaca() + " - " + vehicle.getModelo() + " - " + vehicle.getAno() +
                            " - " + (vehicle.isDisponivel() ? "Disponível" : "Indisponível"))
                    .collect(Collectors.joining("\n"));
            // Envia a resposta de volta ao cliente.
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    // Manipulador para criar um veículo (POST /vehicle)
    static class VehicleCreateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) { // Verifica se o método HTTP é POST.
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8); // Lê o corpo da requisição.

                // Separa os dados do veículo recebidos do frontend.
                String[] vehicleData = body.split(",");
                String tipo = vehicleData[0];
                String placa = vehicleData[1];
                String modelo = vehicleData[2];
                String marca = vehicleData[3];
                int ano = Integer.parseInt(vehicleData[4]);

                try {
                    // Cria diferentes tipos de veículos baseados no "tipo" enviado (Carro, Moto, Caminhão).
                    if ("Carro".equals(tipo)) {
                        Car car = new Car(placa, modelo, marca, ano);
                        CarDTO carDTO = new CarDTO(car.getPlaca(), car.getModelo(), car.getMarca(), car.getAno(), car.isDisponivel(), car.getPrecoDiaria());
                        vehicleService.addVehicle(carDTO); // Adiciona o carro ao repositório.
                    } else if ("Moto".equals(tipo)) {
                        Motorcycle motorcycle = new Motorcycle(placa, modelo, marca, ano);
                        MotorcycleDTO motorcycleDTO = new MotorcycleDTO(motorcycle.getPlaca(), motorcycle.getModelo(), motorcycle.getMarca(), motorcycle.getAno(), motorcycle.isDisponivel(), motorcycle.getPrecoDiaria());
                        vehicleService.addVehicle(motorcycleDTO);
                    } else if ("Caminhão".equals(tipo)) {
                        Truck truck = new Truck(placa, modelo, marca, ano);
                        TruckDTO truckDTO = new TruckDTO(truck.getPlaca(), truck.getModelo(), truck.getMarca(), truck.getAno(), truck.isDisponivel(), truck.getPrecoDiaria());
                        vehicleService.addVehicle(truckDTO);
                    }
                    // Envia uma resposta de sucesso.
                    String response = tipo + " cadastrado com sucesso!";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (DuplicateEntityException e) {
                    // Se a placa já existir, retorna erro 409 (Conflito).
                    String response = "Erro: Veículo com a placa já existe.";
                    exchange.sendResponseHeaders(409, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    // Manipulador para editar um veículo (PUT /vehicle/edit)
    static class VehicleEditHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equals(exchange.getRequestMethod())) { // Verifica se o método HTTP é PUT.
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8); // Lê o corpo da requisição.

                // Separa os dados do veículo a ser editado.
                String[] vehicleData = body.split(",");
                String placa = vehicleData[0];
                String modelo = vehicleData[1];
                String marca = vehicleData[2];
                int ano = Integer.parseInt(vehicleData[3]);
                boolean disponivel = Boolean.parseBoolean(vehicleData[4]);
                double precoDiaria = Double.parseDouble(vehicleData[5]);

                try {
                    // Atualiza os dados do veículo no serviço.
                    VehicleDTO updatedVehicle = new CarDTO(placa, modelo, marca, ano, disponivel, precoDiaria);
                    vehicleService.updateVehicle(updatedVehicle);
                    // Envia uma resposta de sucesso.
                    String response = "Veículo editado com sucesso!";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (EntityNotFoundException e) {
                    // Se o veículo não for encontrado, retorna erro 404.
                    String response = "Erro: Veículo não encontrado.";
                    exchange.sendResponseHeaders(404, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    // Manipulador para deletar um veículo (DELETE /vehicle/delete)
    static class VehicleDeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("DELETE".equals(exchange.getRequestMethod())) { // Verifica se o método HTTP é DELETE.
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8); // Lê o corpo da requisição.
                String placa = body.trim(); // A placa é enviada no corpo da requisição.

                try {
                    vehicleService.deleteVehicle(placa); // Deleta o veículo no serviço.
                    // Envia uma resposta de sucesso.
                    String response = "Veículo deletado com sucesso!";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (EntityNotFoundException e) {
                    // Se o veículo não for encontrado, retorna erro 404.
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
