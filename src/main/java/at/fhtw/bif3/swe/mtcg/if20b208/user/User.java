package at.fhtw.bif3.swe.mtcg.if20b208.user;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
public class User {
    private String username;
    private String password;
    private int coins;
    //private List<Card> stack;
    //private List<Card> deck = new ArrayList<>();
    private int eloPoints = 100;
    private int gamesPlayed = 0;
    private int wins = 0;
    private int losses = 0;
    //private List<UserHistory> history = new ArrayList();
    //private Stack stack;

    public User(String username, String password, int coins, int eloPoints, int gamesPlayed, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.eloPoints = eloPoints;
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
    }

}
