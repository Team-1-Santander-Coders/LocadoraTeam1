package main.java.com.team1.service;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.CustomerRepositoryImpl;

import java.util.List;

public class CustomerService {
    /**
     * A função da CustomerService é implementar a CustomerRepositoryImpl, chamar os seus métodos e tratar as excessões
     */
    private final CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();

    /**
     * Implementa o método save da classe <code>CustomerRepository</code>, tratando as suas excessões
     * @param customerDTO
     */
    public void addCustomer(CustomerDTO customerDTO) {
        try{
            customerRepository.save(customerDTO);
        } catch(IllegalArgumentException | DuplicateEntityException e){
            System.out.println("Erro ao adicionar cliente: " + e.getMessage());
        }
    }

    /**
     * Implementa o método update, da classe <code>CustomerRepositoryImpl</code>, tratando as suas excessões
     * @param customerDTO
     * @param newName
     * @param newAddress
     * @param newPhone
     */
    public void updateCustomer(CustomerDTO customerDTO, String newName, String newAddress, String newPhone) {
        try{
            customerRepository.update(customerDTO, newName, newAddress, newPhone);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Implementa o método update da classe <code>CustomerRepositoryImpl</code>
     * @return List
     */
    public List<CustomerDTO> getAllCustomers(){
        return customerRepository.findAll();
    }

    /**
     * Implementa o método delete da classe <code>CustomerRepositoryImpl</code>, tratando as suas excessões
     * @param customerDTO
     */
    public void deleteCustomer(CustomerDTO customerDTO) {
        try {
            customerRepository.delete(customerDTO);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Método que faz a busca pelo nome (ou por parte dele). Recebe como parametro uma String name, faz a iteração
     * na lista de clientes e utiliza o método String.contains() para enconttrar um cliente que possua o nome ou uma parte
     * dele.
     * @param name
     * @return
     */
    public CustomerDTO searchCustomerByName(String name) {
        CustomerDTO customer = null;
        try {
            for (CustomerDTO customerDTO : getAllCustomers()) {
                if (customerDTO.getName().contains(name)) {
                    customer = customerDTO;
                }
            }
            if (customer == null) throw new EntityNotFoundException("Cliente não encontrado");
            return customer;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Método utilizado na lógica de paginação. Recebe como parametro uma int pageNumber e uma int pageSize. A int pageSize
     * basicamente refere-se a quantos objetos eu quero conter naquela pagina.
     * @param pageNumber
     * @param pageSize
     * @return subList(fromIndex, toIndex)
     */
    public List<CustomerDTO> getCustomersByPage(int pageNumber, int pageSize){
        List<CustomerDTO> customersList = customerRepository.findAll();

        try {
            int fromIndex = (pageNumber - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, customersList.size());

            if (fromIndex < 0 || toIndex > customersList.size()) throw new IllegalArgumentException("Argumentos de listagem invalidos") ;
            return customersList.subList(fromIndex, toIndex);
        } catch (IllegalArgumentException e){
            System.out.println(e.fillInStackTrace());
            return null;
       }
    }

    /**
     * Implementa o método findAll() da classe <code>CustomerRepositoryImpl</code>
     * @return List
     */
    public List<CustomerDTO> getAll(){
        return customerRepository.findAll();
    }


}
