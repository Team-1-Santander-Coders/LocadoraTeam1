package main.java.com.team1.entities;

/**
 * Entidade que implementa a interface {@link main.java.com.team1.entities.Customer},
 * representando um cliente pessoa jurídica.
 * <p>
 * Tem como propriedades: <code>Name, Address, Phone e CNPJ</code>.
 * */
public class LegalPerson implements Customer{
    private String name;
    private String address;
    private String phone;
    private String cnpj;

    /**
     * Construtor da pessoa física.
     *
     * @param name Nome do cliente.
     * @param address Endereço do cliente.
     * @param phone Telefone do cliente.
     * @param cnpj CNPJ do cliente (Pessoa Jurídica)
     */
    public LegalPerson(String name, String address, String phone, String cnpj) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.cnpj = cnpj;
    }

    /**
     * Retorna o nome do cliente Pessoa Jurídica.
     * @return nome do cliente.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Define o nome do cliente Pessoa Jurídica.
     * @param name O novo nome do cliente.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o endereço do cliente Pessoa Jurídica.
     * @return endereço do cliente.
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * Define o novo endereço do cliente Pessoa Jurídica
     * @param address O novo endereço do cliente.
     */
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retorna o telefone do cliente Pessoa Jurídica.
     * @return o telefone do cliente.
     */
    @Override
    public String getPhone() {
        return phone;
    }

    /**
     * Define o novo telefone do cliente Pessoa Jurídica.
     * @param phone O novo telefone do cliente.
     */
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Verifica se os CNPJ de dois clientes Pessoas Jurídicas são iguais.
     * @param customer O cliente a ser comparado.
     * @return boolean
     */
    @Override
    public boolean equals(Customer customer) {
        return this.cnpj.equals(((LegalPerson) customer).cnpj);
    }

    /**
     * Define o HashCode com base no CNPJ do cliente.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return (31*cnpj.hashCode()) + cnpj.hashCode();
    }
}