package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.database.MTCGDaoDb;
import at.fhtw.bif3.swe.mtcg.if20b208.server.Server;
import at.fhtw.bif3.swe.mtcg.if20b208.server.ResponseHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class Main{
    private static MTCGDaoDb dao;

    public static void main(String[] args) {

        dao = new MTCGDaoDb();
        MTCGDaoDb.initDb();
        MTCGDaoDb daoDb = new MTCGDaoDb();

        Server server = new Server();
        try {
            server.start(10001);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("dasdasda");
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

/*        daoDb.saveUser(new UserData("leart","sadsdasd",20,false,0,0,0,0));
        daoDb.saveUser(new UserData("test1","sadsdasd",20,false,0,0,0,0));
        daoDb.saveUser(new UserData("Son Goku","sadsdasd",20,false,0,0,0,0));

        fillStack(1);
        fillStack(2);

        ArrayList<Card> deck1 = daoDb.createDeck(1);
        ArrayList<Card> deck2 = daoDb.createDeck(2);
        daoDb.createDeck(2);

        User user1 = daoDb.getUser("leart");
        User user2 = daoDb.getUser("Son Goku");

        System.out.println(user1.getUsername());
        BattleLogic battle = new BattleLogic(user1, user2, deck1, deck2);
        battle.fight();*/
        //UserData user1 = getPlayer(1);
        //daoDb.updateUser(user1, new String[] {"1","LeartR", "testpass"});


        //System.out.println(daoDb.getUser(1).get().getUserName() + " " + daoDb.getUser(1).get().getPassword());

        //UserData user3 = getPlayer(3);
        //dao.deleteUser(user3);

        //dao.getAllUsers().forEach(item -> System.out.println(item));
        //System.out.println("GET ALL MONSTER CARDS:");
        //dao.getAllCards().forEach(item -> System.out.println(item));

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

    //for testing
/*    public static void fillStack(int userId) {
        for(int i = 0; i<=3; i++){
            Random r = new Random();
            int low = 10;
            int high = 80;
            int random_id = r.nextInt(12333131);
            int damage = r.nextInt(high-low) + low;
            if(i % 2 == 0){
                ElementType randomElement = ElementType.values()[new Random().nextInt(ElementType.values().length)];
                MonsterType randomMonster = MonsterType.values()[new Random().nextInt(MonsterType.values().length)];
                //Card newCard = new Card(randomMonster + "-" + randomElement, damage);
                //dao.saveCard(new CardData(random_id, randomMonster + "-" + randomElement,damage,userId));
            }else {
                //SpellCard newCard = new SpellCard("", ElementType.values()[new Random().nextInt(ElementType.values().length)], damage);
                ElementType randomElement = ElementType.values()[new Random().nextInt(ElementType.values().length)];
               // dao.saveCard(new CardData(random_id,randomElement+"-Spell", damage, userId));
            }


        }

    }*/

}
