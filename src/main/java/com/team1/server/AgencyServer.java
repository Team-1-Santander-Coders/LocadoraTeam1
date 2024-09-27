package main.java.com.team1.server;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.service.AgencyService;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.util.FileUtil;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class AgencyServer {
    private static final AgencyService agencyService = new AgencyService();
    private static final CustomerService customerService = new CustomerService();

    public static void createContexts() throws IOException {
        HttpServer server = MainServer.getServer();
        server.createContext("/agencies", new AgencyListHandler());
        server.createContext("/agency", new AgencyCreateHandler());
        server.createContext("/agency/delete", new AgencyDeleteHandler());
        server.createContext("/agency/edit", new AgencyEditHandler());
    }

    static class AgencyListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<AgencyDTO> agencies = agencyService.getAllAgencies();

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("[");

            for (int i = 0; i < agencies.size(); i++) {
                AgencyDTO agency = agencies.get(i);
                jsonResponse.append("{");
                jsonResponse.append("\"name\": \"").append(agency.name()).append("\", ");
                jsonResponse.append("\"address\": \"").append(agency.address()).append("\"");
                jsonResponse.append("}");
                if (i < agencies.size() - 1) {
                    jsonResponse.append(", ");
                }
            }

            jsonResponse.append("]");

            String response = jsonResponse.toString();
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class AgencyCreateHandler implements HttpHandler {
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

                    if (user != null && user.isAdmin()) {
                        String[] agencyData = body.split(" / ");
                        String name = agencyData[0];
                        String address = agencyData[1];

                        try {
                            AgencyDTO agencyDTO = new AgencyDTO(name, address);
                            agencyService.addAgency(agencyDTO);
                            String response = "Agência cadastrada com sucesso!";
                            exchange.sendResponseHeaders(200, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (DuplicateEntityException e) {
                            String response = "Erro: Agência já existe.";
                            exchange.sendResponseHeaders(409, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        }
                    } else {
                        String response = "Usuário não autorizado ou não encontrado.";
                        exchange.sendResponseHeaders(403, response.length());
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

    static class AgencyEditHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                String[] agencyData = body.split(" / ");
                System.out.println(Arrays.toString(agencyData));

                if (agencyData.length != 4) {
                    sendResponse(exchange, 400, "Dados inválidos. Esperado: nomeAntigo,enderecoAntigo,nomeNovo,enderecoNovo.");
                    return;
                }

                String oldName = agencyData[0].trim();
                String oldAddress = agencyData[1].trim();
                String newName = agencyData[2].trim();
                String newAddress = agencyData[3].trim();

                System.out.println(oldName);
                System.out.println(oldAddress);
                System.out.println(newName);
                System.out.println(newAddress);

                HttpCookie userIdCookie = getUserIdCookie(exchange);

                if (userIdCookie != null) {
                    String userId = userIdCookie.getValue();
                    UserDTO user = customerService.findUserByID(userId);

                    if (user != null && user.isAdmin()) {
                        try {
                            agencyService.updateAgency(agencyService.getAgencyByNameAndAddress(oldName, oldAddress), newName, newAddress);
                            System.out.println("Atualizado");
                            sendResponse(exchange, 200, "Agência editada com sucesso!");
                        } catch (EntityNotFoundException e) {
                            sendResponse(exchange, 404, "Agência não encontrada.");
                        }
                    } else {
                        sendResponse(exchange, 403, "Usuário não autorizado ou não encontrado.");
                    }
                } else {
                    sendResponse(exchange, 401, "Usuário não autenticado.");
                }
            } else {
                sendResponse(exchange, 405, "Método não permitido. Use PUT.");
            }
        }

        private HttpCookie getUserIdCookie(HttpExchange exchange) {
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            if (cookieHeader != null) {
                for (String cookie : cookieHeader.split("; ")) {
                    if (cookie.startsWith("userId=")) {
                        return HttpCookie.parse(cookie).getFirst();
                    }
                }
            }
            return null;
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    static class AgencyDeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                String[] agencyData = body.trim().split(",");
                String name = agencyData[0];
                String address = agencyData[1];

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

                    if (user != null && user.isAdmin()) {
                        try {
                            agencyService.deleteAgency(name, address);
                            String response = "Agência deletada com sucesso!";
                            exchange.sendResponseHeaders(200, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (EntityNotFoundException e) {
                            String response = "Erro: Agência não encontrada.";
                            exchange.sendResponseHeaders(404, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        }
                    } else {
                        String response = "Usuário não autorizado ou não encontrado.";
                        exchange.sendResponseHeaders(403, response.length());
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
