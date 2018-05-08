package companycalculator.dao;

import java.util.List;

/**
 * Abstrakti luokka kaikille Daoille.
 */
public interface Dao<T, K> {

    T findOne(K key);

    List<T> findAll();

    T save(T object);

    T update(T object);

    void delete(K key);
}

