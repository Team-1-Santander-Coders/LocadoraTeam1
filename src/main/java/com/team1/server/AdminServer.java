package main.java.com.team1.server;

import main.java.com.team1.server.MainServer.StaticFileHandler;

import com.sun.net.httpserver.HttpServer;

public class AdminServer {
    private static final String page = "adminPage";
    public static void createContexts() {
        HttpServer server = MainServer.getServer();

        server.createContext("/adminPage", new StaticFileHandler(page,"index.html"));
        server.createContext("/adminPage/veiculos", new StaticFileHandler(page,"veiculos.html"));
        server.createContext("/adminPage/veiculo.js", new StaticFileHandler(page,"veiculo.js"));
        server.createContext("/adminPage/agencias", new StaticFileHandler(page, "agencias.html"));
        server.createContext("/adminPage/agencia.js", new StaticFileHandler(page, "agencia.js"));
        server.createContext("/adminPage/alugueis", new StaticFileHandler(page, "alugueis.html"));
        server.createContext("/adminPage/aluguel.js", new StaticFileHandler(page, "aluguel.js"));
    }
}
