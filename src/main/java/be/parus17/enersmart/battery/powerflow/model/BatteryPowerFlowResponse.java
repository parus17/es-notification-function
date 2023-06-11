package be.parus17.enersmart.battery.powerflow.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatteryPowerFlowResponse {
    private String status;
    private float currentPower;
    private float chargeLevel;
}
