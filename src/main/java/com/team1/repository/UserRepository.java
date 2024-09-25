package main.java.com.team1.repository;

import main.java.com.team1.entities.User;
import main.java.com.team1.util.FileUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Serializable {

    private static String USERS_FILE = "src/resources/data/users.dat";
    List<User> usersList = new ArrayList<>();

    public void addUser(User user) {
        usersList.add(user);
        FileUtil.writeToFile(usersList, USERS_FILE);
    }

    public List<User> findAll(){
        return FileUtil.readFromFile(USERS_FILE);
    }
}
