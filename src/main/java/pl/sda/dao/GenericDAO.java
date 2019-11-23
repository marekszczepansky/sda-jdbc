package pl.sda.dao;

import java.util.List;

public interface GenericDAO<K> {
    K findById(int id) throws Exception;

    void create(K department) throws Exception;

    void update(K department) throws Exception;

    void delete(int id) throws Exception;

    void create(List<K> employees) throws Exception;
}
