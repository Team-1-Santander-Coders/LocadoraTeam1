package main.java.com.team1.dao;

import java.util.List;

public interface DataAccessObject<T> {
    void salvar(T entidade);
    void atualizar(T entidade);
    void deletar(T entidade);
    T buscarPorId(String id);
    List<T> listarTodos();
}
