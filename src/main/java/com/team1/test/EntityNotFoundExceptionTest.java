package main.java.com.team1.test;

import main.java.com.team1.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe EntityNotFoundException.
 */
public class EntityNotFoundExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Entity not found";
        EntityNotFoundException exception = new EntityNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage(),
                "A mensagem da exceção deve ser a mesma que foi passada no construtor.");
    }

    @Test
    public void testExceptionInstanceOf() {
        EntityNotFoundException exception = new EntityNotFoundException("Test");

        assertTrue(exception instanceof Exception,
                "EntityNotFoundException deve ser uma subclasse de Exception.");
    }
}