package companycalculator.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Abstrakti luokka kaikille Daoille.
 */
public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    T save(T object) throws SQLException;

    T update(T object) throws SQLException;

    void delete(K key) throws SQLException;
}

