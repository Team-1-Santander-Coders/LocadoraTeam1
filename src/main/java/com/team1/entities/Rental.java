package main.java.com.team1.entities;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.util.DateUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entidade que representa o aluguel de um veículo.
 * Implementa a interface {@link java.io.Serializable}, para garantir a serialização dos objetos.
 *
 * <p>
 * Tem como propriedades: {@link main.java.com.team1.dto.VehicleDTO},
 * {@link main.java.com.team1.dto.CustomerDTO},
 * {@link main.java.com.team1.dto.AgencyDTO}, <code>rentalDate</code>, <code>returnDate</code>, <code>isReturned</code> e <code>totaCost</code>.
 * */
public class Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final VehicleDTO vehicle;
    private final CustomerDTO customer;
    private final AgencyDTO agencyRental;
    private AgencyDTO agencyReturn;
    private final LocalDate rentalDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private double totalCost;

    /**
     * Construtor do objeto Rental
     * @param vehicle Veículo alugado.
     * @param customer Cliente que fez o aluguel.
     * @param agencyRental Agência em que o veículo se encontra.
     * @param rentalDate Data de retirada do veículo da agência.
     */
    public Rental(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.agencyRental = agencyRental;
        this.rentalDate = rentalDate;
        this.totalCost = 0;
        this.isReturned = false;
    }

    /**
     * Método para finalizar o aluguel, definindo a propriedade isReturned como true.
     * @param agencyReturn Agência de devolução.
     * @param returnDate Data de devolução.
     */
    public void finalizarAluguel(AgencyDTO agencyReturn, LocalDate returnDate) {
        this.agencyReturn = agencyReturn;
        this.returnDate = returnDate;
        this.totalCost = calcularCustoTotal();
        this.isReturned = true;
    }

    /**
     * Calcula o custo total do aluguel, usando como base as propriedades <code>rentalDate</code> e <code>returnDate</code> do objeto.
     * @return custo total do aluguel.
     */
    private double calcularCustoTotal() {
        long diasAluguel = DateUtil.calcularDiferencaDatas(rentalDate, returnDate);
        double custoDiario = vehicle.getPrecoDiaria();

        if (diasAluguel > 5) {
            return (diasAluguel * custoDiario) * 0.95;
        }
        return diasAluguel * custoDiario;
    }

    /**
     * Método que garante a visualização de todas as propriedades do objeto.
     * @return visualização das propriedades do objeto.
     */
    @Override
    public String toString() {
        if (isReturned) {
            return "Rental{" +
                    "vehicle=" + vehicle +
                    ", customer=" + customer +
                    ", agencyRental=" + agencyRental +
                    ", agencyReturn=" + agencyReturn +
                    ", rentalDate=" + DateUtil.formatarData(rentalDate) +
                    ", returnDate=" + DateUtil.formatarData(returnDate) +
                    ", isReturned=" + isReturned +
                    ", totalCost=" + totalCost +
                    '}';
        }
        return "Rental{" +
                "vehicle=" + vehicle +
                ", customer=" + customer +
                ", agencyRental=" + agencyRental +
                ", rentalDate=" + DateUtil.formatarData(rentalDate) +
                ", isReturned=" + isReturned +
                ", totalCost=" + totalCost +
                '}';
    }

    /**
     * Retorna o veículo que foi alugado.
     * @return veículo alugado.
     */
    public VehicleDTO getVehicle() {
        return vehicle;
    }

    /**
     * Retorna o cliente que fez o aluguel.
     * @return cliente que fez o aluguel.
     */
    public CustomerDTO getCustomer() {
        return customer;
    }

    /**
     * Retorna a agência em que o veículo alugado se encontra.
     * @return agência onde o carro foi alugado.
     */
    public AgencyDTO getAgencyRental() {
        return agencyRental;
    }

    /**
     * Retorna a agência em que o veículo foi devolvido.
     * @return agência de devolução do veículo.
     */
    public AgencyDTO getAgencyReturn() {
        return agencyReturn;
    }

    /**
     * Retorna a data de retirada do veículo.
     * @return data de retirada do veículo.
     */
    public LocalDate getRentalDate() {
        return rentalDate;
    }

    /**
     * Retorna a data de devolução do veículo.
     * @return data de devolução do veículo.
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Retorna se o veículo já foi devolvido ou não.
     * @return status d edevolução do veículo.
     */
    public boolean isReturned() {
        return isReturned;
    }
}
