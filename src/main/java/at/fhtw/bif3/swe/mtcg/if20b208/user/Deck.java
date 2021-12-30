package at.fhtw.bif3.swe.mtcg.if20b208.user;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {


    public static List<Card> createDeck(User user){
        List<Card> deck;
        //TODO What if 2 Cards same DMG?
        //TODO User should be able to pick the cards he want to put in his deck by himself!!!!!!
        deck = user.getStack().stream()
                .sorted(Comparator.comparingDouble(Card::getDAMAGE).reversed())
                .limit(4)
                .collect(Collectors.toList());

        return deck;
    }
    public static void chooseDeck(User user){
        System.out.println("CHOOSE DECK");
    }
}
