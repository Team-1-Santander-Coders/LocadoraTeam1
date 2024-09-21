package main.java.com.team1.util;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    private static final String FORMATO_PADRAO = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMATO_PADRAO);

    /**
     * <p>Valida se a string de data fornecida está no formato correto (dd/MM/yyyy) e
     * se a data é válida, ou seja, se ela realmente existe no calendário.</p>
     *
     * <p>Esse método não apenas verifica o formato da string de data, mas também garante que
     * a data fornecida seja válida no calendário, evitando correções automáticas de datas inválidas
     * como "31/02/2020".</p>
     *
     * <p>Se a data for inválida ou estiver em um formato incorreto, uma exceção será lançada.</p>
     *
     * @param dataString String representando a data que deve ser validada no formato (dd/MM/yyyy).
     * @throws IllegalArgumentException Lança uma exceção se o formato ou a validade da data for incorreto.
     */
    private static void validarFormatoData(String dataString) {
        try {
            LocalDate data = LocalDate.parse(dataString, FORMATTER);

            if (!dataString.equals(data.format(FORMATTER))) {
                throw new IllegalArgumentException("Data inválida! Use o formato: " + FORMATO_PADRAO);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida! Use o formato: " + FORMATO_PADRAO);
        }
    }



    /**
     * <p>Formata um objeto LocalDate para uma string no formato padrão (dd/MM/yyyy).</p>
     *
     * @param data Objeto LocalDate que será formatado.
     * @return String formatada representando a data.
     * @throws IllegalArgumentException Lança uma exceção se a data fornecida for nula.
     */
    public static String formatarData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }
        return data.format(FORMATTER);
    }

    /**
     * <p>Converte uma string de data no formato (dd/MM/yyyy) para um objeto LocalDate.</p>
     *
     * <p>Esse método valida se a string está no formato correto antes de realizar a conversão.
     * Se a string não estiver no formato esperado ou representar uma data inválida, uma exceção
     * IllegalArgumentException será lançada.</p>
     *
     * @param dataString String representando a data a ser convertida.
     * @return Objeto LocalDate correspondente à data fornecida.
     * @throws IllegalArgumentException Lança uma exceção se o formato da data for inválido ou se a data não existir.
     */
    public static LocalDate converterTextoParaData(String dataString) {
        validarFormatoData(dataString);
        return LocalDate.parse(dataString, FORMATTER);
    }

    /**
     * <p>Calcula a diferença em dias entre duas datas fornecidas.</p>
     *
     * @param data1 Primeira data do cálculo.
     * @param data2 Segunda data do cálculo.
     * @return Diferença em dias entre as duas datas, ou -1 em caso de erro.
     * @throws IllegalArgumentException Lança uma exceção se alguma das datas for nula.
     */
    public static long calcularDiferencaDatas(LocalDate data1, LocalDate data2) {
        try {
            if (data1 == null || data2 == null) {
                throw new IllegalArgumentException("As datas não podem ser nulas.");
            }
            return ChronoUnit.DAYS.between(data1, data2);
        } catch (IllegalArgumentException e) {
            FileUtil.logError(e);
            return -1;
        }
    }
}
