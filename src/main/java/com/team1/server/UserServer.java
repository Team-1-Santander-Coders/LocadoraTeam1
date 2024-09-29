package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.entities.User;
import main.java.com.team1.server.MainServer.StaticFileHandler;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.util.FileUtil;
import main.java.com.team1.util.UserAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserServer {
    private static CustomerService customerService;
    private static final String page = "user";

    public static void createContexts() throws IOException {
        HttpServer server = MainServer.getServer();
        customerService = new CustomerService();

        server.createContext("/usuario", new StaticFileHandler(page, "index.html"));
        server.createContext("/usuario/script.js", new StaticFileHandler(page, "script.js"));
        server.createContext("/user", new UserCreateHandler());
        server.createContext("/users", new UserListHandler());
        server.createContext("/userPage", new StaticFileHandler("userPage", "index.html"));
        server.createContext("/userPage/script.js", new StaticFileHandler("userPage", "script.js"));
        server.createContext("/checkAuth", new SessionValidationHandler());
        server.createContext("/auth", new UserAuthHandler());
    }

    static class UserAuthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                body = body.replace("{", "").replace("}", "");
                String[] loginData = body.split(",");


                String email = loginData[0].split(":")[1].replace("\"", "").trim();
                String password = loginData[1].split(":")[1].replace("\"", "").trim();

                UserDTO user = UserAuth.AuthLogin(email.trim(), password.trim());

                if (user != null) {
                    HttpCookie userIdCookie = new HttpCookie("userId", user.getId());

                    exchange.getResponseHeaders().add("Set-Cookie", userIdCookie.toString());
                    String response = "{\"success\": true, \"redirectUrl\": \"/userPage\"}";
                    if (user.isAdmin()) {
                        response = "{\"success\": true, \"redirectUrl\": \"/adminPage/\"}";
                    }
                    exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    String response = "{\"success\": false, \"message\": \"Credenciais inválidas\"}";
                    exchange.sendResponseHeaders(401, response.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class SessionValidationHandler implements HttpHandler {
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

                UserDTO user = customerService.findUserByID(userId);

                if (user != null) {
                    boolean isAdmin = user.isAdmin();
                    String userName = user.getName();

                    String jsonResponse = String.format("{\"name\": \"%s\", \"isAdmin\": %b}", userName, isAdmin);

                    exchange.getResponseHeaders().set("Content-Type", "application/json");

                    byte[] responseBytes = jsonResponse.getBytes();
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();
                } else {
                    exchange.sendResponseHeaders(302, -1);
                }
            } else {
                exchange.sendResponseHeaders(302, -1);
            }
        }
    }

    static class UserCreateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                body = body.replace("{", "").replace("}", "");
                String[] userData = body.split(",");
                String name = userData[0].split(":")[1].replace("\"", "").trim();
                String address = userData[1].split(":")[1].replace("\"", "").trim();
                String password = userData[2].split(":")[1].replace("\"", "").trim();
                String email = userData[3].split(":")[1].replace("\"", "").trim();
                String phone = userData[4].split(":")[1].replace("\"", "").trim();
                String documentValue = userData[5].split(":")[1].replace("\"", "").trim();
                String tipo = userData[6].split(":")[1].replace("\"", "").trim();

                try {
                    User user = User.createUser(name, address, password, email, phone, documentValue, tipo);
                    UserDTO userDTO = new UserDTO(user.getName(), user.getAddress(), user.getPhone(), user.getDocument(), user.getEmail(), user.getPassword(), user.getTipo());
                    customerService.addCustomer(userDTO);
                    String response = tipo + " cadastrado com sucesso!";
                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();

                } catch (Exception e) {
                    String response = "Erro ao cadastrar usuário: " + (e.getMessage() != null ? e.getMessage() : "Erro desconhecido.");
                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                    FileUtil.logError(e);
                    exchange.sendResponseHeaders(409, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }


    static class UserListHandler implements HttpHandler {
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
                if (user != null && user.isAdmin()) {
                    Stream<CustomerDTO> customerDTOStream = customerService.getAll().stream().filter(customer -> !customer.isAdmin());
                    String response = customerDTOStream
                            .map(customerDTO -> String.format(
                                    "{\"document\":\"%s\", \"name\":\"%s\", \"email\":\"%s\"}",
                                    customerDTO.getDocument(),
                                    customerDTO.getName(),
                                    customerDTO.getEmail()))
                            .collect(Collectors.joining(",\n", "[\n", "\n]"));
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.close();
                } else {
                    String response = "Usuário não autorizado.";
                    exchange.sendResponseHeaders(403, response.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                String response = "Usuário não autenticado.";
                exchange.sendResponseHeaders(401, response.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
