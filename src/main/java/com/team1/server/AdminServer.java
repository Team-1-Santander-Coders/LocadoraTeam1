package main.java.com.team1.server;

import main.java.com.team1.server.MainServer.StaticFileHandler;

import com.sun.net.httpserver.HttpServer;

/**
 * A classe AdminServer é responsável por criar os contextos do servidor HTTP
 * relacionados à interface administrativa do sistema.
 *
 * <p>Os contextos incluem páginas HTML e arquivos JavaScript que compõem a
 * interface de administração, como a página principal, veículos, agências e
 * aluguéis. Cada contexto é associado a um manipulador de arquivos estáticos
 * que serve os recursos correspondentes.</p>
 *
 */
public class AdminServer {
    private static final String page = "adminPage";
    /**
     * Cria os contextos no servidor HTTP, mapeando cada URL para o manipulador de
     * arquivos estáticos correspondente. Esses contextos servem as páginas HTML e
     * os scripts JavaScript da interface administrativa.
     * <p>Os contextos criados incluem:</p>
     * <ul>
     *     <li>/adminPage - Página principal do administrador</li>
     *     <li>/adminPage/script.js - Script JavaScript da página principal</li>
     *     <li>/adminPage/veiculos - Página para gerenciar veículos</li>
     *     <li>/adminPage/veiculo.js - Script JavaScript para gerenciamento de veículos</li>
     *     <li>/adminPage/agencias - Página para gerenciar agências</li>
     *     <li>/adminPage/agencia.js - Script JavaScript para gerenciamento de agências</li>
     *     <li>/adminPage/alugueis - Página para gerenciar aluguéis</li>
     *     <li>/adminPage/aluguel.js - Script JavaScript para gerenciamento de aluguéis</li>
     * </ul>
     */
    public static void createContexts() {
        HttpServer server = MainServer.getServer();
        server.createContext("/adminPage", new StaticFileHandler(page,"index.html"));
        server.createContext("/adminPage/script.js", new StaticFileHandler(page,"script.js"));
        server.createContext("/adminPage/veiculos", new StaticFileHandler(page,"veiculos.html"));
        server.createContext("/adminPage/veiculo.js", new StaticFileHandler(page,"veiculo.js"));
        server.createContext("/adminPage/agencias", new StaticFileHandler(page, "agencias.html"));
        server.createContext("/adminPage/agencia.js", new StaticFileHandler(page, "agencia.js"));
        server.createContext("/adminPage/alugueis", new StaticFileHandler(page, "alugueis.html"));
        server.createContext("/adminPage/aluguel.js", new StaticFileHandler(page, "aluguel.js"));
    }
}
