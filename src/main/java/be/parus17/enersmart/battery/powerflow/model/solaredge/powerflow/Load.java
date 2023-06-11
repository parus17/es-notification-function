package be.parus17.enersmart.battery.powerflow.model.solaredge.powerflow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Load {
    private String status;
    private float currentPower;
}
