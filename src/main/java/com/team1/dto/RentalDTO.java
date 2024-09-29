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

/**
 * Classe que representa um Data Transfer Object (DTO) para um aluguel,
 * estendendo a classe {@link Rental}.
 * <p>
 * Esta classe encapsula os dados relacionados ao aluguel de veículos,
 * incluindo informações sobre o veículo alugado, o cliente que realizou o
 * aluguel, a agência de retirada e devolução, e as datas correspondentes.
 * </p>
 * <p>
 * Além disso, inclui métodos utilitários para geração de recibos, cálculo de custo total
 * do aluguel, e formatação de dados para exibição.
 * </p>
 * <p>
 * A classe implementa {@link Serializable} para que os objetos possam ser
 * serializados e transportados entre diferentes camadas da aplicação.
 * </p>
 */

public class RentalDTO extends Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor para criar um {@code RentalDTO} com os dados do veículo, cliente,
     * agência de retirada e data de aluguel.
     *
     * @param vehicle O veículo que está sendo alugado.
     * @param customer O cliente que está realizando o aluguel.
     * @param agencyRental A agência onde o veículo será retirado.
     * @param rentalDate A data de retirada do veículo.
     */

    public RentalDTO(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate) {
        super(vehicle, customer, agencyRental, rentalDate);
    }

    /**
     * Construtor para criar um {@code RentalDTO} com os dados do veículo, cliente,
     * agência de retirada, data de aluguel, agência de devolução e data de devolução.
     * O aluguel é finalizado automaticamente ao chamar este construtor.
     *
     * @param vehicle O veículo que está sendo alugado.
     * @param customer O cliente que está realizando o aluguel.
     * @param agencyRental A agência onde o veículo será retirado.
     * @param rentalDate A data de retirada do veículo.
     * @param agencyReturn A agência onde o veículo será devolvido.
     * @param returnDate A data de devolução do veículo.
     */
    public RentalDTO(VehicleDTO vehicle, CustomerDTO customer, AgencyDTO agencyRental, LocalDate rentalDate, AgencyDTO agencyReturn, LocalDate returnDate) {
        super(vehicle, customer, agencyRental, rentalDate);
        finalizarAluguel(agencyReturn, returnDate);
    }

    /**
     * Retorna a placa do veículo associado a este aluguel.
     *
     * @return A placa do veículo.
     */
    public String vehiclePlate(){
        return getVehicle().getPlaca();
    }

    /**
     * Retorna o documento do cliente associado a este aluguel.
     * <p>
     * Se o cliente for uma pessoa jurídica, retorna o CNPJ; se for pessoa física, retorna o CPF.
     * </p>
     *
     * @return O documento do cliente.
     */
    public String customerDocument() {
        return getCustomer().getDocument();
    }

    /**
     * Retorna o nome da agência onde o veículo foi retirado.
     *
     * @return O nome da agência de retirada.
     */

    public String agencyRentalName() {
        return getAgencyRental().name();
    }

    /**
     * Retorna o nome da agência onde o veículo foi devolvido.
     *
     * @return O nome da agência de devolução.
     */
    public String agencyReturnName() {
        return getAgencyReturn().name();
    }

    /**
     * Gera um recibo detalhado do aluguel, contendo informações como a placa do veículo,
     * o documento do cliente, as agências de retirada e devolução, e o custo total do aluguel.
     *
     * @return Uma string formatada representando o recibo do aluguel.
     */

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

    /**
     * Calcula o custo total do aluguel com base no número de dias e no tipo de cliente.
     * <p>
     * Descontos são aplicados para pessoas físicas que alugam por mais de 5 dias
     * e para pessoas jurídicas que alugam por mais de 3 dias.
     * </p>
     *
     * @return O custo total do aluguel ou -1 em caso de erro ao calcular.
     */

    public double calcularCustoTotal() {
        VehicleService vehicleService = new VehicleService(new VehicleRepositoryImpl());
        CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();
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

    /**
     * Verifica se este {@code RentalDTO} é igual a outro objeto.
     * <p>
     * Dois {@code RentalDTO}s são considerados iguais se têm a mesma placa de veículo
     * e o mesmo documento de cliente.
     * </p>
     *
     * @param o o objeto a ser comparado com este {@code RentalDTO}.
     * @return {@code true} se os objetos forem iguais, {@code false} caso contrário.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalDTO rentalDTO)) return false;
        if (this.isReturned()) return false;
        return Objects.equals(vehiclePlate(), rentalDTO.vehiclePlate()) && Objects.equals(customerDocument(), rentalDTO.customerDocument());
    }

    /**
     * Gera um código hash para este {@code RentalDTO} com base na placa do veículo e no documento do cliente.
     *
     * @return O valor do código hash para este {@code RentalDTO}.
     */

    @Override
    public int hashCode() {
        return Objects.hash(vehiclePlate(), customerDocument());
    }

    /**
     * Retorna uma representação textual deste {@code RentalDTO}, herdando o comportamento da classe {@link Rental}.
     *
     * @return uma string representando este {@code RentalDTO}.
     */

    public String toString() {
        return super.toString();
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"veiculo\": \"").append(this.vehiclePlate()).append(" - ").append(getVehicle().getModelo()).append("\",\n");
        json.append("  \"cliente\": \"").append(this.customerDocument()).append(" - ").append(this.getCustomer().getName()).append(" - ").append(this.getCustomer().getEmail()).append("\",\n");
        json.append("  \"agenciaRetirada\": \"").append(this.agencyRentalName()).append(" - ").append(this.getAgencyRental().address()).append("\",\n");

        if (!isReturned()) {
            json.append("  \"agenciaDevolucao\": \"Não devolvido.\",\n");
            json.append("  \"dataRetirada\": \"").append(DateUtil.formatarData(getRentalDate())).append("\",\n");
            json.append("  \"dataDevolucao\": \"Não devolvido.\",\n");
            json.append("  \"custoTotal\": \"0\",\n");
            json.append("  \"situacao\": \"Em aberto.\"\n");
        } else {
            json.append("  \"agenciaDevolucao\": \"").append(this.agencyReturnName()).append(" - ").append(this.getAgencyRental().address()).append("\",\n");
            json.append("  \"dataRetirada\": \"").append(DateUtil.formatarData(getRentalDate())).append("\",\n");
            json.append("  \"dataDevolucao\": \"").append(DateUtil.formatarData(getReturnDate())).append("\",\n");
            json.append("  \"custoTotal\": \"").append(String.format("%.2f", this.calcularCustoTotal())).append("\",\n");
            json.append("  \"situacao\": \"Devolvido.\"\n");
        }

        json.append("}");
        return json.toString();
    }
}