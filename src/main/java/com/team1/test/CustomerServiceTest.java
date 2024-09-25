package main.java.com.team1.test;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.LegalPersonDTO;
import main.java.com.team1.dto.PhysicalPersonDTO;
import main.java.com.team1.service.CustomerService;

import java.util.List;

public class CustomerServiceTest {

    public static void main(String[] args) {

        CustomerService customerService = new CustomerService();

        System.out.println("Teste 1 - Adicionar três clientes, sendo que um terá a quantidade incorreta de digitos do cpf");
        customerService.addCustomer(new PhysicalPersonDTO("Marcinho do reboque", "Rua A", "11111111111", "12345678900"));
        customerService.addCustomer(new LegalPersonDTO("Cafeteria do Jerimum", "Lá na casa do carai", "22222222222", "11111111111111"));
        customerService.addCustomer(new PhysicalPersonDTO("Mariazinha filha de dona Ildete", "Rua B", "333333333", "1122233344"));
        //customerService.addCustomer(new LegalPersonDTO("Borracharia do Boca", "Lá em camaçari", "77777777777", "9998887770001"));
        //customerService.addCustomer(new LegalPersonDTO("Ricardinho Agiota", "vish nem sei", "88888888888", "999999999999"));
        //customerService.deleteCustomer(new LegalPersonDTO("Ricardinho Agiota", "vish nem sei", "88888888888", "999999999999"));

        System.out.println("Teste 2 - Paginação: ");
        List<CustomerDTO> customersPage1 = customerService.getCustomersByPage(1, 2);
        List<CustomerDTO> customersPage2 = customerService.getCustomersByPage(2, 2);

        System.out.println("Imprimindo paginas");
        System.out.println("Página 1: " + customersPage1);
        System.out.println("Página 2: " + customersPage2);


        System.out.println("\n\nTeste 3 -Imprimindo clientes encontrados por nome");
        System.out.println(customerService.searchCustomerByName("Marcinho do reboque"));
        System.out.println(customerService.searchCustomerByName("Cafeteria do Jerimum"));

        System.out.println("Teste 4 - Recuperar toda a lista de clientes");
        System.out.println("\n\nImprimindo lista de clientes");
        for(CustomerDTO customer : customerService.getAll() ){
            System.out.println(customer);
        }
    }



}

