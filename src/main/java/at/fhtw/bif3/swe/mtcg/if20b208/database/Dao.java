package at.fhtw.bif3.swe.mtcg.if20b208.database;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T, M, S> {

    //User
    Optional<T> getUser(int id);

    Collection<T> getAllUsers();

    void saveUser(T t);

    void updateUser(T t, String[] params);

    void deleteUser(T t);

    //Monster Cards
    void getMonsterCard(M m);

    void getAllMonsterCards(M m);

    void saveMonsterCard(M m);

    void updateMonsterCard(M m, String[] params);

    void deleteMonsterCard(M m);

    //Spell Cards
    void getSpellCard(S s);

    void getAllSpellCards(S s);

    void saveSpellCard(S s);

    void updateSpellCard(S s, String[] params);

    void deleteSpellCard(S s);
}
