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

public class RentalServer {
    private static final RentalService rentalService = new RentalService();
    private static final CustomerService customerService = new CustomerService();
    private static final AgencyService agencyService = new AgencyService();
    private static final VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());

    public static void createContexts() {
        HttpServer server = MainServer.getServer();
        server.createContext("/rentals", new RentalListHandler());
        server.createContext("/rent", new RentHandler());
    }

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
                        if (user.isAdmin()) {
                            String response = rentals.stream()
                                    .map(RentalDTO::toJson)
                                    .collect(Collectors.joining("\n"));
                            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes(StandardCharsets.UTF_8));
                            os.close();
                        } else {
                            UserDTO finalUser = user;
                            String response = rentals.stream()
                                    .filter(RentalDTO -> RentalDTO.getCustomer().equals(finalUser))
                                    .map(RentalDTO::toJson)
                                    .collect(Collectors.joining("\n"));
                            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes(StandardCharsets.UTF_8));
                            os.close();
                        }
                    } else {
                        String response = "Usuário não encontrado.";
                        exchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);
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

    static class RentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(body);
                String[] requestData = body.split(" / ");
                String vehiclePlate = requestData[0];
                String agencyRentalName = requestData[1];
                String agencyRentalAddress = requestData[2];
                LocalDate rentalDate = DateUtil.converterTextoParaData(requestData[3]);
                String agencyReturnName = requestData[4];
                String agencyReturnAddress = requestData[5];
                LocalDate returnDate = null;
                if (!requestData[6].equals("Sem data de devolução")) {
                    returnDate = DateUtil.converterTextoParaData(requestData[6]);
                }
                AgencyDTO agencyReturn = null;
                if (!agencyReturnName.equals("Sem agência de devolução")) {
                    agencyService.getAgencyByNameAndAddress(agencyReturnName, agencyReturnAddress);
                }

                AgencyDTO agencyRental = agencyService.getAgencyByNameAndAddress(agencyRentalName, agencyRentalAddress);

                String customerDocument = requestData[7];

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
                        VehicleDTO vehicle = null;
                        try {
                            vehicle = vehicleService.getVehicleByPlaca(vehiclePlate);
                        } catch (EntityNotFoundException e) {
                            FileUtil.logError(e);
                        }
                        CustomerDTO customer = null;

                        if (user.isAdmin()) {
                            customer = customerService.findCustomerByDocument(customerDocument);
                        } else {
                            customer = user;
                        }


                        if (customer != null && vehicle != null && agencyRental != null) {
                            RentalDTO rental;
                            String response = "";
                            if (!agencyReturnName.equals("Sem agência de devolução")) {
                                rental = rentalService.getRental(vehiclePlate, customer.getDocument(), rentalDate);
                                try {
                                    rentalService.returnRental(rental, agencyReturn, returnDate);
                                    response = rental.toJson();
                                    exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                                    OutputStream os = exchange.getResponseBody();
                                    os.write(response.getBytes(StandardCharsets.UTF_8));
                                    os.close();
                                } catch (Exception e) {
                                    response = "Erro: " + e.getMessage();
                                    exchange.sendResponseHeaders(409, response.length());
                                    OutputStream os = exchange.getResponseBody();
                                    os.write(response.getBytes());
                                    os.close();
                                }

                            } else {
                                rental = new RentalDTO(vehicle, customer, agencyRental, rentalDate);
                                try {
                                    rentalService.addRental(rental);
                                } catch (Exception e) {
                                    response = "Erro: " + e.getMessage();
                                    exchange.sendResponseHeaders(409, response.length());
                                    OutputStream os = exchange.getResponseBody();
                                    os.write(response.getBytes());
                                    os.close();
                                }
                                response = rental.toJson();
                                exchange.sendResponseHeaders(201, response.getBytes(StandardCharsets.UTF_8).length);
                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes(StandardCharsets.UTF_8));
                                os.close();
                            }

                        } else {
                            String response = "Erro: Dados inválidos.";
                            exchange.sendResponseHeaders(400, response.length());
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
