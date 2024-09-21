package main.java.com.team1.test;
import main.java.com.team1.util.DateUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class DateUtilTest {
    @Test
    public void testFormatarData() {
        LocalDate data = LocalDate.of(2000, 1, 1);
        String dataFormatada = DateUtil.formatarData(data);
        assertEquals("01/01/2000", dataFormatada, "A formatação da data falhou!");
    }

    @Test
    public void testConverterTextoParaData() {
        String dataString = "01/01/2000";
        LocalDate data = DateUtil.converterTextoParaData(dataString);
        assertEquals(LocalDate.of(2000, 1, 1), data, "A conversão de texto para LocalDate falhou!");
    }

    @Test
    public void testCalcularDiferencaDatas() {
        LocalDate data1 = LocalDate.of(2023, 9, 1);
        LocalDate data2 = LocalDate.of(2023, 9, 10);
        long diferenca = DateUtil.calcularDiferencaDatas(data1, data2);
        assertEquals(9, diferenca, "O cálculo da diferença entre datas falhou!");
    }

    @Test
    public void testValidarFormatoDataInvalido() {
        String dataString = "31/02/2020";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.converterTextoParaData(dataString);
        });

        assertEquals("Data inválida! Use o formato: dd/MM/yyyy", exception.getMessage());
    }

    @Test
    public void testDataNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.formatarData(null);
        });
        assertEquals("A data não pode ser nula.", exception.getMessage());
    }
}
