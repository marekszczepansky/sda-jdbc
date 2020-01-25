package pl.sda.dao;

public interface EntityDAO<K> {
    K findById(int id) throws Exception;

    void create(K entity) throws Exception;

    void update(K entity) throws Exception;

    void delete(int id) throws Exception;
}
