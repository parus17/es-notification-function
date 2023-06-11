package be.parus17.enersmart.battery.info.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatteryInfoResponse {
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private double nameplateCapacity;
    private String sn;
    private InverterInfo inverter;
}
