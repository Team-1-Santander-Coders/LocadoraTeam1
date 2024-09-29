package main.java.com.team1.entities;

import java.io.Serializable;

/**
 * Interface que define as características comuns de um Cliente.
 * Implementa a interface Serializable para permitir a serialização do objeto.
 */
public interface Customer extends Serializable {

    /**
     * Retorna o nome do cliente.
     *
     * @return O nome do cliente.
     */
    public String getName();

    /**
     * Define o nome do cliente.
     *
     * @param name O novo nome do cliente.
     */
    public void setName(String name);

    /**
     * Retorna o endereço do cliente.
     *
     * @return O endereço do cliente.
     */
    public String getAddress();

    /**
     * Define o endereço do cliente.
     *
     * @param address O novo endereço do cliente.
     */
    public void setAddress(String address);

    /**
     * Retorna o telefone do cliente.
     *
     * @return O telefone do cliente.
     */
    public String getPhone();

    /**
     * Define o telefone do cliente.
     *
     * @param phone O novo telefone do cliente.
     */
    public void setPhone(String phone);

    /**
     * Verifica se o objeto passado como parâmetro é igual ao cliente.
     *
     * @param customer O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    public boolean equals(Customer customer);

    /**
     * Retorna o código hash do cliente.
     *
     * @return O código hash do cliente.
     */
    public int hashCode();

    /**
     * Retorna uma representação em string do cliente.
     *
     * @return Uma representação em string do cliente.
     */
    public String toString();
}
