package at.fhtw.bif3.swe.mtcg.if20b208.cards;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Card {
    private String name;
    private final double DAMAGE;

    public Card(String name, final int DAMAGE){
        this.name = name;
        this.DAMAGE = DAMAGE;
    }
}
