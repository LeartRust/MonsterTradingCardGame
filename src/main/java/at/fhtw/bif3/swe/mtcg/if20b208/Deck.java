package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {


    public static List<Card> createDeck(User user){
        List<Card> deck;

        deck = user.getStack().stream()
                .sorted(Comparator.comparingInt(Card::getDAMAGE).reversed())
                .limit(4)
                .collect(Collectors.toList());

        return deck;
    }
}
