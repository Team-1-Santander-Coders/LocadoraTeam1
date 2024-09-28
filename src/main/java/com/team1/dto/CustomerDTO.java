package main.java.com.team1.dto;

import java.io.Serial;
import java.io.Serializable;

public abstract class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * A CustomerDTO é uma classe abstrata, que visa definir a estrutura que as classes filhas precisam seguir. Também
     * define os métodos básicos, como os Getters e Setters. Essa classe também implementa a interfacde Serializable,
     * necessária para a serialização dos objetos pela classe <code>FileUtil</code>, garantindo a persistência de dados.
     */
    private String name;
    private String address;
    private String phone;

    /**
     * Construtor da classe abstrata, que será acessada via <code>super()</code> pelas classes filhas
     * @param name
     * @param address
     * @param phone
     */
    protected CustomerDTO(String name, String address, String phone){

        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    /**
     * Método de acesso da String privada Name
     * @return String Name
     */
    public String getName() {
        return name;
    }

    /**
     * Método para definir o valor da String privada Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método de acesso à String privada Address
     * @return String Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Método para definir o valor da String privada Address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Método de acesso à String privada Phone
     * @return String Phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Método para definir o valor da String privada Phone;
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Método que retorna a classe da instancia do objeto
     * @return String class SimpleName
     */
    public String typeOf(){
        return this.getClass().getSimpleName();
    };

    /**
     * Método sobrescrito, que garante uma melhor visualização dos objetos dessa classe
     * @return String toString
     */
    @Override
    public String toString() {
        return "[ " + typeOf() + " ]" +
                " name= " + name + ", " +
                "address= " + address + ", " +
                "phone= " + phone + "]";
    }

    /**
     * Sobrescrita do método equals. Seguindo as regras de negócio,
     * caso dois customersDTO genéricos possuam o mesmo nome, eles são iguais.
     * @param customerDTO
     * @return boolean customerDTO.name.equals(this.name);
     */
    public boolean equals(CustomerDTO customerDTO){
        return customerDTO.getDocument().equals(this.getDocument());
    }

    /**
     * Sobrescrita do método hashCode para garantir a lógica de que, se dois objetos da classe possuem o mesmo nome, eles são iguais
     * @return
     */
    public int hashCode(){
        return (31 * name.hashCode() + name.hashCode());
    }

    public String getTipo() {
        return "Generic customer";
    }

    public String getDocument() {
        return "Generic document";
    }

    public boolean isAdmin() {
        return false;
    }
}
