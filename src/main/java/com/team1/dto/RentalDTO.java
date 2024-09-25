package main.java.com.team1.dto;

import java.time.LocalDateTime;

/**
 * DTO que contém informações necessárias para o gerenciamento do aluguel.
 * Utiliza o record para simplificar a criação e uso de objetos imutáveis.
 */
public record RentalDTO(String vehiclePlate, String customerDocument, String agencyRentalName,
                        String agencyReturnName, LocalDateTime rentalDate,
                        LocalDateTime returnDate, double totalCost) {}