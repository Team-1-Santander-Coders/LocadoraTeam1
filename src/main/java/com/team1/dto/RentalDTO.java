package main.java.com.team1.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import main.java.com.team1.entities.Rental;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.CustomerRepositoryImpl;
import main.java.com.team1.repository.VehicleRepositoryImpl;
import main.java.com.team1.service.VehicleService;
import main.java.com.team1.util.DateUtil;
import main.java.com.team1.util.FileUtil;

public class RentalDTO extends Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public RentalDTO(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate) {
        super(vehicle, customer, agencyRental, rentalDate);
    }

    public RentalDTO(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate, AgencyDTO agencyReturn, LocalDate returnDate) {
        super(vehicle, customer, agencyRental, rentalDate);
        finalizarAluguel(agencyReturn, returnDate);
    }

    public String vehiclePlate(){
        return getVehicle().getPlaca();
    }

    public String customerDocument() {
        CustomerDTO customer = getCustomer();
        if (customer instanceof LegalPersonDTO) {
            return ((LegalPersonDTO) customer).getCnpj();
        } else if (customer instanceof PhysicalPersonDTO) {
            return ((PhysicalPersonDTO) customer).getCpf();
        } else if (customer instanceof UserDTO) {
            return ((UserDTO) customer).getDocument();
        }
        return "Usuário inválido";
    }

    public String agencyRentalName() {
        return getAgencyRental().name();
    }

    public String agencyReturnName() {
        return getAgencyReturn().name();
    }


    public String generateReceipt() {

        return "===== RECIBO DE ALUGUEL =====\n" +
                "Veículo: " + this.vehiclePlate() + " - " + getVehicle().getModelo() + "\n" +
                "Cliente: " + this.customerDocument() + "\n" +
                "Agência de Retirada: " + this.agencyRentalName() + "\n" +
                "Agência de Devolução: " + this.agencyReturnName() + "\n" +
                "Data de Retirada: " + DateUtil.formatarData(getRentalDate()) + "\n" +
                "Data de Devolução: " + DateUtil.formatarData(getReturnDate()) + "\n" +
                "Custo Total: R$ " + String.format("%.2f", this.calcularCustoTotal()) + "\n" +
                "=============================\n";
    }

    public double calcularCustoTotal() {
        VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());
        try {
            long diasAluguel = DateUtil.calcularDiferencaDatas(getRentalDate(), getReturnDate());
            double custoDiario = vehicleService.getVehicleByPlaca(this.vehiclePlate()).getPrecoDiaria();
            if (this.getCustomer().getTipo().equals("Física") && diasAluguel > 5) {
                return (diasAluguel * custoDiario) * 0.95;
            } else if (this.getCustomer().getTipo().equals("Jurídica") && diasAluguel > 3) {
                return (diasAluguel * custoDiario) * 0.90;
            }
            return diasAluguel * custoDiario;
        } catch (EntityNotFoundException e) {
            FileUtil.logError(e);
            return -1;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalDTO rentalDTO)) return false;
        return Objects.equals(vehiclePlate(), rentalDTO.vehiclePlate()) && Objects.equals(customerDocument(), rentalDTO.customerDocument()) && (!this.isReturned() || !((RentalDTO) o).isReturned());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehiclePlate(), customerDocument());
    }

    public String toString() {
        return super.toString();
    }

    public String toJson() {
        return "{\n" +
                "  \"veiculo\": \"" + this.vehiclePlate() + " - " + getVehicle().getModelo() + "\",\n" +
                "  \"cliente\": \"" + this.customerDocument() + "\",\n" +
                "  \"agenciaRetirada\": \"" + this.agencyRentalName() + "\",\n" +
                "  \"agenciaDevolucao\": \"" + (isReturned() ? this.agencyReturnName() : "Não devolvido.") + "\",\n" +
                "  \"dataRetirada\": \"" + DateUtil.formatarData(getRentalDate()) + "\",\n" +
                "  \"dataDevolucao\": \"" + (isReturned() ? DateUtil.formatarData(getReturnDate()) : "Não devolvido.") + "\",\n" +
                "  \"custoTotal\": \"" + (isReturned() ? String.format("%.2f", this.calcularCustoTotal()) : "0") + "\"\n" +
                "  \"situacao\": \"" + (isReturned() ? "Fechado" : "Em aberto") + "\"\n" +
                "}";
    }

}