package be.parus17.enersmart.model.solaredge.powerflow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Grid {
    private String status;
    private float currentPower;
}