package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
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
        //goku.setDeck(Deck.fillStack(goku.getStack()));
        //Stack stack = new Stack(Stack.fillStack());
        /*
        System.out.println(stack.toString());
        System.out.println("________________________________");
        */

        //BattleLogic.fight(leart, goku);
        BattleLogic battle = new BattleLogic(leart, goku);
        battle.fight();
        //System.out.println(leart.toString() + " " + leart.getStack().size());

        leart.getStack().stream().forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("__________________");
        /*leart.getStack().stream()
                .sorted(Comparator.comparingInt(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));*/

        System.out.println("Player1");
        leart.getDeck().stream()
                .sorted(Comparator.comparingInt(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("Player2");
        goku.getDeck().stream()
                .sorted(Comparator.comparingInt(Card::getDAMAGE).reversed())
                .collect(Collectors.toList())
                .forEach(element -> System.out.println(element));


        //TODO User inputs (setUsername, startFight, chooseDeck....)
        //TODO Create class for user inputs
        while(true){
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter username");
            String command = myObj.nextLine();  // Read user input

            if(command.equals("quit")){
                System.out.println("dasdasda");
                break;
            }else if(command.equals("test")){
                System.out.println("blab");
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
