package main.java.com.team1.dto;

import java.io.Serial;

/**
 * Classe que representa um Data Transfer Object (DTO) para um caminhão,
 * estendendo a classe {@link VehicleDTO}.
 * <p>
 * Esta classe é utilizada para transferir dados de caminhões entre diferentes
 * camadas da aplicação, encapsulando informações como placa, modelo, marca, ano,
 * disponibilidade e preço diário.
 * </p>
 */
public class TruckDTO extends VehicleDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe TruckDTO.
     *
     * @param placa       A placa do caminhão.
     * @param modelo      O modelo do caminhão.
     * @param marca       A marca do caminhão.
     * @param ano         O ano de fabricação do caminhão.
     * @param disponivel  A disponibilidade do caminhão.
     * @param precoDiaria O preço diário de locação do caminhão.
     */
    public TruckDTO(String placa, String modelo, String marca, int ano, boolean disponivel, double precoDiaria, AgencyDTO agency) {
        super(placa, modelo, marca, ano, disponivel, precoDiaria, agency);
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
