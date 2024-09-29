package main.java.com.team1.entities;

import main.java.com.team1.dto.AgencyDTO;

import java.io.Serial;

/**
 * Classe que representa um carro, implementando a interface {@link Vehicle}.
 * <p>
 * Esta classe fornece as propriedades e métodos relacionados a um carro, incluindo
 * informações como placa, modelo, marca, ano, disponibilidade e preço diário de locação.
 * </p>
 */
public class Car implements Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String placa;
    private final String modelo;
    private final String marca;
    private final int ano;
    private boolean disponivel;
    private static final double precoDiaria = 150.00;
    private AgencyDTO agency;

    /**
     * Construtor da classe Car.
     *
     * @param placa  A placa do carro.
     * @param modelo O modelo do carro.
     * @param marca  A marca do carro.
     * @param ano    O ano de fabricação do carro.
     * @param agency    A agência que o carro está associado.
     */
    public Car(String placa, String modelo, String marca, int ano, AgencyDTO agency) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.agency = agency;
        this.disponivel = true;
    }

    /**
     * Retorna a placa do carro.
     *
     * @return A placa do carro.
     */
    @Override
    public String getPlaca() {
        return placa.toUpperCase();
    }

    /**
     * Retorna o modelo do carro.
     *
     * @return O modelo do carro.
     */
    @Override
    public String getModelo() {
        return modelo;
    }

    /**
     * Retorna a marca do carro.
     *
     * @return A marca do carro.
     */
    @Override
    public String getMarca() {
        return marca;
    }

    /**
     * Retorna o ano de fabricação do carro.
     *
     * @return O ano de fabricação do carro.
     */
    @Override
    public int getAno() {
        return ano;
    }

    /**
     * Verifica se o carro está disponível para locação.
     *
     * @return true se o carro está disponível, false caso contrário.
     */
    @Override
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Retorna o preço diário de locação do carro.
     *
     * @return O preço diário de locação do carro.
     */
    @Override
    public double getPrecoDiaria() {
        return precoDiaria;
    }

    /**
     * Retorna o tipo do veículo como uma string.
     *
     * @return Tipo do veículo ("Carro").
     */
    @Override
    public String getTipo() {
        return "Carro";
    }

    public AgencyDTO getAgency() {
        return agency;
    }
}