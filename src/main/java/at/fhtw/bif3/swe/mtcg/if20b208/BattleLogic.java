package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import at.fhtw.bif3.swe.mtcg.if20b208.user.Deck;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BattleLogic {
    private final User user1;
    private final User user2;

    public BattleLogic(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }
    
    public void fight(){

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
        if(user1.getDeck().size() == 0){
            System.out.println(user1.getUsername() + " has no deck to compete with");
        } else if (user2.getDeck().size() == 0){
            System.out.println(user2.getUsername() + " has no deck to compete with");
        }else {
            for(int i = 1; i < 101; i++){
                //get random card from both decks and compare
                if (i == 100){
                    System.out.println("This Match ended in a Draw");
                } else if (user1.getDeck().size() != 0 && user2.getDeck().size() != 0){
                    compareCards(i);
                }else if(user1.getDeck().size() == 0) {
                    System.out.println("User 2 Won");
                    System.out.println("User 2 Deck size: " + user2.getDeck().size());
                    break;
                }else if(user2.getDeck().size() == 0){
                    System.out.println("User 1 Won");
                    System.out.println("User 1 Deck size: " + user1.getDeck().size());
                    break;
                }
            }
        }

        //Reset Swapped cards to initial status
        //TODO reset Deck (when Deck choosing is updated)
        user1.setDeck(Deck.createDeck(user1));
        user2.setDeck(Deck.createDeck(user2));
        System.out.println("========================================");
    }

    public void compareCards(int round){
        System.out.println("----------------------------------");
        int user1DrawnCard = (int)(Math.random() * user1.getDeck().size());
        int user2DrawnCard = (int)(Math.random() * user2.getDeck().size());

        System.out.println("Round " + round);

        Card card1 = user1.getDeck().get(user1DrawnCard);
        Card card2 = user2.getDeck().get(user2DrawnCard);

        //TODO Method to compare all specialities!
        if (checkSpecialities(card1, card2, user1DrawnCard, user2DrawnCard) == true){

        } else if(user1.getDeck().get(user1DrawnCard) instanceof SpellCard || user2.getDeck().get(user2DrawnCard) instanceof SpellCard) {
            compareByElements(card1, card2, user1DrawnCard, user2DrawnCard);
        }else if(card1 instanceof MonsterCard && card2 instanceof MonsterCard){
            compareDMG(card1.getDAMAGE(), card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
        }

    }

    private void compareByElements(Card card1, Card card2, int user1DrawnCard, int user2DrawnCard){

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

    private boolean checkSpecialities(Card card1, Card card2, int user1DrawnCard, int user2DrawnCard){
        System.out.println("COMPARE SPECIALITIES");

        if (card1 instanceof MonsterCard && card2 instanceof MonsterCard){
            //check if Goblin vs Dragon
            if (((MonsterCard) card1).getMonsterType().equals(MonsterType.GOBLIN) && ((MonsterCard) card2).getMonsterType().equals(MonsterType.DRAGON)){
                compareDMG(0,card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
                System.out.println("Dragons win against Goblins");
                return true;
            } else if (((MonsterCard) card2).getMonsterType().equals(MonsterType.GOBLIN) && ((MonsterCard) card1).getMonsterType().equals(MonsterType.DRAGON)){
                compareDMG(card1.getDAMAGE(),0, user1DrawnCard, user2DrawnCard);
                System.out.println("Dragons win against Goblins");
                return true;
            }
            //check if Wizard vs Ork
            else if (((MonsterCard) card1).getMonsterType().equals(MonsterType.WIZARD) && ((MonsterCard) card2).getMonsterType().equals(MonsterType.ORK)){
                compareDMG(card1.getDAMAGE(),0, user1DrawnCard, user2DrawnCard);
                System.out.println("Wizards win against Orks");
                return true;
            }else if(((MonsterCard) card2).getMonsterType().equals(MonsterType.WIZARD) && ((MonsterCard) card1).getMonsterType().equals(MonsterType.ORK)){
                compareDMG(0, card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
                System.out.println("Wizards win against Orks");
                return true;
            }
            //check if FireElves vs Dragon
            else if((((MonsterCard) card1).getMonsterType().equals(MonsterType.ELF) && card1.getElement().equals(ElementType.FIRE)) && ((MonsterCard) card2).getMonsterType().equals(MonsterType.DRAGON)){
                compareDMG(card1.getDAMAGE(), 0, user1DrawnCard, user2DrawnCard);
                System.out.println("FireElves win against Dragons");
                return true;
            }else if((((MonsterCard) card2).getMonsterType().equals(MonsterType.ELF) && card2.getElement().equals(ElementType.FIRE)) && ((MonsterCard) card1).getMonsterType().equals(MonsterType.DRAGON)){
                compareDMG(0, card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
                System.out.println("FireElves win against Dragons");
                return true;
            }
        }
        //check if Water-Spell vs Knight
        else if((card1 instanceof SpellCard && card1.getElement().equals(ElementType.WATER)) && (card2 instanceof MonsterCard && ((MonsterCard) card2).getMonsterType().equals(MonsterType.KNIGHT))){
            compareDMG(card1.getDAMAGE(), 0, user1DrawnCard, user2DrawnCard);
            System.out.println("Water-Spells win against Knights");
            return true;
        }else if((card2 instanceof SpellCard && card2.getElement().equals(ElementType.WATER)) && (card1 instanceof MonsterCard && ((MonsterCard) card1).getMonsterType().equals(MonsterType.KNIGHT))){
            compareDMG(0, card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
            System.out.println("Water-Spells win against Knights");
            return true;
        }
        //check if Kraken vs any Spell
        else if((card1 instanceof MonsterCard && ((MonsterCard) card1).getMonsterType().equals(MonsterType.KRAKEN)) && card2 instanceof SpellCard){
            compareDMG(card1.getDAMAGE(), 0, user1DrawnCard, user2DrawnCard);
            System.out.println("Krakens win against Spells");
            return true;
        }else if((card2 instanceof MonsterCard && ((MonsterCard) card2).getMonsterType().equals(MonsterType.KRAKEN)) && card1 instanceof SpellCard){
            compareDMG(0, card2.getDAMAGE(), user1DrawnCard, user2DrawnCard);
            System.out.println("Krakens win against Spells");
            return true;
        }
        return false;
    }

    //dmg1 - always user1 | dmg2 - always user2 !
    private void  compareDMG(double dmg1, double dmg2, int user1DrawnCard, int user2DrawnCard){
        if (dmg1 > dmg2){
            showWinner(user1, user1DrawnCard, user2DrawnCard);
            swapCards(user1, user2, user2.getDeck().get(user2DrawnCard));
        }else if (dmg2 > dmg1){
            showWinner(user2, user1DrawnCard, user2DrawnCard);
            swapCards(user2, user1, user1.getDeck().get(user1DrawnCard));
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

    public void swapCards(User winner, User loser, Card card){
        winner.getDeck().add(card);
        loser.getDeck().remove(card);
    }
}
