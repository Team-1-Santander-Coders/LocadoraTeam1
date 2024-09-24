package main.java.com.team1.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Rental implements Serializable {

    private Vehicle vehicle;          // Veículo alugado
    private Customer customer;        // Cliente que aluga o veículo
    private Agency agencyRental;      // Agência de onde o veículo foi retirado
    private Agency agencyReturn;      // Agência de devolução do veículo
    private LocalDateTime rentalDate; // Data e hora de início do aluguel
    private LocalDateTime returnDate; // Data e hora de devolução
    private double totalCost;         // Custo total do aluguel

    // Construtor que inicializa os atributos necessários no início do aluguel
    public Rental(Vehicle vehicle, Customer customer, Agency agencyRental, LocalDateTime rentalDate) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.agencyRental = agencyRental;
        this.rentalDate = rentalDate;
        this.totalCost = 0.0; // Inicializa o custo total como 0 até o aluguel ser finalizado
    }

    // Método para finalizar o aluguel e calcular o custo total
    public void finalizarAluguel(Agency agencyReturn, LocalDateTime returnDate) {
        this.agencyReturn = agencyReturn;
        this.returnDate = returnDate;
        this.totalCost = calcularCustoTotal();
    }

    // Método que calcula o custo total do aluguel
    private double calcularCustoTotal() {
        long diasAluguel = ChronoUnit.DAYS.between(rentalDate, returnDate); // Calcula o número de dias de aluguel
        double custoDiario = vehicle.getDailyRate(); // Pega a taxa diária do veículo

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
                ", agencyRental=" + agencyRental +
                ", agencyReturn=" + agencyReturn +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", totalCost=" + totalCost +
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
