package be.parus17.enersmart.battery.notification.model.solaredge.powerflow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pv {
    private String status;
    private float currentPower;
}
