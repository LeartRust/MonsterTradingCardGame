package at.fhtw.bif3.swe.mtcg.if20b208.database;

import at.fhtw.bif3.swe.mtcg.if20b208.BattleLogic;
import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.*;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MTCGDaoDb implements Dao<UserData, CardData>{

    public static void initDb() {
        // re-create the database
        try (Connection connection = DbConnection.getInstance().connect("")) {
            DbConnection.executeSql(connection, "DROP DATABASE mtcg_database", true );
            DbConnection.executeSql(connection,  "CREATE DATABASE mtcg_database", true );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // create the table
        // PostgreSQL documentation: https//www.postgresqltutorial.com/postgresql-create-table/
        try {

            DbConnection.getInstance().executeSql("""
                    CREATE TABLE IF NOT EXISTS users (
                        userName TEXT PRIMARY KEY NOT NULL,
                        password TEXT NOT NULL,
                        coins INT NOT NULL,
                        eloPoints INT NOT NULL,
                        gamesPlayed INT NOT NULL,
                        wins INT NOT NULL,
                        losses INT NOT NULL,
                        isLoggedIn BOOLEAN NOT NULL,
                        profilename TEXT,
                        bio TEXT,
                        image TEXT
                    );

                    CREATE TABLE IF NOT EXISTS cards (
                        id TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        damage FLOAT NOT NULL,
                        element TEXT NOT NULL,
                        type TEXT NOT NULL,
                        userName TEXT,
                        isDeck BOOLEAN NOT NULL,
                        CONSTRAINT users
                              FOREIGN KEY(userName)
                        	  REFERENCES users(userName) ON DELETE CASCADE
                    );
                    
                    CREATE TABLE IF NOT EXISTS packages(
                        id SERIAL PRIMARY KEY,
                        card_id TEXT NOT NULL,
                        CONSTRAINT cards
                              FOREIGN KEY(card_id)
                        	  REFERENCES cards(id)
                    );
                    
                    CREATE TABLE IF NOT EXISTS trade_deals(
                        trade_id TEXT PRIMARY KEY,
                        trading_card_id TEXT NOT NULL,
                        element TEXT,
                        type TEXT,
                        damage FLOAT,
                        CONSTRAINT cards
                            FOREIGN KEY(trading_card_id)
                            REFERENCES cards(id)
                    );
                    
                    CREATE TABLE IF NOT EXISTS current_battles (
                        id SERIAL PRIMARY KEY,
                        initiating_user TEXT UNIQUE NOT NULL,
                        joining_user TEXT UNIQUE,
                        CONSTRAINT users
                            FOREIGN KEY(initiating_user)
                            REFERENCES users(username),
                            FOREIGN KEY(joining_user)
                            REFERENCES users(username)
                    );
                   
                    
                    """);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String battle(String username){
        String log = "";
        List<Integer> openBattles = activeBattles();
        if (openBattles.size() > 0) {
            System.out.println("there is a battle open");
            //joining open battle which is first in row
            System.out.println("RANDOM id; " + openBattles.get(0));
            if(joinBattle(username, openBattles.get(0))){
                System.out.println("joined battle, making ready for fight");
                String initiating_user = getInitiatingUser(username);
                ArrayList<Card> userDeck1 = getUserDeckForBattle(initiating_user);
                ArrayList<Card> userDeck2 = getUserDeckForBattle(username);
                userDeck1.stream().forEach(element -> System.out.println(element.getName()));
                userDeck2.stream().forEach(element -> System.out.println(element.getName()));
                User user1 = getUser(initiating_user);
                User user2 = getUser(username);
                BattleLogic battle = new BattleLogic(user1, user2, userDeck1, userDeck2);
                battle.fight();
                String winner = battle.getWinner();
                deleteClosedBattle(initiating_user, username);
                log = battle.getHistory();
                if (winner.equals("DRAW")){
                    System.out.println("No winner");
                    updateUserAfterBattle(user1.getUsername(), 0, 0, -1);
                    updateUserAfterBattle(user2.getUsername(), 0, 0, -1);
                }else if(winner.equals(user1.getUsername())){
                    System.out.println("player 1 won");
                    tradeCard(user1.getUsername(), getStrongestCardId(user2.getUsername()));
                    updateUserAfterBattle(user1.getUsername(), 1,0, 3);
                    updateUserAfterBattle(user2.getUsername(), 0, 1, -5);
                    addCoins(user1.getUsername());
                    subtractCoins(user2.getUsername());
                }else if (winner.equals(user2.getUsername())){
                    System.out.println("player 2 won");
                    tradeCard(user2.getUsername(), getStrongestCardId(user1.getUsername()));
                    updateUserAfterBattle(user1.getUsername(), 0, 1, -5);
                    updateUserAfterBattle(user2.getUsername(), 1, 0, 3);
                    addCoins(user2.getUsername());
                    subtractCoins(user1.getUsername());
                }

                return log;
            }
        }else {
            System.out.println("no battle open");
            System.out.println("initiating battle and waiting for opponent");
            if(startBattle(username)){
                System.out.println("new Battle opened");
            }else {
                System.out.println("couldnt open battle");
            }
        }
        return log;
    }

    public void updateUserAfterBattle(String username, int win, int loss, int elopoints){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users
                SET gamesplayed = gamesplayed + 1,
                    wins = wins + ?,
                    losses = losses + ?,
                    elopoints = elopoints + ?
                WHERE username = ?;
                """ )
        ) {

            statement.setInt(1, win);
            statement.setInt(2, loss);
            statement.setInt(3, elopoints);
            statement.setString(4, username);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getStrongestCardId(String username){
        String card_id="";
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id
                FROM cards
                WHERE username = ?
                ORDER BY damage DESC
                      LIMIT 1;
                """)
        ) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                card_id = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return card_id;
    }

    public ArrayList<Card> getUserDeckForBattle(String username){
        ArrayList<Card> userDeck = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT name, element, damage, type
                FROM cards
                Where userName = ? AND isDeck = true
                """)
        ) {
            statement.setString(1, username );
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                if (resultSet.getString(4).toLowerCase(Locale.ROOT).contains("spell")){
                    userDeck.add( new SpellCard(
                            resultSet.getString(1),
                            ElementType.valueOf(resultSet.getString( 2)),
                            resultSet.getDouble( 3)
                    ) );
                }else {
                    userDeck.add( new MonsterCard(
                            resultSet.getString(1),
                            ElementType.valueOf(resultSet.getString( 2)),
                            resultSet.getDouble( 3),
                            MonsterType.valueOf(resultSet.getString(4))
                    ) );
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userDeck;
    }

    public Boolean startBattle(String username){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO current_battles
                (initiating_user)
                VALUES (?);
                """ )
        ) {
            statement.setString(1, username );
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Boolean joinBattle(String username, Integer id){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE current_battles
                SET joining_user = ?
                WHERE id = ?
                """ )
        ) {
            statement.setString(1, username);
            statement.setInt(2, id);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public List<Integer> activeBattles(){
        List<Integer> openBattles = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id
                FROM current_battles
                WHERE joining_user IS null AND
                      initiating_user IS NOT null
                      LIMIT 1;
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                System.out.println("RESULT FROM BATTLES: " + resultSet.getString(1));
                openBattles.add(resultSet.getInt(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("LIST SIZE:" + openBattles.size());
        return openBattles;
    }


    public String getInitiatingUser(String joining_user){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT initiating_user
                FROM current_battles
                WHERE joining_user = ?
                """)
        ) {
            statement.setString(1, joining_user);
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public List<Trademarket> getTradingDeals(){
        List<Trademarket> deals = new ArrayList<>();

        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT cards.name, trade_deals.type, trade_deals.damage
                FROM trade_deals, cards
                WHERE trade_deals.trading_card_id = cards.id
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                deals.add( new Trademarket(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble( 3)
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deals;
    }

    public Boolean checkIfCardBelongsToUser(String username, String card_id){
        System.out.println("USER NAMEE: " + username + " id: " + card_id);
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT 1
                FROM cards
                WHERE username = ? AND id = ?
                LIMIT 1;
                """)
        ) {
            statement.setString( 1, username);
            statement.setString( 2, card_id);
            //statement.execute();
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                System.out.println("------------------------");
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Boolean createTradingDeal(String username, String trade_id, String card_id, String type, String dmg){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO trade_deals
                (trade_id, trading_card_id, type, damage)
                VALUES (?, ?, ?, ?);
                """ )
        ) {
            double damage = Double.parseDouble(dmg);

            statement.setString(1, trade_id );
            statement.setString(2, card_id );
            statement.setString(3, type );
            statement.setDouble(4, damage );
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Boolean tradeCards(String username, String trade_id, String card_id){
        String card_id_from_trade = getCardIdFromTradeDeal(trade_id);
        String username2 = getUsernameFromCard(card_id_from_trade);

        if (checkIfCardBelongsToUser(username, card_id_from_trade)){
            //return "Cant trade with yourself";
            System.out.println("Cant trade with yourself");
            return false;
        }else {
            if(checkIfCardMeetsRequirements(trade_id, card_id)){
                //gets card id by trade id from specific trade
                //Cards to trade:
                //card1 = (username1, card_id) --- card2 = (username2, card_id_from_trade)
                //To: card1(username2, card_id_from_trade) --- card2(username1, card_id)
                if(tradeCard(username, card_id_from_trade) && tradeCard(username2, card_id)){
                    //return "Trade successful";
                    return true;
                }
            }else {
                //return "card doesnt meet requirements";
                System.out.println("card doesnt meet requirements");
                return false;
            }
        }
        return false;
    }
    public boolean tradeCard(String username, String card_id){
        //Boolean query_status = false;
            try (PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                    UPDATE cards
                    SET username = ?,
                        isDeck = false
                    WHERE id = ?
                    """)
            ) {
                statement.setString(1, username);
                statement.setString(2, card_id);
                statement.execute();
                //if user exists and name and password right
/*                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next() ) {
                    query_status = true;
                }*/
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        return false;
    }
    public Boolean checkIfCardMeetsRequirements(String trade_id, String card_id){
        String type = null;
        double damage = -1;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT type, damage
                FROM trade_deals
                WHERE trade_id=?
                """)
        ) {
            statement.setString( 1, trade_id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                type = resultSet.getString(1);
                damage = resultSet.getDouble(2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Trademarket card = getCardDetails(card_id);
        if(type != null && damage >= -1){
            String card_type = "";
            if(!card.getType().toLowerCase(Locale.ROOT).contains("spell")){
                card_type = "monster";
            }else {
                card_type = card.getType().toLowerCase(Locale.ROOT);
            }
            if (type.equals(card_type) && damage <= card.getDmg()){
                return true;
            }{
                return false;
            }
        }
        return false;
    }

    public Trademarket getCardDetails(String card_id){
         Trademarket card = new Trademarket();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT type, damage
                FROM cards
                WHERE id=?
                """)
        ) {
            statement.setString( 1, card_id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                card.setType(resultSet.getString(1));
                card.setDmg(resultSet.getDouble( 2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return card;
    }

    public String getCardIdFromTradeDeal(String trade_id){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT trading_card_id
                FROM trade_deals
                WHERE trade_id=?
                """)
        ) {
            statement.setString( 1, trade_id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public String getUsernameFromCard(String card_id){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT username
                FROM cards
                WHERE id=?
                """)
        ) {
            statement.setString( 1, card_id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public Boolean deleteTradingDeal(String trade_id){
            try (PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                    DELETE FROM trade_deals
                    WHERE trade_id = ?;
                    """)
            ) {
                statement.setString(1, trade_id);
                statement.execute();
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return false;
    }

    public List<Card> getUserCards(String username){
        List<Card> userCards = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT name, element, damage
                FROM cards
                Where userName = ?
                """)
        ) {
            statement.setString(1, username );
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                userCards.add( new Card(
                        resultSet.getString(1),
                        //TODO check if toUpperCase changed something!!!!!!
                        ElementType.valueOf(resultSet.getString( 2).toUpperCase(Locale.ROOT)),
                        resultSet.getDouble( 3)
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userCards;
    }
    public List<Card> getUserDeck(String username){
        List<Card> userDeck = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT name, element, damage
                FROM cards
                Where userName = ? AND isDeck = true
                """)
        ) {
            statement.setString(1, username );
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                userDeck.add( new Card(
                        resultSet.getString(1),
                        ElementType.valueOf(resultSet.getString( 2)),
                        resultSet.getDouble( 3)
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userDeck;
    }

    public boolean createUserDeck(String[] cardIds, String userName){
        Boolean query_status = false;
        for (int i = 0; i < cardIds.length; i++) {
            System.out.println("DB QUERY CARD: " + cardIds[i]);
            try (PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                    UPDATE cards
                    SET isDeck = true
                    WHERE id = ? AND userName = ?
                    """)
            ) {
                statement.setString(1, cardIds[i]);
                statement.setString(2, userName);
                statement.execute();
                //if user exists and name and password right
/*                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next() ) {
                    query_status = true;
                }*/
                query_status = true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return query_status;
    }

    public void addPackage(String cardId){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO packages
                (card_id)
                VALUES (?);
                """ )
        ) {
            statement.setString(1, cardId );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void acquirePackage(String userName){
        ArrayList<String> cardIds = new ArrayList<>();

        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT card_id FROM packages LIMIT 5;
                """ )
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                cardIds.add(resultSet.getString(1));
            }
            //statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (int i = 0; i < cardIds.size(); i++){
            try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE cards
                SET userName = ?
                WHERE id = ?;
                """ )
            ) {
                statement.setString(1, userName);
                statement.setString( 2, cardIds.get(i));
                statement.execute();
                //Delete package
                deletePackage(cardIds);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //remove 5 coins from User coins
        subtractCoins(userName);

    }

    public void subtractCoins(String username){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users
                SET coins = coins - 5
                WHERE username = ?;
                """ )
        ) {
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addCoins(String username){
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users
                SET coins = coins + 5
                WHERE username = ?;
                """ )
        ) {
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deletePackage(ArrayList<String> card_ids){
        for (int i = 0; i < card_ids.size(); i++) {
            try (PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                    DELETE FROM packages
                    WHERE card_id = ?;
                    """)
            ) {
                statement.setString(1, card_ids.get(i));
                statement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public int getUserCoins(String username){
        int coins = 0;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT coins
                FROM users
                WHERE username=?
                """)
        ) {
            statement.setString( 1, username );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                coins = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return coins;
    }

    //TODO maybe remove password from getUser?
    @Override
    public User getUser(String username) {
        String userName = "", password = "";
        int coins = 0, eloPoints = 0, gamesPlayed = 0, wins = 0, losses = 0;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT userName, password, coins, eloPoints, gamesPlayed, wins, losses
                FROM users
                WHERE username=?
                """)
        ) {
            statement.setString( 1, username );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                userName = resultSet.getString(1);
                password = resultSet.getString(2);
                coins = resultSet.getInt(3);
                eloPoints = resultSet.getInt(4);
                gamesPlayed = resultSet.getInt(5);
                wins = resultSet.getInt(6);
                losses = resultSet.getInt(7);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new User(userName, password, coins, eloPoints, gamesPlayed, wins, losses);
    }

    public String[] getUserProfile(String username) {
        String[] profile = new String[3];
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT profilename, bio, image
                FROM users
                WHERE username=?
                """)
        ) {
            statement.setString( 1, username );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                System.out.println("RESULT::" + resultSet.getString(1));
                if (resultSet.getString(1) == null){
                    profile[0] = "";
                }else {
                    profile[0] = resultSet.getString(1);
                }

                if (resultSet.getString(2) == null){
                    profile[1] = "";
                }else {
                    profile[1] = resultSet.getString(2);
                }

                if (resultSet.getString(3) == null){
                    profile[2] = "";
                }else {
                    profile[2] = resultSet.getString(3);
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return profile;
    }

    public boolean updateUserProfile(String username, String profilename,String bio, String image) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users
                SET profilename = ?, bio = ?, image = ?
                WHERE username = ?;
                """)
        ) {
            statement.setString(1, profilename );
            statement.setString( 2, bio );
            statement.setString(3, image );
            statement.setString(4, username );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean checkUserExistence(String username) {
        Boolean status = false;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT 1
                FROM users
                WHERE username=?
                LIMIT 1;
                """)
        ) {
            statement.setString( 1, username );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                status = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

    public List<UserScores> getScoreboard(String username){
        List<UserScores> userScores = new ArrayList<>();

        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT userName, eloPoints
                FROM users
                ORDER BY eloPoints DESC
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                userScores.add( new UserScores(
                        resultSet.getString(1),
                        resultSet.getInt( 2 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userScores;
    }


    @Override
    public void saveUser(UserData userData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO users
                (userName, password, coins, isLoggedIn, eloPoints, gamesPlayed, wins, losses)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """ )
        ) {
            statement.setString(1, userData.getUserName() );
            statement.setString( 2, userData.getPassword() );
            statement.setInt( 3, userData.getCoins() );
            statement.setBoolean( 4, userData.getLogInStatus() );
            statement.setInt( 5, userData.getEloPoints() );
            statement.setInt( 6, userData.getGamesPlayed() );
            statement.setInt( 7, userData.getWins() );
            statement.setInt( 8, userData.getLosses() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateUser(UserData userData, String[] params) {
        // update the item
        userData.setUserName(Objects.requireNonNull( params[1], "Username cannot be null" ) );
        userData.setPassword( Objects.requireNonNull( params[2], "Password cannot be null" ) );

        // persist the updated item
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users
                SET userName = ?, password = ?
                WHERE username = ?;
                """)
        ) {
            statement.setString(1, userData.getUserName() );
            statement.setString( 2, userData.getPassword() );
            statement.setString(3, userData.getUserName() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean logInOutUser(Boolean status, String username, String password) {
        Boolean query_status = false;
        // persist the updated item
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users
                SET isLoggedIn = ?
                WHERE username = ? AND
                      password = ?
                      returning username;
                """)
        ) {
            statement.setBoolean(1, status);
            statement.setString( 2, username);
            statement.setString(3, password);
            //statement.execute();
            ResultSet resultSet = statement.executeQuery();
            //if user exists and name and password right
            if(resultSet.next() ) {
                String userName = resultSet.getString(1);
                System.out.println("USERNAME BY DB: "+ userName);
                query_status = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return query_status;
    }

    public Boolean isLogedIn(String username){
        Boolean logIn_status = false;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT isLoggedIn
                FROM users
                WHERE username=?
                """)
        ) {
            statement.setString( 1, username );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                logIn_status = resultSet.getBoolean(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return logIn_status;
    }

    public void deleteClosedBattle(String initiator, String joiner) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                DELETE FROM current_battles
                WHERE initiating_user = ? AND joining_user = ?;
                """)
        ) {
            statement.setString( 1, initiator );
            statement.setString( 2, joiner );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void getCard(CardData monsterCardData) {

    }

    @Override
    public void saveCard(CardData cardData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO cards
                (id, name, damage, element, type, isDeck)
                VALUES (?, ?, ?, ?, ?, ?);
                """ )
        ) {
            statement.setString( 1, cardData.getId());
            statement.setString(2, cardData.getName());
            statement.setDouble( 3, cardData.getDAMAGE());
            statement.setString(4,cardData.getElement());
            statement.setString(5,cardData.getType());
            statement.setBoolean(6,cardData.getIsDeck());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void updateCard(CardData monsterCardData, String[] params) {

    }

    @Override
    public void deleteCard(CardData monsterCardData) {

    }



}
