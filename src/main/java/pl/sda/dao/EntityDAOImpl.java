package pl.sda.dao;

/**
 * TODO: Document this class / interface here
 *
 * @since v8.0
 */
public abstract class EntityDAOImpl<K> implements EntityDAO<K> {
    protected final JdbcConnectionManager jdbcConnectionManager;

    protected EntityDAOImpl(JdbcConnectionManager jdbcConnectionManager) {
        this.jdbcConnectionManager = jdbcConnectionManager;
    }

    @Override
    public void delete(int id) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }
}
