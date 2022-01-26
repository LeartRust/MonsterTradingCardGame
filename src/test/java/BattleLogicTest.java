import at.fhtw.bif3.swe.mtcg.if20b208.BattleLogic;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BattleLogicTest {

    @Test
    public void compareSameElement(){
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("FireDragon", ElementType.FIRE, 70, MonsterType.DRAGON);
        assertTrue("Both Monsters have the same ", monster1.getMonsterType() ==  monster2.getMonsterType());
    }

    @Test
    public void checkIfSpecialityDragonGoblin(){
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("FireGoblin", ElementType.FIRE, 15, MonsterType.GOBLIN);
        User user1 = new User("TestUser1", "testPassword", 0,0,0,0,0);
        User user2 = new User("TestUser2", "testPassword", 0,0,0,0,0);
        ArrayList<Card> deck1 = new ArrayList<Card>();
        deck1.add(monster1);
        ArrayList<Card> deck2 = new ArrayList<Card>();
        deck2.add(monster2);
        BattleLogic battleLogic = new BattleLogic(user1, user2, deck1, deck2);
        assertTrue("this is a speciality", battleLogic.checkSpecialities(monster1, monster2,0,0));
    }

    @Test
    public void checkIfSpecialityWaterSpellKnight(){
        MonsterCard monster1 = new MonsterCard("NormalKnight", ElementType.NORMAL, 15, MonsterType.KNIGHT);
        SpellCard spell = new SpellCard("WaterSpell", ElementType.WATER, 65);
        User user1 = new User("TestUser1", "testPassword", 0,0,0,0,0);
        User user2 = new User("TestUser2", "testPassword", 0,0,0,0,0);
        ArrayList<Card> deck1 = new ArrayList<Card>();
        deck1.add(monster1);
        ArrayList<Card> deck2 = new ArrayList<Card>();
        deck2.add(spell);
        BattleLogic battleLogic = new BattleLogic(user1, user2, deck1, deck2);
        assertTrue("this is a speciality", battleLogic.checkSpecialities(monster1, spell,0,0));
    }

    @Test
    public void checkIfSpecialityWizardOrk(){
        MonsterCard monster1 = new MonsterCard("WaterWizard", ElementType.WATER, 15, MonsterType.WIZARD);
        MonsterCard monster2 = new MonsterCard("NormalOrk", ElementType.NORMAL, 15, MonsterType.ORK);
        User user1 = new User("TestUser1", "testPassword", 0,0,0,0,0);
        User user2 = new User("TestUser2", "testPassword", 0,0,0,0,0);
        ArrayList<Card> deck1 = new ArrayList<Card>();
        deck1.add(monster1);
        ArrayList<Card> deck2 = new ArrayList<Card>();
        deck2.add(monster2);
        BattleLogic battleLogic = new BattleLogic(user1, user2, deck1, deck2);
        assertTrue("this is a speciality", battleLogic.checkSpecialities(monster1, monster2,0,0));
    }

    @Test
    public void checkIfSpecialityDragonElf(){
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("FireElves", ElementType.FIRE, 15, MonsterType.ELF);
        SpellCard spell = new SpellCard("WaterSpell", ElementType.WATER, 65);
        User user1 = new User("TestUser1", "testPassword", 0,0,0,0,0);
        User user2 = new User("TestUser2", "testPassword", 0,0,0,0,0);
        ArrayList<Card> deck1 = new ArrayList<Card>();
        deck1.add(monster1);
        ArrayList<Card> deck2 = new ArrayList<Card>();
        deck2.add(monster2);
        BattleLogic battleLogic = new BattleLogic(user1, user2, deck1, deck2);
        assertTrue("this is a speciality", battleLogic.checkSpecialities(monster1, monster2,0,0));
    }

    @Test
    public void checkIfSpecialityKrakenSpell(){
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.WATER, 15, MonsterType.KRAKEN);
        SpellCard spell = new SpellCard("WaterSpell", ElementType.WATER, 65);
        User user1 = new User("TestUser1", "testPassword", 0,0,0,0,0);
        User user2 = new User("TestUser2", "testPassword", 0,0,0,0,0);
        ArrayList<Card> deck1 = new ArrayList<Card>();
        deck1.add(monster1);
        ArrayList<Card> deck2 = new ArrayList<Card>();
        deck2.add(spell);
        BattleLogic battleLogic = new BattleLogic(user1, user2, deck1, deck2);
        assertTrue("this is a speciality", battleLogic.checkSpecialities(monster1, spell,0,0));
    }

    @Test
    public void checkIfNoSpeciality(){
        MonsterCard monster1 = new MonsterCard("WaterDragon", ElementType.WATER, 15, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("WaterDragon", ElementType.WATER, 15, MonsterType.DRAGON);
        User user1 = new User("TestUser1", "testPassword", 0,0,0,0,0);
        User user2 = new User("TestUser2", "testPassword", 0,0,0,0,0);
        ArrayList<Card> deck1 = new ArrayList<Card>();
        deck1.add(monster1);
        ArrayList<Card> deck2 = new ArrayList<Card>();
        deck2.add(monster2);
        BattleLogic battleLogic = new BattleLogic(user1, user2, deck1, deck2);
        assertFalse("this is not a speciality", battleLogic.checkSpecialities(monster1, monster2,0,0));
    }



}
