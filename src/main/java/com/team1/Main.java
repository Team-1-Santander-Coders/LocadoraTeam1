package main.java.com.team1;

import main.java.com.team1.server.MainServer;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        try {
            MainServer.startServer();
        } catch (IOException e) {
            FileUtil.logError(e);
        }
    }
}
