package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.ElementType;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.MonsterCard;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.SpellCard;

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class BattleLogic {
    private User user1;
    private User user2;

    public BattleLogic(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }
    
    public int fight(){
        System.out.println("USER1 Cards");
        user1.getDeck().stream()
                .sorted(Comparator.comparingDouble(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("USER2 Cards");
        user2.getDeck().stream()
                .sorted(Comparator.comparingDouble(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("CARD LIST END");
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

    public void compareCards(int round){
        System.out.println("----------------------------------");
        int user1DrawnCard = (int)(Math.random() * user1.getDeck().size());
        int user2DrawnCard = (int)(Math.random() * user2.getDeck().size());

        System.out.println("Round " + round);

        Card card1 = user1.getDeck().get(user1DrawnCard);
        Card card2 = user2.getDeck().get(user2DrawnCard);

        if(card1 instanceof MonsterCard){
            System.out.println(((MonsterCard) card1).getMonsterType());
        }

        //TODO Method to compare all specialities!

        if(user1.getDeck().get(user1DrawnCard) instanceof SpellCard || user2.getDeck().get(user2DrawnCard) instanceof SpellCard) {
            compareByElements(card1, card2, user1DrawnCard, user2DrawnCard);
        }else if(card1 instanceof MonsterCard && card2 instanceof MonsterCard){
            compareDMG(card1.getDAMAGE(), card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
        }

    }

    private void compareByElements(Card card1, Card card2, int user1DrawnCard, int user2DrawnCard){

        //sout only for testing
        System.out.println(card1.getName() + " " + card1.getDAMAGE() + " ELEMENT----------------------------");
        System.out.println(card2.getName() + " " + card2.getDAMAGE() + " ELEMENT----------------------------");

        if((card1.getElement() == ElementType.FIRE) && (card2.getElement() == ElementType.NORMAL)){
            compareDMG((card1.getDAMAGE() * 2), (card2.getDAMAGE() / 2), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + (card1.getDAMAGE() * 2) + " " + user2.getUsername() + " " + (card2.getDAMAGE() / 2));
        } else if((card2.getElement() == ElementType.FIRE) && (card1.getElement() == ElementType.NORMAL)){
            compareDMG((card1.getDAMAGE() / 2), (card2.getDAMAGE() * 2), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + (card1.getDAMAGE() / 2) + " " + user2.getUsername() + " " + (card2.getDAMAGE() * 2));
        } else if((card1.getElement() == ElementType.WATER) && (card2.getElement() == ElementType.FIRE)){
            compareDMG((card1.getDAMAGE() * 2), (card2.getDAMAGE() / 2), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + (card1.getDAMAGE() * 2) + " " + user2.getUsername() + " " + (card2.getDAMAGE() / 2));
        } else if((card2.getElement() == ElementType.WATER) && (card1.getElement() == ElementType.FIRE)){
            compareDMG((card1.getDAMAGE() / 2), (card2.getDAMAGE() * 2), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + (card1.getDAMAGE() / 2) + " " + user2.getUsername() + " " + (card2.getDAMAGE() * 2));
        } else if ((card1.getElement() == ElementType.NORMAL) && (card2.getElement() == ElementType.WATER)){
            compareDMG((card1.getDAMAGE() * 2), (card2.getDAMAGE() / 2), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + (card1.getDAMAGE() * 2) + " " + user2.getUsername() + " " + (card2.getDAMAGE() / 2));
        } else if ((card2.getElement() == ElementType.NORMAL) && (card1.getElement() == ElementType.WATER)){
            compareDMG((card1.getDAMAGE() / 2), (card2.getDAMAGE() * 2), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + (card1.getDAMAGE() / 2) + " " + user2.getUsername() + " " + (card2.getDAMAGE() * 2));
        } else {
            compareDMG(card1.getDAMAGE(), card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
            System.out.println("DMG after calculating element effectiveness:");
            System.out.println(user1.getUsername() + " " + card1.getDAMAGE() + " " + user2.getUsername() + " " + card2.getDAMAGE());
        }


    }

    private void compareMonsterFight(MonsterCard card1, MonsterCard card2){
        System.out.println("COMPARE ONLY MONSTER CARDS");
        System.out.println(card1.getName() + " tpye: " + card1.getMonsterType());
        System.out.println(card2.getName() + " tpye: " + card2.getMonsterType());
    }

    //dmg1 - always user1 | dmg2 - always user2
    private void compareDMG(double dmg1, double dmg2, int user1DrawnCard, int user2DrawnCard){
        if(dmg1 > dmg2){
            showWinner(user1, user1DrawnCard, user2DrawnCard);
        }else if (dmg2 > dmg1){
            showWinner(user2, user1DrawnCard, user2DrawnCard);
        } else {
            System.out.println("DRAW");
            System.out.println("Name: " + user1.getDeck().get(user1DrawnCard).getName() + " vs Name: " + user2.getDeck().get(user2DrawnCard).getName());
            System.out.println("Damage: " + user1.getDeck().get(user1DrawnCard).getDAMAGE() + " vs Damage: " + user2.getDeck().get(user2DrawnCard).getDAMAGE());
        }

    }

    public void showWinner(User winner, int user1DrawnCard, int user2DrawnCard){
        System.out.println("Winner " + winner.getUsername());
        System.out.println("Left Player:" + user1.getUsername() + " Right Player: " + user2.getUsername());
        System.out.println("Name: " + user1.getDeck().get(user1DrawnCard).getName() + " vs Name: " + user2.getDeck().get(user2DrawnCard).getName());
        System.out.println("Damage: " + user1.getDeck().get(user1DrawnCard).getDAMAGE() + " vs Damage: " + user2.getDeck().get(user2DrawnCard).getDAMAGE());
    }


}
