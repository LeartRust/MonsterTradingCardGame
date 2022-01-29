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

public class DatabaseTest {

    @Test
    public void createUser(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        daoDb.saveUser(new UserData("UnitTestUser", "12345678",20, false,100,0,0,0));
        User user = daoDb.getUser("UnitTestUser");
        assertTrue("saveUser and getUser should work", user.getUsername().equals("UnitTestUser"));
    }

    @Test
    public void getCardsFromUserTest(){
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.initDb();
        daoDb.saveUser(new UserData("UnitTestUser", "12345678",20, false,100,0,0,0));
        daoDb.saveCard(new CardData("randomTestId1", "WaterKraken", "Water", "Kraken", 50, "", false ));
        daoDb.saveCard(new CardData("randomTestId2", "FireSpell", "Fire", "Spell", 60, "", false ));
        daoDb.saveCard(new CardData("randomTestId3", "NormalKnight", "Normal", "Knight", 70, "", false ));
        daoDb.saveCard(new CardData("randomTestId4", "WaterGoblin", "Water", "Goblin", 55, "", false ));
        daoDb.addPackage("randomTestId1");
        daoDb.addPackage("randomTestId2");
        daoDb.addPackage("randomTestId3");
        daoDb.addPackage("randomTestId4");

        daoDb.acquirePackage("UnitTestUser");

        List<Card> cards = daoDb.getUserCards("UnitTestUser");
        cards.stream().forEach(element -> System.out.println("Name: " + element.getName()));
        assertTrue("saveUser, saveCard, addPackage and acquirePackage should work", cards.get(0).getName().equals("WaterKraken") );
    }
}
