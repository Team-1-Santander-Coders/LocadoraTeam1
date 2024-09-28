package main.java.com.team1.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe abstrata que representa um Data Transfer Object (DTO) para veículos.
 * <p>
 * Esta classe fornece as propriedades e métodos comuns que todos os DTOs de veículos
 * devem implementar. Inclui informações como placa, modelo, marca, ano, disponibilidade
 * e preço diário de locação. A classe é serializável para permitir a persistência de dados.
 * </p>
 */
public abstract class VehicleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String placa;
    protected String modelo;
    protected String marca;
    protected int ano;
    protected boolean disponivel;
    protected double precoDiaria;
    protected AgencyDTO agency;

    /**
     * Construtor da classe VehicleDTO.
     *
     * @param placa         A placa do veículo.
     * @param modelo        O modelo do veículo.
     * @param marca         A marca do veículo.
     * @param ano           O ano de fabricação do veículo.
     * @param disponivel    A disponibilidade do veículo.
     * @param precoDiaria   O preço diário de locação do veículo.
     */
    protected VehicleDTO(String placa, String modelo, String marca, int ano, boolean disponivel, double precoDiaria) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.disponivel = disponivel;
        this.precoDiaria = precoDiaria;
        this.agency = agency;
    }

    /**
     * Retorna a placa do veículo.
     *
     * @return Placa do veículo.
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Retorna o modelo do veículo.
     *
     * @return Modelo do veículo.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Retorna a marca do veículo.
     *
     * @return Marca do veículo.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Retorna o ano de fabricação do veículo.
     *
     * @return Ano de fabricação do veículo.
     */
    public int getAno() {
        return ano;
    }

    /**
     * Verifica se o veículo está disponível.
     *
     * @return true se o veículo estiver disponível, caso contrário, false.
     */
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Retorna o preço diário de locação do veículo.
     *
     * @return Preço diário de locação.
     */
    public double getPrecoDiaria() {
        return precoDiaria;
    }

    /**
     * Método abstrato que deve ser implementado pelas subclasses para retornar o tipo do veículo.
     *
     * @return Tipo do veículo como uma string.
     */
    public abstract String getTipo();

    public AgencyDTO getAgency() {
        return agency;
    }

    /**
     * Retorna uma representação em string do veículo, incluindo todas as suas propriedades.
     *
     * @return String que representa o veículo.
     */
    @Override
    public String toString() {
        return "VehicleDTO{" +
                "placa='" + placa + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", ano=" + ano +
                ", disponivel=" + disponivel +
                ", precoDiaria=" + precoDiaria +
                ", tipo=" + getTipo() +
                '}';
    }
}
