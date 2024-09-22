package main.java.com.team1.entities;

import java.io.Serial;

/**
 * Classe que representa uma motocicleta, implementando a interface {@link Vehicle}.
 * <p>
 * Esta classe fornece as propriedades e métodos relacionados a uma motocicleta,
 * incluindo informações como placa, modelo, marca, ano, disponibilidade e preço diário
 * de locação.
 * </p>
 */
public class Motorcycle implements Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;

    // Atributos da motocicleta
    private final String placa;     // Placa da motocicleta (não pode ser alterada após a criação)
    private final String modelo;     // Modelo da motocicleta
    private final String marca;      // Marca da motocicleta
    private final int ano;           // Ano de fabricação da motocicleta
    private boolean disponivel;      // Disponibilidade da motocicleta
    private static final double precoDiaria = 100.00; // Preço diário de locação

    /**
     * Construtor da classe Motorcycle.
     *
     * @param placa  A placa da motocicleta.
     * @param modelo O modelo da motocicleta.
     * @param marca  A marca da motocicleta.
     * @param ano    O ano de fabricação da motocicleta.
     */
    public Motorcycle(String placa, String modelo, String marca, int ano) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.disponivel = true;
    }

    /**
     * Retorna a placa da motocicleta.
     *
     * @return Placa da motocicleta.
     */
    @Override
    public String getPlaca() {
        return placa; // Retorna a placa da motocicleta
    }

    /**
     * Retorna o modelo da motocicleta.
     *
     * @return Modelo da motocicleta.
     */
    @Override
    public String getModelo() {
        return modelo;
    }

    /**
     * Retorna a marca da motocicleta.
     *
     * @return Marca da motocicleta.
     */
    @Override
    public String getMarca() {
        return marca;
    }

    /**
     * Retorna o ano de fabricação da motocicleta.
     *
     * @return Ano de fabricação da motocicleta.
     */
    @Override
    public int getAno() {
        return ano;
    }

    /**
     * Verifica se a motocicleta está disponível.
     *
     * @return true se a motocicleta está disponível, caso contrário, false.
     */
    @Override
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Retorna o preço diário de locação da motocicleta.
     *
     * @return Preço diário de locação da motocicleta.
     */
    @Override
    public double getPrecoDiaria() {
        return precoDiaria;
    }

    /**
     * Retorna o tipo do veículo.
     *
     * @return Tipo do veículo como uma string ("Moto").
     */
    @Override
    public String getTipo() {
        return "Moto";
    }
}
