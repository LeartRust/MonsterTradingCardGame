package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.user.Deck;
import at.fhtw.bif3.swe.mtcg.if20b208.user.Stack;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;
import at.fhtw.bif3.swe.mtcg.if20b208.user.UserHistory;

import java.util.Comparator;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Main{
    public static void main(String[] args) {

        //Stack stack = new Stack();
        User leart = new User("Leart", "123456789", Stack.fillStack());
        leart.setDeck(Deck.createDeck(leart));
        User goku = new User("Son Goku", "12345678", Stack.fillStack());
        goku.setDeck(Deck.createDeck(goku));
        leart.getHistory().add(new UserHistory("win", "TEST"));

        //BattleLogic.fight(leart, goku);
        BattleLogic battle = new BattleLogic(leart, goku);
        battle.fight();

        System.out.println("HISTORY STATS BEGIN");
        leart.getHistory().stream().forEach(element -> System.out.println(element.getMatchOutcome() + " " + element.getOpponent()));
        System.out.println(leart.getEloPoints());
        System.out.println(leart.getGamesPlayed());
        System.out.println(leart.getWins());
        System.out.println(leart.getLosses());
        goku.getHistory().stream().forEach(element -> System.out.println(element.getMatchOutcome() + " " + element.getOpponent()));
        System.out.println(goku.getEloPoints());
        System.out.println(goku.getWins());
        System.out.println(goku.getLosses());
        System.out.println("HISTORY STATS END");

        leart.getStack().stream().forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("__________________");

        System.out.println("Player1");
        leart.getDeck().stream()
                .sorted(Comparator.comparingDouble(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("Player2");
        goku.getDeck().stream()
                .sorted(Comparator.comparingDouble(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element));


        //TODO User inputs (setUsername, startFight, chooseDeck....)
        //TODO Create class for user inputs
        while(true){
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            System.out.println("You can use following commands:");
            System.out.println("setProfile - creates a profile for you with your chosen username");
            System.out.println("chooseDeck - lets you choose 4 cards for your deck");
            System.out.println("quit - ends the app");

            String command = myObj.nextLine();  // Read user input

            if(command.equals("quit")){
                System.out.println("You pressed quit");
                break;
            }else if(command.equals("setProfile")){
                System.out.println("Type in your Username:");
                String name = myObj.nextLine();  // Read user input
                System.out.println("Choose a password:");
                String password = myObj.nextLine();  // Read user input
                User thisUser = new User(name, password, Stack.fillStack());
            }
            else if(command.equals("chooseDeck")){
                //Deck.chooseDeck(thisUser);
            }else {
                System.out.println("nothing");
            }
        }

        //------------------------------------
/*
        String string1 = "Leart";
        String string2 = "Rustemi";

        Collection<String> stringCollection = new ArrayList<>();
        stringCollection.add(string1);
        stringCollection.add(string2);

        stringCollection.stream().forEach(element -> System.out.println(element));

        stringCollection.stream().filter(element -> element.startsWith("L"))
                .forEach(element -> System.out.println(element));
*/


    }

}
