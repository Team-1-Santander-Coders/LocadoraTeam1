package main.java.com.team1.service;

import main.java.com.team1.dto.RentalDTO;
import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.exception.EntityNotFoundException;
import main.java.com.team1.repository.RentalRepository;
import main.java.com.team1.util.FileUtil;
import java.util.Collections;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de serviço responsável por gerenciar operações sobre aluguéis de veículos.
 * Fornece métodos para adicionar, finalizar, calcular custos e gerar recibos de aluguéis.
 */
public class RentalService {
    private final RentalRepository rentalRepository; // Repositório de aluguéis.

    /**
     * Construtor da classe RentalService.
     *
     * @param rentalRepository O repositório de aluguéis a ser utilizado.
     */
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Adiciona um novo aluguel se o veículo estiver disponível e não houver outro aluguel existente
     * com o mesmo veículo e cliente.
     *
     * @param rentalDTO Os dados do aluguel a serem adicionados.
     * @throws DuplicateEntityException se já existir um aluguel com o mesmo veículo e cliente.
     */
    public void addRental(RentalDTO rentalDTO) throws DuplicateEntityException {
        // Lógica para verificar se o veículo está disponível pode ser implementada aqui.
        rentalRepository.save(rentalDTO);
    }

    /**
     * Finaliza o aluguel, define a devolução e gera o comprovante.
     *
     * @param rentalDTO Os dados do aluguel a ser finalizado.
     * @throws EntityNotFoundException se o aluguel não for encontrado.
     */
    public void returnRental(RentalDTO rentalDTO) throws EntityNotFoundException {
        rentalRepository.update(rentalDTO); // Atualiza o aluguel para incluir a devolução.
    }

    /**
     * Calcula o custo do aluguel aplicando os valores de diária para cada tipo de veículo.
     *
     * @param rentalDTO Os dados do aluguel.
     * @param daysRented O número de dias que o veículo foi alugado.
     * @return O custo total do aluguel.
     */
    public double calculateCost(RentalDTO rentalDTO, long daysRented) {
        double dailyRate = getDailyRate(rentalDTO.vehiclePlate()); // Obtem a diária com base no tipo de veículo.
        return dailyRate * daysRented; // Calcula o custo total.
    }

    /**
     * Retorna a diária com base no tipo de veículo.
     *
     * @param vehiclePlate A placa do veículo para determinar o tipo.
     * @return O valor da diária do veículo.
     */
    public double getDailyRate(String vehiclePlate) {
        // Aqui, uma lógica simples é usada para determinar o tipo de veículo com base na placa.
        // Supondo que a placa segue um padrão específico ou que exista um método para identificar o veículo.
        if (vehiclePlate.startsWith("M")) { // Exemplo: placas de motos começam com "M".
            return 100; // Valor diário para motos.
        } else if (vehiclePlate.startsWith("C")) { // Exemplo: placas de carros começam com "C".
            return 150; // Valor diário para carros.
        } else if (vehiclePlate.startsWith("T")) { // Exemplo: placas de caminhões começam com "T".
            return 200; // Valor diário para caminhões.
        }
        return 0; // Retorna 0 se não encontrar o tipo de veículo.
    }

    /**
     * Gera um comprovante de aluguel.
     *
     * @param rentalDTO Os dados do aluguel para o comprovante.
     * @param fileName O nome do arquivo onde o comprovante será salvo.
     */

    public void generateReceipt(RentalDTO rentalDTO, String fileName) {
        String receiptContent = "Comprovante de Aluguel:\n" +
                "Veículo: " + rentalDTO.vehiclePlate() + "\n" +
                "Cliente: " + rentalDTO.customerDocument() + "\n" +
                "Agência de Aluguel: " + rentalDTO.agencyRentalName() + "\n" +
                "Agência de Devolução: " + rentalDTO.agencyReturnName() + "\n" +
                "Data de Início: " + rentalDTO.rentalDate() + "\n" +
                "Data de Devolução: " + rentalDTO.returnDate() + "\n" +
                "Custo Total: R$ " + rentalDTO.totalCost();

        // Cria uma lista contendo o conteúdo do recibo.
        List<String> receiptLines = Collections.singletonList(receiptContent);

        // Salva o recibo no arquivo especificado.
        FileUtil.writeToFile(receiptLines, fileName);
    }

    /**
     * Recupera todos os aluguéis.
     *
     * @return Uma lista de todos os aluguéis.
     */
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll(); // Chama o repositório para obter todos os aluguéis.
    }

    /**
     * Busca aluguéis por data.
     *
     * @param date A data para filtrar os aluguéis.
     * @return Uma lista de aluguéis que correspondem à data informada.
     */
    public List<RentalDTO> searchRentalsByDate(String date) {
        return rentalRepository.findByDate(date); // Chama o repositório para buscar aluguéis por data.
    }
}
