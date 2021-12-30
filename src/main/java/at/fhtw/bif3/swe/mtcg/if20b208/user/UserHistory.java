package at.fhtw.bif3.swe.mtcg.if20b208.user;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserHistory {

    private String matchOutcome;
    private String opponent;

    public UserHistory(String matchOutcome, String opponent) {
        this.matchOutcome = matchOutcome;
        this.opponent = opponent;
    }
}
