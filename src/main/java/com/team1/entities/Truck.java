package main.java.com.team1.entities;

import java.io.Serial;

/**
 * Classe que representa um caminhão, implementando a interface {@link Vehicle}.
 * <p>
 * Esta classe fornece as propriedades e métodos relacionados a um caminhão, incluindo
 * informações como placa, modelo, marca, ano, disponibilidade e preço diário de locação.
 * </p>
 */
public class Truck implements Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String placa;
    private final String modelo;
    private final String marca;
    private final int ano;
    private boolean disponivel;
    private static final double precoDiaria = 200.00;

    /**
     * Construtor da classe Truck.
     *
     * @param placa  A placa do caminhão.
     * @param modelo O modelo do caminhão.
     * @param marca  A marca do caminhão.
     * @param ano    O ano de fabricação do caminhão.
     */
    public Truck(String placa, String modelo, String marca, int ano) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.disponivel = true;
    }

    /**
     * Retorna a placa do caminhão.
     *
     * @return A placa do caminhão.
     */
    @Override
    public String getPlaca() {
        return placa;
    }

    /**
     * Retorna o modelo do caminhão.
     *
     * @return O modelo do caminhão.
     */
    @Override
    public String getModelo() {
        return modelo;
    }

    /**
     * Retorna a marca do caminhão.
     *
     * @return A marca do caminhão.
     */
    @Override
    public String getMarca() {
        return marca;
    }

    /**
     * Retorna o ano de fabricação do caminhão.
     *
     * @return O ano de fabricação do caminhão.
     */
    @Override
    public int getAno() {
        return ano;
    }

    /**
     * Verifica se o caminhão está disponível para locação.
     *
     * @return true se o caminhão está disponível, false caso contrário.
     */
    @Override
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Retorna o preço diário de locação do caminhão.
     *
     * @return O preço diário de locação do caminhão.
     */
    @Override
    public double getPrecoDiaria() {
        return precoDiaria;
    }

    /**
     * Retorna o tipo do veículo como uma string.
     *
     * @return Tipo do veículo ("Caminhão").
     */
    @Override
    public String getTipo() {
        return "Caminhão";
    }
}
