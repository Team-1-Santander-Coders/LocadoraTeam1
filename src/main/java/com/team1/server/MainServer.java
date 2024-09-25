package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.entities.User;
import main.java.com.team1.util.FileUtil;
import main.java.com.team1.util.UserAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MainServer {
    private static HttpServer server;
    private static final String page = "home";

    public static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new StaticFileHandler(page,"index.html"));
        server.createContext("/style.css", new StaticFileHandler(page, "style.css"));
        server.createContext("/script.js", new StaticFileHandler(page, "script.js"));
        server.createContext("/userPage", new StaticFileHandler("userPage", "index.html"));
        server.createContext("/userPage/script.js", new StaticFileHandler("userPage", "script.js"));
        server.createContext("/auth", new UserAuthHandler());


        VehicleServer.createContexts();
        UserServer.createContexts();
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado http://localhost:8000");
        System.out.println("User: http://localhost:8000/usuario");
        System.out.println("Vehicle: http://localhost:8000/veiculo");
    }

    public static HttpServer getServer() {
        return server;
    }

    public static class StaticFileHandler implements HttpHandler {
        private final String page;
        private final String fileName;

        public StaticFileHandler(String page, String fileName) {
            this.page = page;
            this.fileName = fileName;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Constrói o caminho para o arquivo estático (HTML, CSS ou JS).
            String path = Paths.get("src", "resources", "static", page, fileName).toString();

            if (page.equals("home")) {
                path = Paths.get("src", "resources", "static", fileName).toString();
            }
            byte[] response = Files.readAllBytes(Paths.get(path)); // Lê o conteúdo do arquivo.
            // Define o tipo de conteúdo baseado no arquivo (.css, .js, .html).
            exchange.getResponseHeaders().add("Content-Type", fileName.endsWith(".css") ? "text/css" :
                    fileName.endsWith(".js") ? "application/javascript" : "text/html");
            // Envia a resposta com o conteúdo do arquivo.
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    static class UserAuthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                body = body.replace("{", "").replace("}", "");
                String[] loginData = body.split(",");

                String email = loginData[0].split(":")[1].replace("\"", "").trim();
                String password = loginData[1].split(":")[1].replace("\"", "").trim();

                try{
                    UserDTO user = UserAuth.AuthLogin(email.trim(), password.trim());
                    assert user != null;
                    String response = "{" +
                                        "\"name\":\"" + user.getName() + "\"," +
                                        "\"email\":\"" + user.getEmail() + "\"," +
                                        "\"document\":\"" + user.getDocument() + "\"" +
                                        "}";

                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();
                } catch (Exception e) {
                    String response = "Usuário ou senha incorretos " + (e.getMessage() != null ? e.getMessage() : "Erro desconhecido.");
                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                    FileUtil.logError(e);
                    exchange.sendResponseHeaders(409, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();
                }
            }
        }
    }
}
