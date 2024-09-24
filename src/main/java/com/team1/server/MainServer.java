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
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new StaticFileHandler(page,"index.html"));
        server.createContext("/style.css", new StaticFileHandler(page, "style.css"));
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

    static class StaticFileHandler implements HttpHandler {
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
            if (page.equals("home") && fileName.contains(".css")) {
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
}
