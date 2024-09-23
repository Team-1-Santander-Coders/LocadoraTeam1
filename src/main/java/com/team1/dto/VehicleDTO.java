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

    public static VehicleDTO fromJson(String json) {
        String[] keyValuePairs = json.replaceAll("[{}\"]", "").split(",");

        String placa = "", modelo = "", marca = "";
        int ano = 0;
        boolean disponivel = false;
        double precoDiaria = 0.0;
        String tipo = "";

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case "placa":
                    placa = value;
                    break;
                case "modelo":
                    modelo = value;
                    break;
                case "marca":
                    marca = value;
                    break;
                case "ano":
                    ano = Integer.parseInt(value);
                    break;
                case "disponivel":
                    disponivel = Boolean.parseBoolean(value);
                    break;
                case "precoDiaria":
                    precoDiaria = Double.parseDouble(value);
                    break;
                case "tipo":
                    tipo = value;
                    break;
            }
        }

        // Instancia o veículo com base no tipo
        return createVehicleByType(placa, modelo, marca, ano, disponivel, precoDiaria, tipo);
    }



    private static VehicleDTO createVehicleByType(String placa, String modelo, String marca, int ano,
                                                  boolean disponivel, double precoDiaria, String tipo) {
        switch (tipo) {
            case "Carro" -> {
                return new CarDTO(placa, modelo, marca, ano, disponivel, precoDiaria);
            }
            case "Moto" -> {
                return new MotorcycleDTO(placa, modelo, marca, ano, disponivel, precoDiaria);
            }
            case "Caminhão" -> {
                return new TruckDTO(placa, modelo, marca, ano, disponivel, precoDiaria);
            }
            default -> throw new IllegalArgumentException("Tipo de veículo desconhecido: " + tipo);
        }
    }
}
