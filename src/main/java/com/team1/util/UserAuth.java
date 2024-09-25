package main.java.com.team1.util;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.entities.Customer;
import main.java.com.team1.entities.User;
import main.java.com.team1.service.CustomerService;

import java.util.List;

public class UserAuth {

    public static UserDTO AuthLogin(String email, String password){
        UserDTO user = findUser(email);
        if(user != null){
            if(user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public static UserDTO findUser(String email){
        try {
            CustomerService customerService = new CustomerService();
            List<CustomerDTO> customersList = customerService.getAll();

            for (CustomerDTO customer : customersList) {
                if (customer instanceof UserDTO && ((UserDTO) customer).getEmail().equals(email)) {
                        return (UserDTO) customer;

                }
            }
            return null;
        } catch (Exception e) {
            FileUtil.logError(e);
            return null;
        }
    }
}
