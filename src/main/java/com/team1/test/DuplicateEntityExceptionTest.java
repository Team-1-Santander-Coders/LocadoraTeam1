package main.java.com.team1.test;

import main.java.com.team1.exception.DuplicateEntityException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe DuplicateEntityException.
 */
public class DuplicateEntityExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Entity already exists";
        DuplicateEntityException exception = new DuplicateEntityException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage(),
                "A mensagem da exceção deve ser a mesma que foi passada no construtor.");
    }

    @Test
    public void testExceptionInstanceOf() {
        DuplicateEntityException exception = new DuplicateEntityException("Test");

        assertTrue(exception instanceof Exception,
                "DuplicateEntityException deve ser uma subclasse de Exception.");
    }
}
