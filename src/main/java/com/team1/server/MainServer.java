package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MainServer {
    private static HttpServer server;
    private static final String page = "home";

    public static void startServer() throws IOException {
        int port = 8000;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticFileHandler(page,"index.html"));
        server.createContext("/style.css", new StaticFileHandler(page, "style.css"));
        server.createContext("/script.js", new StaticFileHandler(page, "script.js"));

        VehicleServer.createContexts();
        UserServer.createContexts();
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado http://localhost:" + port);
        System.out.println("User: http://localhost:" + port + "/usuario");
        System.out.println("Vehicle: http://localhost:" + port + "/veiculo");
        System.out.println("User Page: http://localhost:" + port + "/userPage");
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
            String path = Paths.get("src", "resources", "static", page, fileName).toString();

            if (page.equals("home")) {
                path = Paths.get("src", "resources", "static", fileName).toString();
            }
            byte[] response = Files.readAllBytes(Paths.get(path));
            exchange.getResponseHeaders().add("Content-Type", fileName.endsWith(".css") ? "text/css" :
                    fileName.endsWith(".js") ? "application/javascript" : "text/html");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}