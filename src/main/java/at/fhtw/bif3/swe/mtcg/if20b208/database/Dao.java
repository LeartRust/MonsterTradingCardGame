package at.fhtw.bif3.swe.mtcg.if20b208.database;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);

    Collection<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
