package at.fhtw.bif3.swe.mtcg.if20b208.database;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.ElementType;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.MonsterType;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T, C> {

    //User
    User getUser(String username);

    //Collection<T> getAllUsers();

    void saveUser(T t);

    void updateUser(T t, String[] params);

    //void deleteUser(T t);

    //Cards
    void getCard(C c);

    //Collection<C> getAllCards();

    void saveCard(C c);

    void updateCard(C c, String[] params);

    void deleteCard(C c);


}
