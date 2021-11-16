package at.fhtw.bif3.swe.mtcg.if20b208.cards;

import lombok.*;

@Getter
@Setter
public class MonsterCard extends Card{
    private MonsterType monsterType;

    public MonsterCard(String name, ElementType element, int DAMAGE, MonsterType monsterType) {
        super(name, element, DAMAGE);
        this.monsterType = monsterType;
    }


    @Override
    public String toString() {
        return "MonsterCard{" +
                "monsterType=" + monsterType +
                "} " + super.toString();
    }
}
