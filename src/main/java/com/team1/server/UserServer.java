package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.com.team1.entities.User;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.UserService;
import main.java.com.team1.server.MainServer.StaticFileHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UserServer {
    private static CustomerService customerService;
    private static final String page = "user";

    public static void createContexts() throws IOException {
        HttpServer server = MainServer.getServer();
        customerService = new CustomerService();

        server.createContext("/usuario", new StaticFileHandler(page, "index.html")); // Serve o arquivo HTML principal.
        server.createContext("/usuario/script.js", new StaticFileHandler(page, "script.js"));
        server.createContext("/user", new UserCreateHandler());
    }

        static class UserCreateHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if("POST".equals(exchange.getRequestMethod())) {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    String[] userData = body.split(",");
                    String name = userData[0];
                    String address = userData[1];
                    String password = userData[2];
                    String email = userData[3];
                    String phone = userData[4];
                    String documento = userData[5];
                    String tipo = userData[6];


                    try{
                        UserService userService = new UserService();
                        User user = User.createUser(name, address, password, email, phone, documento.trim(), tipo);

                        userService.addUser(user);
                        String response = tipo + " cadastrado com sucesso!";
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();

                    } catch(Exception e){
                        String response = e.getCause().toString();
                        exchange.sendResponseHeaders(409, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }

                }
            }
        }

    }
