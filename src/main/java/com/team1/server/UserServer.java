package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.entities.User;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.UserService;
import main.java.com.team1.server.MainServer.StaticFileHandler;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UserServer {
    private static CustomerService customerService;
    private static final String page = "user";

    public static void createContexts() throws IOException {
        HttpServer server = MainServer.getServer();
        customerService = new CustomerService();

        // Contextos para servir os arquivos estáticos e a criação de usuário
        server.createContext("/usuario", new StaticFileHandler(page, "index.html")); // Serve o arquivo HTML principal
        server.createContext("/usuario/script.js", new StaticFileHandler(page, "script.js"));
        server.createContext("/user", new UserCreateHandler());
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
}
