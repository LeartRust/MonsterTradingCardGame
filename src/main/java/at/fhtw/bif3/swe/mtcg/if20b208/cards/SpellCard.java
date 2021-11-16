package at.fhtw.bif3.swe.mtcg.if20b208.cards;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpellCard extends Card{

    public SpellCard(String name, ElementType element, int DAMAGE) {
        super(name, element, DAMAGE);
    }

    @Override
    public String toString() {
        return "SpellCard{}" + super.toString();
    }
}
