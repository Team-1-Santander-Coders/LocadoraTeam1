package main.java.com.team1.dto;

import java.io.Serial;

/**
 * A classe <code>LegalPersonDTO</code> é uma subclasse da classe abstrata CustomerDTO,
 * herdando todos as suas caracteristicas, sendo necessária a implementação dos métodos.
 * Como a classe pai é Serializavel, essa subclasse mantem essa caracteristica.
 */

public class PhysicalPersonDTO extends CustomerDTO {
    @Serial
    private static final long serialVersionUID = 1L;
    private String cpf;

    /**
     * No construtor da <code>PhysicalPersonDTO</code>, temos a inclusão de mais um parametro: O CPF.

     * @param name
     * @param address
     * @param phone
     * @param cpf
     */
    public PhysicalPersonDTO(String name, String address, String phone, String cpf)  {
        super(name, address, phone);
        this.cpf = cpf;
    }

    /**
     * Método de acesso à String privada cpf
     * @return String cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Método utilizado para definir o valor da String privada cpf
     * @param cpf
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Sobrescrita do método equals da classe pai, definindo melhor o critério de igualdade entre dois objetos.
     * Se dois objetos do tipo PhysicalPersonDto possuem o mesmo CPF, elçes são iguais
     * @param customerDTO
     * @return ((PhysicalPersonDTO) customerDTO).getCpf().equals(this.getCpf())
     */
    @Override
    public boolean equals(CustomerDTO customerDTO) {
        if(customerDTO instanceof PhysicalPersonDTO) {
            return ((PhysicalPersonDTO) customerDTO).getCpf().equals(this.getCpf());
        }
        return false;
    }

    /**
     * Como houve a sobrescrita do método equals, definimos também uma nova lógica para o HashCode, garantindo que,
     * caso dois objetos da classe PhysicalPersonDTO possuam o mesmo cpf, ambos terão o mesmo hashCode
     * @return (31*this.cpf.hashCode()) + this.cpf.hashCode();
     */
    public int hashCode(){
        return (31*this.cpf.hashCode()) + this.cpf.hashCode();
    }

    /**
     * Sobrescrita do método toString, para garantir que a informação do CPF seja visualizada
     * @return String toString
     */
    @Override
    public String toString() {
        return "[ " + typeOf() + " ] " +
                "name= " + getName() + ", " +
                "address= " + getAddress() + ", " +
                "phone= " + getPhone() + ", " +
                "cpf= " + getCpf()  + "]";
    }

}