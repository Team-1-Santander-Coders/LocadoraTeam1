package main.java.com.team1.dto;

import java.io.Serial;

/**
 * Classe que representa um Data Transfer Object (DTO) para um carro,
 * estendendo a classe {@link VehicleDTO}.
 * <p>
 * Esta classe é utilizada para transferir dados de carros entre diferentes
 * camadas da aplicação, encapsulando informações como placa, modelo, marca, ano,
 * disponibilidade e preço diário.
 * </p>
 */
public class CarDTO extends VehicleDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe CarDTO.
     *
     * @param placa       A placa do carro.
     * @param modelo      O modelo do carro.
     * @param marca       A marca do carro.
     * @param ano         O ano de fabricação do carro.
     * @param disponivel  A disponibilidade do carro.
     * @param precoDiaria O preço diário de locação do carro.
     */
    public CarDTO(String placa, String modelo, String marca, int ano, boolean disponivel, double precoDiaria, AgencyDTO agency) {
        super(placa, modelo, marca, ano, disponivel, precoDiaria, agency);
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
}
