package main.java.com.team1.server;

import main.java.com.team1.server.MainServer.StaticFileHandler;

import com.sun.net.httpserver.HttpServer;

public class AdminServer {
    private static final String page = "adminPage";
    public static void createContexts() {
        HttpServer server = MainServer.getServer();

        server.createContext("/adminPage", new StaticFileHandler(page,"index.html"));
        server.createContext("/adminPage/veiculos", new StaticFileHandler(page,"veiculos.html"));
        server.createContext("/adminPage/script.js", new StaticFileHandler(page,"script.js"));
        server.createContext("/adminPage/agencias", new StaticFileHandler(page, "agencias.html"));
        server.createContext("/adminPage/agencia.js", new StaticFileHandler(page, "agencia.js"));
    }
}
