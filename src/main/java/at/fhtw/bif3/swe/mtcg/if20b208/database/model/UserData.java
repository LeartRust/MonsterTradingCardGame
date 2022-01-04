package at.fhtw.bif3.swe.mtcg.if20b208.database.model;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.Card;
import at.fhtw.bif3.swe.mtcg.if20b208.user.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserData implements Serializable {

        private Integer id;
        private String userName;
        private String password;
        private Integer coins;
        private Integer eloPoints;
        private Integer gamesPlayed;
        private Integer wins;
        private Integer losses;
/*
        private List<Card> stack;
        private List<Card> deck = new ArrayList<>();
        private List<UserHistory> history = new ArrayList();*/

}
