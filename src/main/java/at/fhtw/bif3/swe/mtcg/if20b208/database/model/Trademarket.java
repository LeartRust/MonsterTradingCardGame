package at.fhtw.bif3.swe.mtcg.if20b208.database.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonAutoDetect
public class Trademarket {

    private String id;
    @JsonProperty
    private String card_id;
    @JsonProperty
    private String card;
    private String element;
    @JsonProperty
    private String type;
    @JsonProperty
    private double dmg;

    @JsonCreator
    public Trademarket(@JsonProperty("CardToTrade") String card_id,  @JsonProperty("Type") String type,@JsonProperty("MinimumDamage") double requiredMinDamage){
        this.card_id = card_id;
        this.dmg = requiredMinDamage;
        this.type = type;
    }



}
