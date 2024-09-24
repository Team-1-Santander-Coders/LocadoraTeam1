package main.java.com.team1.entities;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.util.DateUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

    // Construtor que inicializa os atributos necessários no início do aluguel
    public Rental(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.agencyRental = agencyRental;
        this.rentalDate = rentalDate;
        this.totalCost = 0;
        this.isReturned = false;
    }

    // Método para finalizar o aluguel e calcular o custo total
    public void finalizarAluguel(AgencyDTO agencyReturn, LocalDate returnDate) {
        this.agencyReturn = agencyReturn;
        this.returnDate = returnDate;
        this.totalCost = calcularCustoTotal();
        this.isReturned = true;
    }

    // Método que calcula o custo total do aluguel
    private double calcularCustoTotal() {
        long diasAluguel = DateUtil.calcularDiferencaDatas(rentalDate, returnDate); // Calcula o número de dias de aluguel
        double custoDiario = vehicle.getPrecoDiaria(); // Pega a taxa diária do veículo

        // Aplicar regra de desconto (5% de desconto para aluguéis de mais de 5 dias)
        if (diasAluguel > 5) {
            return (diasAluguel * custoDiario) * 0.95;
        }
        return diasAluguel * custoDiario;
    }

    // Sobrescrita do método toString para representar o aluguel em formato textual
    @Override
    public String toString() {
        return "Rental{" +
                "vehicle=" + vehicle +
                ", customer=" + customer +
                ", agencyRental=" + agencyRental.name() +
                ", agencyReturn=" + agencyReturn.name() +
                ", rentalDate=" + DateUtil.formatarData(rentalDate) +
                ", returnDate=" + DateUtil.formatarData(returnDate) +
                ", totalCost=" + String.format("R$ %.2f", totalCost) +
                '}';
    }

    // Sobrescrita de equals para comparar aluguéis com base em veículo e cliente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return Objects.equals(vehicle, rental.vehicle) &&
                Objects.equals(customer, rental.customer);
    }

    // Sobrescrita do hashCode para garantir que o hash seja consistente com equals
    @Override
    public int hashCode() {
        return Objects.hash(vehicle, customer);
    }

    public record RentalDTO(
            String vehiclePlate,
            String customerDocument,
            String agencyRentalName,
            String agencyReturnName,
            LocalDateTime rentalDate,
            LocalDateTime returnDate,
            double totalCost
    ) {}

}
