package be.parus17.enersmart.battery.powerflow;

import be.parus17.enersmart.battery.powerflow.model.BatteryPowerFlowResponse;
import be.parus17.enersmart.battery.powerflow.model.solaredge.powerflow.PowerFlowResponse;
import be.parus17.enersmart.battery.powerflow.model.solaredge.powerflow.Storage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Supplier;

@Component
public class BatteryPowerFlow implements Supplier<BatteryPowerFlowResponse> {
    private ObjectMapper objectMapper;

    WebClient webClient = WebClient.builder()
            .baseUrl("https://monitoringapi.solaredge.com/site/1453155")
            .build();

    @Override
    public BatteryPowerFlowResponse get() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/currentPowerFlow")
                        .queryParam("api_key", "9U5TH6DR27F7YMPNX0BCYTG58TC9CIK3")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PowerFlowResponse.class)
                .map(response -> {
                    // TODO improve
                    Storage storage = response.getSiteCurrentPowerFlow().getStorage();

                    return BatteryPowerFlowResponse.builder()
                            .status(storage.getStatus())
                            .currentPower(storage.getCurrentPower())
                            .chargeLevel(storage.getChargeLevel())
                            .build();
                })
                .block();
    }
}
