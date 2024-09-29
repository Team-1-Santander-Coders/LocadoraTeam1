package main.java.com.team1.dto;

import java.io.Serial;


public class LegalPersonDTO extends CustomerDTO {

    /**
     * A classe <code>LegalPersonDTO</code> é uma subclasse da classe abstrata CustomerDTO,
     * herdando todos as suas caracteristicas, sendo necessária a implementação dos métodos.
     * Como a classe pai é Serializavel, essa subclasse mantem essa caracteristica.
     */

    @Serial
    private static final long serialVersionUID = 1L;
    private String cnpj;

    /**
     * No construtor da <code>LegalPersonDTO</code>, temos a inclusão de mais um parametro: O CNPJ.

     * @param name
     * @param address
     * @param phone
     * @param cnpj
     */
    public LegalPersonDTO(String name, String address, String phone, String cnpj) {
        super(name, address, phone);
        this.cnpj = cnpj;
    }

    /**
     * Método de acesso à String privada Cnpj
     * @return String cnpj
     */

    public String getCnpj() {
        return cnpj;
    }

    /**
     * Método usado para definir o valor da String privada Cnpj
     * @param cnpj
     */

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    /**
     * Sobrescrita do método equals da classe pai, definindo melhor o critério de igualdade entre dois objetos.
     * Se dois objetos do tipo LegalPersonDto possuem o mesmo CNPJ, elçes são iguais
     * @param customerDTO
     * @return ((LegalPersonDTO) customerDTO).getCnpj().equals(this.getCnpj())
     */

    @Override
    public boolean equals(CustomerDTO customerDTO) {
        if(customerDTO instanceof LegalPersonDTO) {
            return ((LegalPersonDTO) customerDTO).getCnpj().equals(this.getCnpj());
        }
        return false;
    }

    /**
     * Como houve a sobrescrita do método equals, definimos também uma nova lógica para o HashCode, garantindo que,
     * caso dois objetos da classe LegalPersonDTO possuam o mesmo cnpj, ambos terão o mesmo hashCode
     * @return (31*this.cnpj.hashCode()) + this.cnpj.hashCode();
     */

    public int hashCode(){
        return (31*this.cnpj.hashCode()) + this.cnpj.hashCode();
    }

    /**
     * Sobrescrita do método toString, para garantir que a informação do CNPJ seja visualizada
     * @return String toString
     */

    @Override
    public String toString() {
        return "[ " + typeOf() + " ] " +
                "name= " + getName() + ", " +
                "address= " + getAddress() + ", " +
                "phone= " + getPhone() + ", " +
                "cpnj= " + getCnpj()  + "]";
    }

    @Override
    public String getDocument() {
        return this.getCnpj();
    }

    /**
     * Retorna o tipo de cliente como "Física".
     * <p>
     * Este método sobrescreve a implementação genérica da classe base
     * {@link CustomerDTO} e especifica que este cliente é do tipo "Jurídica".
     * </p>
     *
     * @return Uma string indicando que o tipo de cliente é "Jurídica".
     */

    @Override
    public String getTipo() {
        return "Jurídica";
    }
}


