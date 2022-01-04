package at.fhtw.bif3.swe.mtcg.if20b208.database.model;

import java.io.Serializable;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.ElementType;
import at.fhtw.bif3.swe.mtcg.if20b208.user.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SpellCardData implements Serializable {
    private String name;
    private ElementType element;
    private double DAMAGE;
}
