package main.java.com.team1.entities;

/**
 * Representa uma pessoa física como cliente.
 * Esta classe implementa a interface {@link main.java.com.team1.entities.Customer} e encapsula
 * os detalhes do cliente, como nome, endereço, telefone e CPF (Cadastro de Pessoas Físicas).
 */
public class PhysicalPerson implements Customer {

    private String name;
    private String address;
    private String phone;
    private String cpf;

    /**
     * Constrói um objeto PhysicalPerson com os detalhes especificados.
     *
     * @param name    o nome da pessoa física
     * @param address o endereço da pessoa física
     * @param phone   o número de telefone da pessoa física
     * @param cpf     o CPF da pessoa física
     */
    public PhysicalPerson(String name, String address, String phone, String cpf) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.cpf = cpf;
    }

    /**
     * Obtém o nome da pessoa física.
     *
     * @return o nome da pessoa física
     */
    public String getName() {
        return name;
    }

    /**
     * Define o novo nome da pessoa física.
     * @param name O novo nome do cliente.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o endereço da pessoa física.
     * @return o endereço da pessoa física.
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * Define o novo endereço da pessoa física.
     * @param address O novo endereço do cliente.
     */
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retorna o telefone da pessoa física.
     * @return o telefone do cliente.
     */
    @Override
    public String getPhone() {
        return phone;
    }

    /**
     * Define o novo telefone da pessoa física.
     * @param phone O novo telefone do cliente.
     */
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Compara esta pessoa física com outro cliente para verificar igualdade
     * com base no CPF.
     *<p>
     * @param customer o cliente a ser comparado
     * @return true se o CPF de ambos os clientes for igual, false caso contrário
     */
    @Override
    public boolean equals(Customer customer) {
        return this.cpf.equals(((PhysicalPerson) customer).cpf);
    }

    /**
     *
     * Define o HashCode com base no CPF do cliente.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return (31 * cpf.hashCode()) + cpf.hashCode();
    }
}