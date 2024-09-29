package main.java.com.team1.service;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.LegalPersonDTO;
import main.java.com.team1.dto.PhysicalPersonDTO;
import main.java.com.team1.entities.User;
import main.java.com.team1.repository.UserRepository;

import java.util.List;

/**
 * A classe {@code UserService} gerencia operações relacionadas a usuários.
 * Ela encapsula a lógica de negócios para adicionar e recuperar usuários do repositório de usuários.
 */
public class UserService {
    private UserRepository userRepository;
    private CustomerService customerService;

    /**
     * Cria uma nova instância de {@code UserService}.
     * Inicializa o repositório de usuários e o serviço de clientes.
     */
    public UserService() {
        userRepository = new UserRepository();
        customerService = new CustomerService();
    }

    /**
     * Adiciona um novo usuário.
     *
     * @param user O objeto {@code User} a ser adicionado.
     * @throws NullPointerException Se o usuário for nulo.
     */
    public void addUser(User user) {

        try{
            CustomerDTO customer;
            if(user != null){
                System.out.println("Física".hashCode());
                System.out.println(user.getTipo().hashCode());
                if(user.getTipo().trim().equals("Física") ) customer = new PhysicalPersonDTO(user.getName(), user.getAddress(), user.getPhone(), user.getDocument());
                else if(user.getTipo().trim().equals("Jurídica")) customer = new LegalPersonDTO(user.getName(), user.getAddress(), user.getPhone(), user.getDocument());
                else customer = null;
                customerService.addCustomer(customer);
                userRepository.addUser(user);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Recupera todos os usuários registrados.
     *
     * @return Uma lista de objetos {@code User} representando todos os usuários.
     */
    public List<User> getUsers(){
        return userRepository.findAll();
        }

}
