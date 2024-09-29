package main.java.com.team1.util;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.UserDTO;
import main.java.com.team1.service.CustomerService;

import java.util.List;


public class UserAuth {
    private static final CustomerService customerService = new CustomerService();

    /**
     * Realiza a autenticação de login de um usuário.
     *
     * @param email    O email do usuário.
     * @param password A senha do usuário.
     * @return O objeto UserDTO do usuário autenticado, ou null se não encontrado ou senha incorreta.
     */
    public static UserDTO AuthLogin(String email, String password){
        UserDTO user = findUserByMail(email);
        if(user != null){
            if(user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    /**
     * Encontra um usuário pelo seu email.
     *
     * @param email O email do usuário.
     * @return O objeto UserDTO do usuário encontrado, ou null se não encontrado.
     */
    public static UserDTO findUserByMail(String email){
        try {
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
