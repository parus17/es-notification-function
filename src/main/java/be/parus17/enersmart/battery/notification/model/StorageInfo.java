package be.parus17.enersmart.battery.notification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StorageInfo {
    private String status;
    private float currentPower;
    private float chargeLevel;
}
