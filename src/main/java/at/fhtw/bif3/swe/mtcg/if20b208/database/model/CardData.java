package at.fhtw.bif3.swe.mtcg.if20b208.database.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CardData implements Serializable {
    private String id;
    private String name;
    private String element;
    private String type;
    private double DAMAGE;
    private String userName;
    private Boolean isDeck;


}
