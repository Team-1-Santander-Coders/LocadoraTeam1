package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.com.team1.entities.User;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserServer {
    private static CustomerService customerService;

    public static void StartServer() throws IOException {
        customerService = new CustomerService();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new StaticFileHandler("index.html")); // Serve o arquivo HTML principal.
        server.createContext("/style.css", new StaticFileHandler("style.css")); // Serve o CSS.
        server.createContext("/script.js", new StaticFileHandler("script.js"));
        server.createContext("/user", new UserCreateHandler());

        server.setExecutor(null); // Define que o servidor usará o executor padrão (threading).
        server.start(); // Inicia o servidor.
        System.out.println("Servidor rodando http://localhost:8000");


    }

    static class StaticFileHandler implements HttpHandler {
        private String fileName;

        public StaticFileHandler(String fileName) {
            this.fileName = fileName;
        }
        // Serve o JavaScript.


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = Paths.get("src", "resources", "static", "User",fileName).toString();
            byte[] response = Files.readAllBytes(Paths.get(path));

            exchange.getResponseHeaders().add("Content-Type", fileName.endsWith(".css") ? "text/css" :
                    fileName.endsWith(".js") ? "application/javascript" : "text/html");

            exchange.sendResponseHeaders(200, response.length);

            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
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

