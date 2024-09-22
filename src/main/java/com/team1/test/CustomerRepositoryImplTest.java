package main.java.com.team1.test;

import main.java.com.team1.dto.LegalPersonDTO;
import main.java.com.team1.dto.PhysicalPersonDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.CustomerRepositoryImpl;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;

class CustomerRepositoryImplTest {

    public static void main(String[] args) {
        CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();
        FileUtil.writeToFile(new ArrayList<>(), "src/resources/data/customers.dat"); //Limpar base da dados antes de testar

        //testando a criação de três clientes, sendo um repetido:
        System.out.println("Teste 1 - Criação de três clientes, sendo um repetido.");
        LegalPersonDTO lp = new LegalPersonDTO("Ricardinho serviços de agiotagem LTDA", "Rua A", "1111111111", "11111111111111");
        PhysicalPersonDTO pp = new PhysicalPersonDTO("Mariaziha filha de dona Ildete", "Rua b", "22222222222", "00000000000");
        PhysicalPersonDTO pp2 = new PhysicalPersonDTO("Mariaziha filha de dona Ildete", "Rua b", "22222222222", "00000000000");

        try {
            customerRepository.save(lp);
            customerRepository.save(pp);
            customerRepository.save(pp2);
        }catch (DuplicateEntityException e){
            e.printStackTrace();
        }

        System.out.println("Resultado do teste 1: ");
        customerRepository.findAll().forEach(System.out::println);

        System.out.println("\n\nTeste 2 - Método update (E o buscar pelo documento): ");
        try {
            //Alterando o cadastro do Ricardinho
            customerRepository.update(lp, "Ricardinho nao é mais agiota", "", "22222222222");
            System.out.println(customerRepository.findByDocument(lp.getCnpj()));

        }catch (EntityNotFoundException e){
            e.printStackTrace();
        }

        System.out.println("\nTeste 3 -  Deletar");

        try {
            //Deletando Mariazinha
            customerRepository.delete(pp);
            System.out.print("Lista após deletar: ");
            customerRepository.findAll().forEach(System.out::println);
        }catch(EntityNotFoundException e){
            e.printStackTrace();
        }



    }
}
