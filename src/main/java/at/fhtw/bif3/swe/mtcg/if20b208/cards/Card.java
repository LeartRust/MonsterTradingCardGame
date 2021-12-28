package at.fhtw.bif3.swe.mtcg.if20b208.cards;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public abstract class Card {
    private String name;
    private ElementType element;
    private final double DAMAGE;

    public Card(String name, ElementType element, final int DAMAGE){
        this.name = name;
        this.element = element;
        this.DAMAGE = DAMAGE;
    }
}
