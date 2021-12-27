package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.MonsterCard;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.SpellCard;

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BattleLogic {
    private User user1;
    private User user2;

    public BattleLogic(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }
    
    public int fight(){
        System.out.println("TEEEEEEEEEEEST");
        user1.getDeck().stream()
                .sorted(Comparator.comparingInt(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("TEEEEEEEEEEEST");

        //Shuffle both decks first
        Collections.shuffle(user1.getDeck());
        Collections.shuffle(user2.getDeck());
        //TODO
        for(int i = 0; i < 10; i++){
            //get random card from both decks and compare
            if (user1.getDeck().size() != 0 && user2.getDeck().size() != 0){
                compareCards(i);
            }else if(user1.getDeck().size() == 0) {
                System.out.println("User 2 Won");
                System.out.println("User 2 Deck size: " + user2.getDeck().size());
            }else {
                System.out.println("User 1 Won");
                System.out.println("User 1 Deck size: " + user1.getDeck().size());
            }
        }

        //user1.getDeck().forEach(element -> System.out.println(element));
        return 1;
    }

    public int compareCards(int round){
        System.out.println("----------------------------------");
        int user1DrawnCard = (int)(Math.random() * user1.getDeck().size());
        int user2DrawnCard = (int)(Math.random() * user2.getDeck().size());
        System.out.println("Round " + round);
        Card card1 = user1.getDeck().get(user1DrawnCard);
        Card card2 = user1.getDeck().get(user1DrawnCard);
        if(card1 instanceof MonsterCard){
            System.out.println(((MonsterCard) card1).getMonsterType());
        }

/*
        if (user1.getDeck().get(user1DrawnCard) instanceof SpellCard){
            System.out.println(user1.getDeck().get(user1DrawnCard).getName() + "SPELLCARD ++++++++++++++++++++");
        } else if(user1.getDeck().get(user1DrawnCard) instanceof MonsterCard){
            System.out.println(user1.getDeck().get(user1DrawnCard).getName() + " MONSTERCARD ++++++++++++++++++++");
        }
*/
        if(user1.getDeck().get(user1DrawnCard) instanceof SpellCard || user2.getDeck().get(user2DrawnCard) instanceof SpellCard) {
            compareByElements(card1, card2);
        }

        if (user1.getDeck().get(user1DrawnCard).getDAMAGE() > user2.getDeck().get(user2DrawnCard).getDAMAGE() ){
            System.out.println("Winner " + user1.getUsername());
            System.out.println("Left Player:" + user1.getUsername() + " Right Player: " + user2.getUsername());
            System.out.println("Name: " + user1.getDeck().get(user1DrawnCard).getName() + " vs Name: " + user2.getDeck().get(user2DrawnCard).getName());
            System.out.println("Damage: " + user1.getDeck().get(user1DrawnCard).getDAMAGE() + " vs Damage: " + user2.getDeck().get(user2DrawnCard).getDAMAGE());
            //user1.getDeck().add(user2.getDeck().get(user2DrawnCard).);
        }else if(user2.getDeck().get(user2DrawnCard).getDAMAGE() > user1.getDeck().get(user1DrawnCard).getDAMAGE() ){
            System.out.println("Winner " + user2.getUsername());
            System.out.println("Left Player:" + user2.getUsername() + " Right Player: " + user1.getUsername());
            System.out.println("Name: " + user2.getDeck().get(user1DrawnCard).getName() + " vs Name: " + user1.getDeck().get(user2DrawnCard).getName());
            System.out.println("Damage: " + user2.getDeck().get(user1DrawnCard).getDAMAGE() + " vs Damage: " + user1.getDeck().get(user2DrawnCard).getDAMAGE());
        }else {
            System.out.println("DRAW");
            System.out.println("Name: " + user1.getDeck().get(user1DrawnCard).getName() + " vs Name: " + user2.getDeck().get(user2DrawnCard).getName());
            System.out.println("Damage: " + user1.getDeck().get(user1DrawnCard).getDAMAGE() + " vs Damage: " + user2.getDeck().get(user2DrawnCard).getDAMAGE());
        }
        System.out.println("------------------------------------");
        return 0;
    }

    private int compareByElements(Card card1, Card card2){
        System.out.println("COMPARE SPELL MONSTER CARDS");
        return 0;
    }

}
