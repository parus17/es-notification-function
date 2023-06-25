package be.parus17.enersmart;

import be.parus17.enersmart.model.Notification;
import be.parus17.enersmart.model.NotificationStatus;
import be.parus17.enersmart.model.StorageInfo;
import be.parus17.enersmart.model.solaredge.powerflow.PowerFlowResponse;
import be.parus17.enersmart.model.solaredge.powerflow.Storage;
import com.microsoft.azure.functions.ExecutionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
class BatteryNotification implements Consumer<Message<String>> {
    private final NotificationStatusRepository notificationStatusRepository;

    private final List<String> interestedDeviceUrls = Arrays.asList(
            "https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/cUkiWNvA9P7AqdwmyjzWET",
            "https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/bMWxig56Wvd4J4LEBLUdgQ",
            "https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/0hRdNZLaQeJod_nhOHcqw"
    );

    @Override
    public void accept(Message<String> message) {
        // (Optionally) access and use the Azure function context.
        Logger logger = getLogger(message);

        // /timeInfo is a JSON string, you can deserialize it to an object using your favorite JSON library
        String timeInfo = message.getPayload();

        // Business logic -> convert the timeInfo to uppercase.
        String value = timeInfo.toUpperCase();

        logger.info("Timer is triggered with TimeInfo: " + value);

        // No response.

        WebClient batteryWebClient = WebClient.builder()
                .baseUrl("https://monitoringapi.solaredge.com/site/1453155")
                .build();

        batteryWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/currentPowerFlow")
                        .queryParam("api_key", "9U5TH6DR27F7YMPNX0BCYTG58TC9CIK3")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PowerFlowResponse.class)
                .map(BatteryNotification::mapToStorageInfo)
                .subscribe(this::processStorageInfo);
    }

    private static Logger getLogger(Message<String> message) {
        ExecutionContext context = (ExecutionContext) message.getHeaders().get("executionContext");
        return context.getLogger();
    }

    private static StorageInfo mapToStorageInfo(PowerFlowResponse response) {
        // TODO improve
        Storage storage = response.getSiteCurrentPowerFlow().getStorage();

        return StorageInfo.builder()
                .status(storage.getStatus())
                .currentPower(storage.getCurrentPower())
                .chargeLevel(storage.getChargeLevel())
                .build();
    }

    private void processStorageInfo(StorageInfo storageInfo) {
        notificationStatusRepository.findOne()
                .switchIfEmpty(Mono.just(new NotificationStatus()))
                .subscribe(previousNotificationStatus -> {
                    NotificationStatus newNotificationStatus = new NotificationStatus();

                    if (storageInfo.getChargeLevel() > 90) {
                        newNotificationStatus.setTreshold90(true);
                    }

                    if (storageInfo.getChargeLevel() < 30) {
                        newNotificationStatus.setTreshold30(true);
                    }

                    if (!previousNotificationStatus.equals(newNotificationStatus)) {
                        notificationStatusRepository.save(newNotificationStatus).subscribe();

                        Notification notification = null;
                        if (newNotificationStatus.isTreshold90()) {
                            notification = Notification.builder()
                                    .value1("Battery charge level > 90%")
                                    .build();
                        }

                        if (newNotificationStatus.isTreshold30()) {
                            notification = Notification.builder()
                                    .value1("Battery charge level < 30%")
                                    .build();
                        }

                        if (Objects.nonNull(notification)) {
                            sendNotification(notification);
                        }
                    }
                });
    }

    private void sendNotification(Notification notification) {
        interestedDeviceUrls.parallelStream().forEach(interestedDeviceUrl -> {
            WebClient notificationWebClient = WebClient.builder()
                    .baseUrl(interestedDeviceUrl)
                    .build();

            notificationWebClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(notification))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();
        });
    }
}
