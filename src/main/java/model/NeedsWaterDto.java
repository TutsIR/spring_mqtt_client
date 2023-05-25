package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeedsWaterDto {

    @JsonProperty("needs_water")
    private boolean needsWater;

}
