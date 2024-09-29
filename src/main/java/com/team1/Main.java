package main.java.com.team1;

import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.server.MainServer;
import main.java.com.team1.service.RentalService;
import main.java.com.team1.util.DateUtil;
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
