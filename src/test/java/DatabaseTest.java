import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.database.MTCGDaoDb;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.CardData;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.UserData;
import at.fhtw.bif3.swe.mtcg.if20b208.server.Helper;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    public void createUser(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        daoDb.saveUser(new UserData("UnitTestUserCreateUserTest", "12345678",20, false,100,0,0,0));
        User user = daoDb.getUser("UnitTestUserCreateUserTest");
        assertTrue("saveUser and getUser should work", user.getUsername().equals("UnitTestUserCreateUserTest"));
    }

    @Test
    public void getCardsFromUserTest(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        //create user
        daoDb.saveUser(new UserData("UnitTestUserGetCardsTest", "12345678",20, false,100,0,0,0));
        //create cards
        daoDb.saveCard(new CardData("randomTestId1", "WaterKraken", "Water", "Kraken", 50, "", false ));
        daoDb.saveCard(new CardData("randomTestId2", "FireSpell", "Fire", "Spell", 60, "", false ));
        daoDb.saveCard(new CardData("randomTestId3", "NormalKnight", "Normal", "Knight", 70, "", false ));
        daoDb.saveCard(new CardData("randomTestId4", "WaterGoblin", "Water", "Goblin", 55, "", false ));
        //create package
        daoDb.addPackage("randomTestId1");
        daoDb.addPackage("randomTestId2");
        daoDb.addPackage("randomTestId3");
        daoDb.addPackage("randomTestId4");
        //acquire package
        daoDb.acquirePackage("UnitTestUserGetCardsTest");
        //get cards
        List<Card> cards = daoDb.getUserCards("UnitTestUserGetCardsTest");
        cards.stream().forEach(element -> System.out.println("Name: " + element.getName()));
        //check if cards
        //assertTrue("saveUser, saveCard, addPackage and acquirePackage should work", cards.get(0).getName().equals("WaterKraken") );
        assertAll("Check cards",
                () -> assertEquals(cards.get(0).getName(), "WaterKraken", "Name should be WaterKraken"),
                () -> assertEquals(cards.get(1).getName(), "FireSpell", "ame should be FireSpell"),
                () -> assertEquals(cards.get(2).getName(), "NormalKnight", "ame should be NormalKnight"),
                () -> assertEquals(cards.get(3).getName(), "WaterGoblin", "ame should be WaterGoblin")
        );
    }

    @Test
    public void loggedInTest(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        daoDb.saveUser(new UserData("UnitTestUserLoggedInTest", "12345678",20, false,100,0,0,0));

        User user = daoDb.getUser("UnitTestUserLoggedInTest");
        daoDb.logInOutUser(true, user.getUsername(), user.getPassword());
        assertTrue("User login Status should be true, saveUser and getUser should work too", daoDb.isLogedIn(user.getUsername()));
    }

    @Test
    public void loggedOutTest(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        daoDb.saveUser(new UserData("UnitTestUserLoggedOutTest", "12345678",20, false,100,0,0,0));
        User user = daoDb.getUser("UnitTestUserLoggedOutTest");
        assertFalse("User login Status should be true, saveUser and getUser should work too", daoDb.isLogedIn(user.getUsername()));
    }

    @Test
    public void coinsTest(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        daoDb.saveUser(new UserData("UnitTestUserCoinsTest1", "12345678",20, false,100,0,0,0));
        daoDb.saveUser(new UserData("UnitTestUserCoinsTest2", "12345678",20, false,100,0,0,0));
        daoDb.addCoins("UnitTestUserCoinsTest1");
        daoDb.subtractCoins("UnitTestUserCoinsTest2");

        User user1 = daoDb.getUser("UnitTestUserCoinsTest1");
        User user2 = daoDb.getUser("UnitTestUserCoinsTest2");

        assertTrue("user1 should have 25 coins and user2 should have 15 coins", user1.getCoins() == 25 && user2.getCoins() == 15);
    }

}
