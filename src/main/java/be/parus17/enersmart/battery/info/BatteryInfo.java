package be.parus17.enersmart.battery.info;

import be.parus17.enersmart.battery.info.model.BatteryInfoResponse;
import be.parus17.enersmart.battery.info.model.solaredge.inventory.Battery;
import be.parus17.enersmart.battery.info.model.solaredge.inventory.InventoryResponse;
import be.parus17.enersmart.battery.info.model.InverterInfo;
import be.parus17.enersmart.battery.info.model.solaredge.inventory.Inverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Supplier;

@Component
public class BatteryInfo implements Supplier<BatteryInfoResponse> {
    private ObjectMapper objectMapper;

    WebClient webClient = WebClient.builder()
            .baseUrl("https://monitoringapi.solaredge.com/site/1453155")
            .build();

    @Override
    public BatteryInfoResponse get() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/inventory")
                        .queryParam("api_key", "9U5TH6DR27F7YMPNX0BCYTG58TC9CIK3")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .map(response -> {
                    // TODO improve
                    Battery battery = response.getInventory().getBatteries()[0];
                    Inverter inverter = response.getInventory().getInverters()[0];

                    return BatteryInfoResponse.builder()
                            .manufacturer(battery.getManufacturer())
                            .model(battery.getModel())
                            .firmwareVersion(battery.getFirmwareVersion())
                            .nameplateCapacity(battery.getNameplateCapacity())
                            .sn(battery.getSn())
                            .inverter(InverterInfo.builder()
                                    .manufacturer(inverter.getManufacturer())
                                    .model(inverter.getModel())
                                    .communicationMethod(inverter.getCommunicationMethod())
                                    .cpuVersion(inverter.getCpuVersion())
                                    .sn(inverter.getSn())
                                    .connectedOptimizers(inverter.getConnectedOptimizers())
                                    .build())
                            .build();
                })
                .block();
    }
}
