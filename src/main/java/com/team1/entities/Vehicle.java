package main.java.com.team1.entities;

import java.io.Serial;
import java.io.Serializable;

/**
 * Interface que define a estrutura de um veículo.
 * <p>
 * Esta interface é usada para representar as propriedades básicas de um veículo,
 * permitindo que diferentes implementações forneçam dados específicos.
 * </p>
 */
public interface Vehicle extends Serializable {
    /**
     * Retorna a placa do veículo.
     *
     * @return uma string representando a placa do veículo.
     */
    String getPlaca();

    /**
     * Retorna o modelo do veículo.
     *
     * @return uma string representando o modelo do veículo.
     */
    String getModelo();

    /**
     * Retorna a marca do veículo.
     *
     * @return uma string representando a marca do veículo.
     */
    String getMarca();

    /**
     * Retorna o ano de fabricação do veículo.
     *
     * @return um inteiro representando o ano do veículo.
     */
    int getAno();

    /**
     * Verifica se o veículo está disponível para locação.
     *
     * @return true se o veículo estiver disponível, false caso contrário.
     */
    boolean isDisponivel();

    /**
     * Retorna o preço diário de locação do veículo.
     *
     * @return um double representando o preço diário.
     */
    double getPrecoDiaria();

    /**
     * Retorna o tipo do veículo (ex.: carro, moto, etc.).
     *
     * @return uma string representando o tipo do veículo.
     */
    String getTipo();
}
