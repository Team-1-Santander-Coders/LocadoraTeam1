package main.java.com.team1.entities;

import main.java.com.team1.dto.AgencyDTO;
import main.java.com.team1.dto.CustomerDTO;
import main.java.com.team1.dto.VehicleDTO;
import main.java.com.team1.util.DateUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

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

    public Rental(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.agencyRental = agencyRental;
        this.rentalDate = rentalDate;
        this.totalCost = 0;
        this.isReturned = false;
    }

    public void finalizarAluguel(AgencyDTO agencyReturn, LocalDate returnDate) {
        this.agencyReturn = agencyReturn;
        this.returnDate = returnDate;
        this.totalCost = calcularCustoTotal();
        this.isReturned = true;
    }

    private double calcularCustoTotal() {
        long diasAluguel = DateUtil.calcularDiferencaDatas(rentalDate, returnDate);
        double custoDiario = vehicle.getPrecoDiaria();

        if (diasAluguel > 5) {
            return (diasAluguel * custoDiario) * 0.95;
        }
        return diasAluguel * custoDiario;
    }

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

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public AgencyDTO getAgencyRental() {
        return agencyRental;
    }

    public AgencyDTO getAgencyReturn() {
        return agencyReturn;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }
}
