package pl.sda.dao;

import pl.sda.domain.Department;

/**
 * TODO: Document this class / interface here
 *
 * @since v8.0
 */
public interface EntityDAO<K> {
    K findById(int id) throws Exception;

    void create(K entity) throws Exception;

    void update(K entity) throws Exception;

    void delete(int id) throws Exception;
}
