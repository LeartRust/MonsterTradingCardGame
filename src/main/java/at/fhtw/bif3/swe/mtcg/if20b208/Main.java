package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import at.fhtw.bif3.swe.mtcg.if20b208.database.MTCGDaoDb;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.MonsterCardData;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.SpellCardData;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.UserData;
import at.fhtw.bif3.swe.mtcg.if20b208.user.Deck;
import at.fhtw.bif3.swe.mtcg.if20b208.user.Stack;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;
import at.fhtw.bif3.swe.mtcg.if20b208.user.UserHistory;

import java.util.*;

import java.util.stream.Collectors;

public class Main{
    private static MTCGDaoDb dao;

    public static void main(String[] args) {

/*
        //Stack stack = new Stack();
        User leart = new User("Leart", "123456789", Stack.fillStack());
        leart.setDeck(Deck.createDeck(leart));
        User goku = new User("Son Goku", "12345678", Stack.fillStack());
        goku.setDeck(Deck.createDeck(goku));
        leart.getHistory().add(new UserHistory("win", "TEST"));
*/

        //BattleLogic.fight(leart, goku);
/*
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
*/

/*
        leart.getStack().stream().forEach(element -> System.out.println(element.getName() + " Dmg:" + element.getDAMAGE()));
        System.out.println("__________________");
*/

/*
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
*/

        //MainClient newClient = new MainClient();
        //newClient.start();


        //For testing purposes
        dao = new MTCGDaoDb();
        MTCGDaoDb.initDb();
        MTCGDaoDb daoDb = new MTCGDaoDb();
        daoDb.saveUser(new UserData(1,"leart","sadsdasd",20,0,0,0,0));
        daoDb.saveUser(new UserData(2,"test1","sadsdasd",20,0,0,0,0));
        daoDb.saveUser(new UserData(3,"Son Goku","sadsdasd",20,0,0,0,0));

        fillStack(1);
        fillStack(2);

        ArrayList<Card> deck1 = daoDb.createDeck(1);
        ArrayList<Card> deck2 = daoDb.createDeck(2);
        daoDb.createDeck(2);

        User user1 = daoDb.getUser(1);
        User user2 = daoDb.getUser(2);

        System.out.println(user1.getUsername());
        BattleLogic battle = new BattleLogic(user1, user2, deck1, deck2);
        battle.fight();
        //UserData user1 = getPlayer(1);
        //daoDb.updateUser(user1, new String[] {"1","LeartR", "testpass"});


        //System.out.println(daoDb.getUser(1).get().getUserName() + " " + daoDb.getUser(1).get().getPassword());

        //UserData user3 = getPlayer(3);
        //dao.deleteUser(user3);

        dao.getAllUsers().forEach(item -> System.out.println(item));
        System.out.println("GET ALL MONSTER CARDS:");
        dao.getAllMonsterCards().forEach(item -> System.out.println(item));

        //TODO User inputs (setUsername, startFight, chooseDeck....)
        //TODO Create class for user inputs
        while(true){
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            System.out.println("You can use following commands:");
            System.out.println("register - creates a profile for you with your chosen username");
            System.out.println("login - log in with your data");
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
                //User thisUser = new User(name, password);
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
/*    private static UserData getPlayer(int id) {
        Optional<UserData> player = dao.getUser(id);

        return player.orElseGet(
                () -> new UserData()
        );
    }*/

    public static void fillStack(int userId) {
        for(int i = 0; i<=3; i++){
            Random r = new Random();
            int low = 10;
            int high = 80;
            int random_id = r.nextInt(12333131);
            int damage = r.nextInt(high-low) + low;
            if(i % 2 == 0){
                ElementType randomElement = ElementType.values()[new Random().nextInt(ElementType.values().length)];
                MonsterType randomMonster = MonsterType.values()[new Random().nextInt(MonsterType.values().length)]
                Card newCard = new Card(randomMonster+"-"+randomElement, damage);

                dao.saveMonsterCard(new MonsterCardData(random_id,newCard.getName(),newCard.getElement().name(),newCard.getMonsterType().name(),newCard.getDAMAGE(),userId));
            }else {
                SpellCard newCard = new SpellCard("", ElementType.values()[new Random().nextInt(ElementType.values().length)], damage);
                newCard.setName(newCard.getElement()+"-Spell");
                dao.saveSpellCard(new SpellCardData(random_id,newCard.getName(),newCard.getElement().name(),newCard.getDAMAGE(),userId));
            }


        }

    }

}
