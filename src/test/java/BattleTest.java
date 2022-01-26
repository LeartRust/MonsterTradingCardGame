import at.fhtw.bif3.swe.mtcg.if20b208.BattleLogic;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class BattleTest {
    User mockedA = new User("TestUser1", "testPassword", 20,0,0,0,0);
    User mockedB = new User("TestUser2", "testPassword", 20,0,0,0,0);
    ArrayList<Card> deck1 = new ArrayList<>();
    ArrayList<Card> deck2 = new ArrayList<>();
    BattleLogic battleLogic;
    //TODO create 2 Test users with decks and test battle logic + elo stats, etc...


    @Test
    @DisplayName("Test if deck goes -1 for user1")
    public  void deckSizeTest1() {
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        SpellCard spell1 = new SpellCard("WaterSpell", ElementType.WATER, 65);
        deck1.add(spell1);
        deck2.add(monster1);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        battleLogic.fight();
        assertTrue("Deck1 size = 2 | Deck2 size = 0", deck1.size() == 2 && deck2.size() == 0);
    }

    @Test
    @DisplayName("Test if deck goes -1 for user2")
    public  void deckSizeTest2() {
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        SpellCard spell1 = new SpellCard("WaterSpell", ElementType.WATER, 65);
        deck1.add(monster1);
        deck2.add(spell1);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Deck1 size = 0 | Deck2 size = 2", deck2.size() == 2 && deck1.size() == 0);
    }

    @Test
    @DisplayName("Test if draw possible")
    public  void drawTest() {
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        deck1.add(monster1);
        deck2.add(monster2);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Draw possible", deck1.size() == 1);
    }

    //TESTS IF ELEMENT MAKES A DIFFERENCE IN FIGHTS (Monster vs Spell & Spell vs Spell) & (no specialities)!

    //TEST by Spell vs Monster
    @Test
    @DisplayName("Test if Water Spell is stronger than Fire Monster")
    public  void FightWaterSpellVsFireMonster() {
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 55, MonsterType.DRAGON);
        SpellCard spell1 = new SpellCard("WaterSpell", ElementType.WATER, 15);
        deck1.add(spell1);
        deck2.add(monster1);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Water Spell is stronger than Fire Monster", deck1.size() == 2 && deck2.size() == 0);
    }
    @Test
    @DisplayName("Test if Normal Monster is stronger than Water Spell")
    public  void FightNormalMonsterVsWaterSpell() {
        MonsterCard monster1 = new MonsterCard("NormalGoblin", ElementType.NORMAL, 55, MonsterType.GOBLIN);
        SpellCard spell1 = new SpellCard("WaterSpell", ElementType.WATER, 15);
        deck1.add(monster1);
        deck2.add(spell1);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Normal Monster is stronger than Water Spell", deck1.size() == 2 && deck2.size() == 0);
    }
    @Test
    @DisplayName("Test if Fire Monster is stronger than Normal Spell")
    public  void FightFireMonsterVsNormalSpell() {
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        SpellCard spell1 = new SpellCard("NormalSpell", ElementType.NORMAL, 55);
        deck1.add(monster1);
        deck2.add(spell1);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Fire is stronger than Normal", deck1.size() == 2 && deck2.size() == 0);
    }

    //Test Spell vs Spell
    @Test
    @DisplayName("Test if Fire Spell is stronger than Normal Spell")
    public  void FightWaterSpellVsFireSpell() {
        SpellCard spell1 = new SpellCard("WaterSpell", ElementType.WATER, 15);
        SpellCard spell2 = new SpellCard("FireSpell", ElementType.FIRE, 55);
        deck1.add(spell1);
        deck2.add(spell2);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Water Spell is stronger than Fire Spell", deck1.size() == 2 && deck2.size() == 0);
    }

    @Test
    @DisplayName("Test if Fire Spell is stronger than Normal Spell")
    public  void FightFireSpellVsNormalSpell() {
        SpellCard spell1 = new SpellCard("FireSpell", ElementType.FIRE, 15);
        SpellCard spell2 = new SpellCard("NormalSpell", ElementType.NORMAL, 55);
        deck1.add(spell1);
        deck2.add(spell2);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Fire Spell is stronger than Normal Spell", deck1.size() == 2 && deck2.size() == 0);
    }

    @Test
    @DisplayName("Test if Fire Spell is stronger than Normal Spell")
    public  void FightNormalSpellVsWaterSpell() {
        SpellCard spell1 = new SpellCard("NormalSpell", ElementType.NORMAL, 15);
        SpellCard spell2 = new SpellCard("WaterSpell", ElementType.WATER, 55);
        deck1.add(spell1);
        deck2.add(spell2);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Fire Spell is stronger than Normal Spell", deck1.size() == 2 && deck2.size() == 0);
    }


    @Test
    @DisplayName("Test if Element makes a difference in pure Monster fights")
    public  void pureMonsterFight() {
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 60, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("WaterDragon", ElementType.WATER, 55, MonsterType.DRAGON);
        deck1.add(monster1);
        deck2.add(monster2);
        battleLogic = new BattleLogic(mockedA, mockedB, deck1, deck2);
        System.out.println(deck1.size());
        System.out.println(deck2.size());
        battleLogic.fight();
        assertTrue("Element makes no difference in this fight", deck1.size() == 2 && deck2.size() == 0);
    }


}
