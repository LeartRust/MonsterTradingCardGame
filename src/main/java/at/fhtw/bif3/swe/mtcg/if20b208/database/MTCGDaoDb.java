package at.fhtw.bif3.swe.mtcg.if20b208.database;

import at.fhtw.bif3.swe.mtcg.if20b208.database.model.MTCGData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class MTCGDaoDb implements Dao<MTCGData>{

    public static void initDb() {
        // re-create the database
        try (Connection connection = DbConnection.getInstance().connect("")) {
            DbConnection.executeSql(connection, "DROP DATABASE mtcg_database", true );
            DbConnection.executeSql(connection,  "CREATE DATABASE mtcg_database", true );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // create the table
        // PostgreSQL documentation: https://www.postgresqltutorial.com/postgresql-create-table/
        try {
            DbConnection.getInstance().executeSql("""
                CREATE TABLE IF NOT EXISTS players (
                    
                    id INT PRIMARY KEY,
                    userName VARCHAR(50) NOT NULL,
                    password VARCHAR(50) NOT NULL
                )
                """);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<MTCGData> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userName, password 
                FROM players 
                WHERE id=?
                """)
        ) {
            statement.setInt( 1, id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                return Optional.of( new MTCGData(
                        resultSet.getInt(1),
                        resultSet.getString( 2 ),
                        resultSet.getString( 3 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }



    @Override
    public Collection<MTCGData> getAll() {
        ArrayList<MTCGData> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userName, password 
                FROM players 
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                result.add( new MTCGData(
                        resultSet.getInt(1),
                        resultSet.getString( 2 ),
                        resultSet.getString( 3 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void save(MTCGData mtcgData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO players 
                (id, userName, password) 
                VALUES (?, ?, ?);
                """ )
        ) {
            statement.setInt( 1, mtcgData.getId() );
            statement.setString(2, mtcgData.getUserName() );
            statement.setString( 3, mtcgData.getPassword() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(MTCGData mtcgData, String[] params) {
// update the item
        mtcgData.setId( Integer.parseInt(Objects.requireNonNull( params[0], "Id cannot be null" ) ) );
        mtcgData.setUserName(Objects.requireNonNull( params[1], "Username cannot be null" ));
        mtcgData.setPassword( Objects.requireNonNull( params[2], "Password cannot be null" ) );

        // persist the updated item
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE players 
                SET id = ?, userName = ?, password = ? 
                WHERE id = ?;
                """)
        ) {
            statement.setInt(1, mtcgData.getId() );
            statement.setString(2, mtcgData.getUserName() );
            statement.setString( 3, mtcgData.getPassword() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(MTCGData mtcgData) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                DELETE FROM players 
                WHERE id = ?;
                """)
        ) {
            statement.setInt( 1, mtcgData.getId() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
