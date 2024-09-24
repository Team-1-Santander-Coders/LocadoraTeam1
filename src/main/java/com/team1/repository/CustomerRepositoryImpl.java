package main.java.com.team1.repository;

import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.LegalPersonDTO;
import main.java.com.team1.dto.PhysicalPersonDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe <code>CustomerRepositoryImpl</code> implementa a interface CustomerRepository.
 * Essa classe será a responsável por fazer as operações com os dados. Sendo assim, essa classe precisa garantir
 * que as regras de negócio estejam sendo aplicadas, e, se tudo estiver nas conformidades, ela faz a operação solicitada.
 *
 * <p>
 * São métodos dessa classe:
 * <p><code>save(CustomerDTO)</code> - Salva o cliente passado na base de dados</p>
 * <p><code>findAll()</code> - Recuperar todos os dados salvos</p>
 * <p><code>update(CustomerDTO, params)</code> - Edita um cliente salvo</p>
 * <p><code>delete(CustomerDTO)</code> - Deleta o cliente passado</p>
 * <p><code>findByDocument(String document)</code> - Recupera um cliente com base em seu documento cadastrado</p>
 * </p>
 */
public class CustomerRepositoryImpl implements CustomerRepository {
     private static final String CUSTOMERS_FILE = "src/resources/data/customers.dat";

    /**
     * Método responsável por salvar o cliente. Faz a verificação se o cliente já foi adicionado na base de dados,
     * bem como se a quantidade de digitos do cliente se enquadram nas regras do negócio.
     * @param customer - Tipo PhysicalPersonDTO ou LegalPersonDTO
     * @throws DuplicateEntityException - Proteção contra duplicidade de cadastro de clientes
     * @throws IllegalArgumentException - Proteção contra dados que não se enquadrem nas regras de negócio
     */
    @Override
    public void save(CustomerDTO customer) throws DuplicateEntityException, IllegalArgumentException {
        List<CustomerDTO> customersList = findAll();

        if(isIncluded(customer)) throw new DuplicateEntityException("Cliente já cadastrado");
        else {
            if(customer instanceof LegalPersonDTO) {
                if(((LegalPersonDTO) customer).getCnpj() == null || ((LegalPersonDTO) customer).getCnpj().length() != 14) {
                    throw new IllegalArgumentException("CNPJ invalido");
                }
            }

            if(customer instanceof PhysicalPersonDTO) {
                if(((PhysicalPersonDTO) customer).getCpf() == null || ((PhysicalPersonDTO) customer).getCpf().length() != 11) {
                    throw new IllegalArgumentException("CPF invalido");
                }
            }
            customersList.add(customer);
            FileUtil.writeToFile(customersList, CUSTOMERS_FILE);
        }
    }

    /**
     * Retorna uma lista com todos os dados persistidos
     * @return List<CustomerDTO>
     */
    @Override
    public List<CustomerDTO> findAll() {
        List<CustomerDTO> customersList;
        if(FileUtil.readFromFile(CUSTOMERS_FILE) == null) customersList = new ArrayList<>();
        else customersList = FileUtil.readFromFile(CUSTOMERS_FILE);

        return customersList;
    }


    /**
     * Método responsável por editar um contato anteriormente adicionado à base de dados.
     * Caso os parâmetros passados sejam vazios, os mesmos não serão editados.
     * @param customer  Tipo PhysicalPersonDTO ou LegalPersonDTO
     * @param newName
     * @param newAddress
     * @param newPhone
     * @throws EntityNotFoundException  Refere-se a não poder editar um cliente inexistente
     */
    public void update(CustomerDTO customer, String newName, String newAddress, String newPhone) throws EntityNotFoundException {
        List<CustomerDTO> customersList = findAll();

        if(!isIncluded(customer)) throw new EntityNotFoundException("Contato não existe");
        else {
            for (CustomerDTO c : customersList) {
                if (c.hashCode() == customer.hashCode()) {
                    if (newName.isEmpty()) newName = ((LegalPersonDTO) customer).getName();
                    if (newAddress.isEmpty()) newAddress = ((LegalPersonDTO) customer).getAddress();
                    if (newPhone.isEmpty()) newPhone = ((LegalPersonDTO) customer).getPhone();

                    c.setName(newName);
                    c.setAddress(newAddress);
                    c.setPhone(newPhone);

                    FileUtil.writeToFile(customersList, CUSTOMERS_FILE);
                }
            }
        }
    }

    /**
     * Deletar um cliente passado como parãmetro
     * @param customer
     * @throws EntityNotFoundException - Caso o cliente não exista
     */
    @Override
    public void delete(CustomerDTO customer) throws EntityNotFoundException {
        List<CustomerDTO> customersList = findAll();

        if(!isIncluded(customer)) throw new EntityNotFoundException("Cliente não encontrado");
        for(CustomerDTO c : customersList){
            if(customer.hashCode() == c.hashCode()){
                customersList.remove(c);
                FileUtil.writeToFile(customersList, CUSTOMERS_FILE);
                customersList = findAll();
                break;
            }
        }
    }

    /**
     * Recebe um documento como parâmetro. Faz uma iteração da lista de clientes, comparando o documento com os CPF e CNPJ
     * cadastrados. Retorna o cliente que possui o documento igual ao passado como parâmetro.
     * @param document
     * @return CustomerDTO
     */
    @Override
    public CustomerDTO findByDocument(String document) {
        List<CustomerDTO> customersList = findAll();

        for(CustomerDTO customer : customersList){
            if(customer instanceof LegalPersonDTO){
                if(document.equals(((LegalPersonDTO) customer).getCnpj())) return customer;
            }
            if(customer instanceof PhysicalPersonDTO){
                if(document.equals(((PhysicalPersonDTO) customer).getCpf())) return customer;
            }
        }
        return null;
    }

    /**
     * Método de auxilio, cuja a função é verificar se um cliente passado como parametro está presente na base de dados.
     * @param customerDTO
     * @return boolean
     */
    private boolean isIncluded(CustomerDTO customerDTO) {
        List<CustomerDTO> customersList = findAll();
        if(customersList.getFirst() == null) return false;
        for(CustomerDTO customer : customersList){
                if(customerDTO.hashCode() == customer.hashCode())
                    return true;
            }
        return false;
        }
}
