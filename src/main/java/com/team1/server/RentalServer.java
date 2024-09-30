package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.com.team1.dto.*;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.exception.RentIllegalUpdateException;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.service.AgencyService;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.RentalService;
import main.java.com.team1.service.VehicleService;
import main.java.com.team1.util.DateUtil;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe RentalServer responsável por definir os manipuladores de requisição HTTP para gerenciamento de aluguéis.
 * Fornece endpoints para listagem de aluguéis e criação/devolução de aluguéis.
 */
public class RentalServer {
    private static final RentalService rentalService = new RentalService();
    private static final CustomerService customerService = new CustomerService();
    private static final AgencyService agencyService = new AgencyService();
    private static final VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());

    /**
     * Cria os contextos (endpoints) do servidor HTTP relacionados ao aluguel.
     * Define os endpoints para listar aluguéis e realizar ações de aluguel.
     */
    public static void createContexts() {
        HttpServer server = MainServer.getServer();
        server.createContext("/rentals", new RentalListHandler());
        server.createContext("/rent", new RentHandler());
    }

    /**
     * Manipulador de requisição para listar os aluguéis.
     * Permite que usuários administradores visualizem todos os aluguéis e usuários normais vejam apenas os seus próprios.
     * Método aceito: GET.
     */
    static class RentalListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<RentalDTO> rentals = rentalService.getAllRentals();

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
                        List<RentalDTO> filteredRentals;
                        if (user.isAdmin()) {
                            filteredRentals = rentals;
                        } else {
                            UserDTO finalUser = user;
                            filteredRentals = rentals.stream()
                                    .filter(rental -> rental.getCustomer().equals(finalUser))
                                    .collect(Collectors.toList());
                        }

                        String response = "[" + filteredRentals.stream()
                                .map(RentalDTO::toJson)
                                .collect(Collectors.joining(",\n")) + "]";

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(StandardCharsets.UTF_8));
                        }
                    } else {
                        sendErrorResponse(exchange, 404, "Usuário não encontrado.");
                    }
                } else {
                    sendErrorResponse(exchange, 401, "Usuário não autenticado.");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }

        /**
         * Envia uma resposta de erro em formato JSON.
         *
         * @param exchange   Instância de HttpExchange para enviar a resposta.
         * @param statusCode Código de status HTTP.
         * @param message    Mensagem de erro a ser enviada.
         */
        private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = "{\"error\": \"" + message + "\"}";
            exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * Manipulador de requisição para criar ou devolver um aluguel.
     * Permite alugar um veículo ou registrar a devolução de um aluguel.
     * Método aceito: POST.
     */
    static class RentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                String[] requestData = body.split(" / ");

                String vehiclePlate = requestData[0];
                String agencyRentalName = requestData[1].replace("{", "");;
                String agencyRentalAddress = requestData[2].replace("}", "");;
                LocalDate rentalDate = DateUtil.converterTextoParaData(requestData[3]);
                String agencyReturnName = requestData[4];
                String agencyReturnAddress = requestData[5];
                String returnDateStr = requestData[6];
                String customerDocument = requestData[7];

                LocalDate returnDate = null;
                if (!returnDateStr.equals("Sem data de devolução")) {
                    returnDate = DateUtil.converterTextoParaData(returnDateStr);
                }

                AgencyDTO agencyRental = agencyService.getAgencyByNameAndAddress(agencyRentalName, agencyRentalAddress);
                AgencyDTO agencyReturn = null;
                if (!agencyReturnName.equals("Sem agência de devolução")) {
                    agencyReturn = agencyService.getAgencyByNameAndAddress(agencyReturnName, agencyReturnAddress);
                }

                UserDTO user = getUserFromCookie(exchange);

                if (user != null) {
                    VehicleDTO vehicle = getVehicle(vehiclePlate);
                    CustomerDTO customer = getCustomer(user, customerDocument);

                    if (customer != null && vehicle != null && agencyRental != null) {
                        try {
                            String response;
                            if (agencyReturn != null && returnDate != null) {
                                RentalDTO rental = rentalService.getRental(vehiclePlate, customer.getDocument(), rentalDate);
                                if (rental == null) {
                                    throw new EntityNotFoundException("Aluguel não encontrado");
                                }
                                if (rental.getReturnDate() != null) {
                                    throw new RentIllegalUpdateException("Este aluguel já foi devolvido");
                                }
                                response = rentalService.returnRental(rental, agencyReturn, returnDate);
                                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                            } else {
                                RentalDTO rental = new RentalDTO(vehicle, customer, agencyRental, rentalDate);
                                rentalService.addRental(rental);
                                response = rental.toJson();
                                exchange.sendResponseHeaders(201, response.getBytes(StandardCharsets.UTF_8).length);
                            }

                            sendResponse(exchange, response);
                        } catch (Exception e) {
                            FileUtil.logError(e);
                            sendErrorResponse(exchange, 409, "Erro: " + e.getMessage());
                        }
                    } else {
                        sendErrorResponse(exchange, 400, "Erro: Dados inválidos.");
                    }
                } else {
                    sendErrorResponse(exchange, 401, "Usuário não autenticado.");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }

        /**
         * Obtém o usuário autenticado a partir do cookie.
         *
         * @param exchange Instância de HttpExchange que contém os cookies.
         * @return O usuário autenticado ou null se não estiver autenticado.
         */
        private UserDTO getUserFromCookie(HttpExchange exchange) {
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            if (cookieHeader != null) {
                for (String cookie : cookieHeader.split("; ")) {
                    if (cookie.startsWith("userId=")) {
                        String userId = HttpCookie.parse(cookie).getFirst().getValue();
                        try {
                            return customerService.findUserByID(userId);
                        } catch (Exception e) {
                            System.out.println("Erro ao encontrar usuário: " + e.getMessage());
                            FileUtil.logError(e);
                        }
                    }
                }
            }
            return null;
        }

        /**
         * Obtém um veículo pelo número da placa.
         *
         * @param vehiclePlate Número da placa do veículo.
         * @return O veículo correspondente ou null se não encontrado.
         */
        private VehicleDTO getVehicle(String vehiclePlate) {
            try {
                return vehicleService.getVehicleByPlaca(vehiclePlate);
            } catch (EntityNotFoundException e) {
                FileUtil.logError(e);
                return null;
            }
        }

        /**
         * Obtém o cliente com base no usuário e no documento informado.
         *
         * @param user             O usuário autenticado.
         * @param customerDocument Documento do cliente a ser buscado.
         * @return O cliente correspondente ou null se não encontrado.
         */
        private CustomerDTO getCustomer(UserDTO user, String customerDocument) {
            return user.isAdmin() ? customerService.findCustomerByDocument(customerDocument) : user;
        }

        /**
         * Envia uma resposta com o conteúdo fornecido.
         *
         * @param exchange Instância de HttpExchange para enviar a resposta.
         * @param response Resposta a ser enviada.
         */
        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }

        /**
         * Envia uma resposta de erro em formato JSON.
         *
         * @param exchange   Instância de HttpExchange para enviar a resposta.
         * @param statusCode Código de status HTTP.
         * @param message    Mensagem de erro a ser enviada.
         */
        private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
            exchange.sendResponseHeaders(statusCode, message.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(message.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
