package at.fhtw.bif3.swe.mtcg.if20b208.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserData implements Serializable {

        private String userName;
        private String password;
        private Integer coins;
        private Boolean logInStatus;
        private Integer eloPoints;
        private Integer gamesPlayed;
        private Integer wins;
        private Integer losses;

}
