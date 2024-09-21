package main.java.com.team1.exception;

/**
 * Exceção personalizada lançada quando ocorre uma tentativa de duplicar
 * uma entidade em operações de cadastro ou atualização.
 * <p>
 * Esta exceção é utilizada para garantir que entidades únicas, como veículos,
 * clientes ou agências, não sejam cadastradas mais de uma vez no sistema.
 * </p>
 */
public class DuplicateEntityException extends Exception {
    /**
     * Construtor para a exceção DuplicateEntityException.
     *
     * @param message A mensagem descritiva que detalha a causa da exceção.
     */
    public DuplicateEntityException(String message) {
        super(message);
    }
}

