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
    private int coins = 20;
    private List<Card> stack;
    private List<Card> deck = new ArrayList<>();
    private int eloPoints = 100;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private List<UserHistory> history = new ArrayList();
    //private Stack stack;

    public User(String username, String password, List<Card> stack) {
        this.username = username;
        this.password = password;
        this.stack = stack;
    }

}
