package main.java.com.team1.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A classe MainServer é responsável por iniciar e configurar o servidor HTTP
 * principal da aplicação de locação de veículos. Ela cria os contextos das
 * rotas iniciais, além de iniciar contextos de servidores secundários como
 * AdminServer, VehicleServer, RentalServer, UserServer e AgencyServer.
 *
 * <p>O servidor escuta na porta 8000 e serve arquivos estáticos como HTML, CSS
 * e JavaScript, além de configurar rotas específicas para funcionalidades
 * relacionadas a usuários, veículos, agências e aluguéis.</p>
 */
public class MainServer {
    private static HttpServer server;
    private static final String page = "home";

    /**
     * Inicializa o servidor HTTP na porta 8000 e define os contextos de arquivos
     * estáticos e os contextos administrativos do sistema.
     *
     * <p>Além das rotas principais, como a página inicial e o favicon, são
     * configurados também contextos de servidores auxiliares que gerenciam
     * diferentes funcionalidades da aplicação.</p>
     *
     * @throws IOException se ocorrer algum erro ao criar o servidor.
     */
    public static void startServer() throws IOException {
        int port = 8000;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticFileHandler(page,"index.html"));
        server.createContext("/style.css", new StaticFileHandler(page, "style.css"));
        server.createContext("/script.js", new StaticFileHandler(page, "script.js"));
        server.createContext("/favicon.ico", new StaticFileHandler(page, "favicon.ico"));

        AdminServer.createContexts();
        VehicleServer.createContexts();
        RentalServer.createContexts();
        UserServer.createContexts();
        AgencyServer.createContexts();
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado http://localhost:" + port);
    }

    /**
     * Retorna a instância do servidor HTTP atualmente em execução.
     *
     * @return a instância do servidor HTTP.
     */
    public static HttpServer getServer() {
        return server;
    }

    /**
     * A classe StaticFileHandler é responsável por lidar com as requisições HTTP
     * de arquivos estáticos como HTML, CSS e JavaScript. Ela busca os arquivos
     * no diretório apropriado e envia a resposta ao cliente com o conteúdo e o
     * tipo MIME correto.
     */
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

