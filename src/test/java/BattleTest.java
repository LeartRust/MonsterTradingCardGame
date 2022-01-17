import at.fhtw.bif3.swe.mtcg.if20b208.BattleLogic;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.ElementType;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.MonsterCard;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.MonsterType;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.SpellCard;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;


public class BattleTest {
    @Test
    public void compareSameElement(){
        MonsterCard monster1 = new MonsterCard("FireDragon", ElementType.FIRE, 15, MonsterType.DRAGON);
        MonsterCard monster2 = new MonsterCard("FireDragon", ElementType.FIRE, 70, MonsterType.DRAGON);
        assertTrue("Both Monsters have the same ", monster1.getMonsterType() ==  monster2.getMonsterType());
    }



}
