package main.java.com.team1.dto;

import java.io.Serial;

/**
 * Classe que representa um Data Transfer Object (DTO) para uma motocicleta,
 * estendendo a classe {@link VehicleDTO}.
 * <p>
 * Esta classe é utilizada para transferir dados de motocicletas entre diferentes
 * camadas da aplicação, encapsulando informações como placa, modelo, marca, ano,
 * disponibilidade e preço diário.
 * </p>
 */
public class MotorcycleDTO extends VehicleDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe MotorcycleDTO.
     *
     * @param placa       A placa da motocicleta.
     * @param modelo      O modelo da motocicleta.
     * @param marca       A marca da motocicleta.
     * @param ano         O ano de fabricação da motocicleta.
     * @param disponivel  A disponibilidade da motocicleta.
     * @param precoDiaria O preço diário de locação da motocicleta.
     */
    public MotorcycleDTO(String placa, String modelo, String marca, int ano, boolean disponivel, double precoDiaria) {
        super(placa, modelo, marca, ano, disponivel, precoDiaria);
    }

    /**
     * Retorna o tipo do veículo como uma string.
     *
     * @return Tipo do veículo ("Moto").
     */
    @Override
    public String getTipo() {
        return "Moto";
    }
}
