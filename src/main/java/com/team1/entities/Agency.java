package main.java.com.team1.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Agency implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String address;

    /**
     * Construtor da classe Agency.
     *
     * @param name    O nome da agência.
     * @param address O endereço da agência.
     */
    public Agency(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * Retorna o endereço da agência.
     *
     * @return O endereço da agência.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Define o endereço da agência.
     *
     * @param address O novo endereço da agência.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retorna o nome da agência.
     *
     * @return O nome da agência.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome da agência.
     *
     * @param name O novo nome da agência.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Verifica se o objeto é igual a agência comparada.
     *
     * @param o O objeto que deve ser comparado.
     * @return true se for igual (compara nome e endereço) e false se for diferente.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agency agency)) return false;
        return Objects.equals(name, agency.name) && Objects.equals(address, agency.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}
