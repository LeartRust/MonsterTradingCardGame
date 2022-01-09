package at.fhtw.bif3.swe.mtcg.if20b208.database;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.MonsterCardData;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.SpellCardData;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.UserData;
import at.fhtw.bif3.swe.mtcg.if20b208.user.Stack;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MTCGDaoDb implements Dao<UserData, MonsterCardData, SpellCardData>{

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
                    CREATE TABLE IF NOT EXISTS players (
                        
                        id INT PRIMARY KEY,
                        userName TEXT NOT NULL UNIQUE,
                        password TEXT NOT NULL,
                        coins INT NOT NULL,
                        eloPoints INT NOT NULL,
                        gamesPlayed INT NOT NULL,
                        wins INT NOT NULL,
                        losses INT NOT NULL
                    );
                    
                    CREATE TABLE IF NOT EXISTS monsterCards (
                        id INT PRIMARY KEY,
                        name TEXT NOT NULL,
                        element TEXT NOT NULL,
                        monsterType TEXT NOT NULL,
                        damage INT NOT NULL,
                        user_id INT NOT NULL,
                        CONSTRAINT players
                              FOREIGN KEY(user_id)
                        	  REFERENCES players(id) ON DELETE CASCADE
                    );
                    
                    CREATE TABLE IF NOT EXISTS spellCards(
                        id INT PRIMARY KEY,
                        name TEXT NOT NULL,
                        element TEXT NOT NULL,
                        damage INT NOT NULL,
                        user_id INT NOT NULL,
                        CONSTRAINT players
                              FOREIGN KEY(user_id)
                        	  REFERENCES players(id) ON DELETE CASCADE
                    );
                    """);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public User getUser(int id) {
        String userName = "", password = "";
        int coins = 0, eloPoints = 0, gamesPlayed = 0, wins = 0, losses = 0;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userName, password, coins, eloPoints, gamesPlayed, wins, losses
                FROM players
                WHERE id=?
                """)
        ) {
            statement.setInt( 1, id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                userName = resultSet.getString(2);
                password = resultSet.getString(3);
                coins = resultSet.getInt(4);
                eloPoints = resultSet.getInt(5);
                gamesPlayed = resultSet.getInt(6);
                wins = resultSet.getInt(7);
                losses = resultSet.getInt(8);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new User(userName, password, coins, eloPoints, gamesPlayed, wins, losses);
    }



    @Override
    public Collection<UserData> getAllUsers() {
        ArrayList<UserData> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userName, password, coins, eloPoints, gamesPlayed, wins, losses
                FROM players
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                result.add( new UserData(
                        resultSet.getInt(1),
                        resultSet.getString( 2 ),
                        resultSet.getString( 3 ),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8)
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveUser(UserData userData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO players
                (id, userName, password, coins, eloPoints, gamesPlayed, wins, losses)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """ )
        ) {
            statement.setInt( 1, userData.getId() );
            statement.setString(2, userData.getUserName() );
            statement.setString( 3, userData.getPassword() );
            statement.setInt( 4, userData.getCoins() );
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
        userData.setId( Integer.parseInt(Objects.requireNonNull( params[0], "Id cannot be null") ) );
        userData.setUserName(Objects.requireNonNull( params[1], "Username cannot be null" ) );
        userData.setPassword( Objects.requireNonNull( params[2], "Password cannot be null" ) );

        // persist the updated item
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE players
                SET userName = ?, password = ?
                WHERE id = ?;
                """)
        ) {
            statement.setString(1, userData.getUserName() );
            statement.setString( 2, userData.getPassword() );
            statement.setInt(3, userData.getId() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteUser(UserData userData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                DELETE FROM players
                WHERE id = ?;
                """)
        ) {
            statement.setInt( 1, userData.getId() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void getMonsterCard(MonsterCardData monsterCardData) {

    }

    @Override
    public Collection<MonsterCardData> getAllMonsterCards() {
        ArrayList<MonsterCardData> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, name, monsterType, element, damage, user_id
                FROM monsterCards
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                result.add( new MonsterCardData(
                        resultSet.getInt(1),
                        resultSet.getString( 2 ),
                        resultSet.getString( 3 ),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveMonsterCard(MonsterCardData monsterCardData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO monsterCards
                (id, name, monsterType, element, damage, user_id)
                VALUES (?, ?, ?, ?, ?, ?);
                """ )
        ) {
            statement.setInt( 1, monsterCardData.getId() );
            statement.setString(2, monsterCardData.getName() );
            statement.setString( 3, monsterCardData.getMonsterType() );
            statement.setString( 4, monsterCardData.getElement() );
            statement.setDouble( 5, monsterCardData.getDAMAGE() );
            statement.setInt(6,monsterCardData.getUserId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateMonsterCard(MonsterCardData monsterCardData, String[] params) {

    }

    @Override
    public void deleteMonsterCard(MonsterCardData monsterCardData) {

    }

    @Override
    public void getSpellCard(SpellCardData spellCardData) {

    }

    @Override
    public void getAllSpellCards(SpellCardData spellCardData) {

    }

    @Override
    public void saveSpellCard(SpellCardData spellCardData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO spellCards
                (id, name, element, damage, user_id)
                VALUES (?, ?, ?, ?, ?);
                """ )
        ) {
            statement.setInt( 1, spellCardData.getId() );
            statement.setString(2, spellCardData.getName() );
            statement.setString( 3, spellCardData.getElement() );
            statement.setDouble( 4, spellCardData.getDAMAGE() );
            statement.setInt(5,spellCardData.getUserId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateSpellCard(SpellCardData spellCardData, String[] params) {

    }

    @Override
    public void deleteSpellCard(SpellCardData spellCardData) {

    }

    public ArrayList<Card> createDeck(int user_id){

        ArrayList<Card> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, name, monsterType, element, damage, user_id
                FROM monsterCards
                WHERE user_id = ?
                """)
        ) {
            statement.setInt( 1, user_id );
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
               //TODO how to distinguish between monster and spell!!
                MonsterCard card = new MonsterCard(resultSet.getString(2), ElementType.valueOf(resultSet.getString(4)), resultSet.getInt(5), MonsterType.valueOf(resultSet.getString(3)));
                result.add(card);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, name, element, damage, user_id
                FROM spellCards
                WHERE user_id = ?
                """)
        ) {
            statement.setInt( 1, user_id );
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                SpellCard card = new SpellCard(resultSet.getString(2), ElementType.valueOf(resultSet.getString(3)), resultSet.getInt(4));
                result.add(card);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }



}
