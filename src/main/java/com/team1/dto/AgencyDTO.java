package main.java.com.team1.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe que representa um Data Transfer Object (DTO) para uma agência.
 * <p>
 * Esta classe é utilizada para transferir dados de agências entre diferentes
 * camadas da aplicação, encapsulando informações como nome e endereço.
 * </p>
 * <p>
 * A classe implementa {@link Serializable} para permitir que seus objetos
 * sejam serializados e transportados ou armazenados com facilidade.
 * </p>
 *
 * @param name O nome da agência.
 * @param address O endereço da agência.
 */

public record AgencyDTO(String name, String address) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Verifica se este {@code AgencyDTO} é igual a outro objeto.
     * <p>
     * Dois {@code AgencyDTO}s são considerados iguais se possuem o mesmo nome
     * e o mesmo endereço.
     * </p>
     *
     * @param o o objeto a ser comparado com este {@code AgencyDTO}.
     * @return {@code true} se os objetos forem iguais, caso contrário {@code false}.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyDTO agencyDTO)) return false;
        return Objects.equals(name, agencyDTO.name) && Objects.equals(address, agencyDTO.address);
    }

    /**
     * Gera um código hash para este {@code AgencyDTO} com base no nome e endereço.
     *
     * @return o valor do código hash para este {@code AgencyDTO}.
     */

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    /**
     * Retorna uma representação textual deste {@code AgencyDTO}.
     * <p>
     * O formato utilizado é: {@code AgencyDTO{name='nome', address='endereço'}}.
     * </p>
     *
     * @return uma string representando este {@code AgencyDTO}.
     */

    @Override
    public String toString() {
        return "AgencyDTO{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
