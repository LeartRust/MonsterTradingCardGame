package at.fhtw.bif3.swe.mtcg.if20b208.database.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonAutoDetect
public class PackageData {
    @JsonProperty
    private String Id;
    @JsonProperty
    private String Name;
    @JsonProperty
    private double Damage;
}
