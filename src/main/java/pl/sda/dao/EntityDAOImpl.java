package pl.sda.dao;

public abstract class EntityDAOImpl<K> implements EntityDAO<K> {
    protected final JdbcConnectionManager jdbcConnectionManager;

    protected EntityDAOImpl(JdbcConnectionManager jdbcConnectionManager) {
        this.jdbcConnectionManager = jdbcConnectionManager;
    }
}
