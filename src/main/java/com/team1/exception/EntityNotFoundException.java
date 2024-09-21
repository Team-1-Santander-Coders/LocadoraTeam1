package main.java.com.team1.exception;

/**
 * Exceção personalizada lançada quando uma entidade não é encontrada
 * durante operações de busca ou manipulação no sistema.
 * <p>
 * Esta exceção é utilizada para sinalizar que uma determinada entidade,
 * como veículos, clientes ou agências, não está presente no sistema.
 * </p>
 */
public class EntityNotFoundException extends Exception {

    /**
     * Construtor para a exceção EntityNotFoundException.
     *
     * @param message A mensagem descritiva que detalha a causa da exceção.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}