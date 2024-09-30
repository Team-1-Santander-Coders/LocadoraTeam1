package main.java.com.team1;


import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.entities.User;
import main.java.com.team1.server.MainServer;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.util.FileUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        try {

        } catch (Exception e) {
            FileUtil.logError(e);
        }


        try {
            MainServer.startServer();
        } catch (IOException e) {
            FileUtil.logError(e);
        }
    }
}
