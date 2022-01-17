package at.fhtw.bif3.swe.mtcg.if20b208.cards;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Card {
    private String name;
    private ElementType element;
    private final double DAMAGE;

    public Card(String name, ElementType element, final double DAMAGE){
        this.name = name;
        this.element = element;
        this.DAMAGE = DAMAGE;
    }


}
