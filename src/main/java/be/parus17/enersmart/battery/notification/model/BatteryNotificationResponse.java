package be.parus17.enersmart.battery.notification.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatteryNotificationResponse {
    private String status;
    private float currentPower;
    private float chargeLevel;
}
