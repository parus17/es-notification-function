package be.parus17.enersmart.battery.notification;

import be.parus17.enersmart.battery.notification.model.NotificationStatus;
import be.parus17.enersmart.battery.notification.model.StorageInfo;
import be.parus17.enersmart.battery.notification.model.solaredge.powerflow.PowerFlowResponse;
import be.parus17.enersmart.battery.notification.model.solaredge.powerflow.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class BatteryNotification implements Consumer<Void> {
    private final NotificationStatusRepository notificationStatusRepository;

    @Override
    public void accept(Void unused) {
        System.out.println("accept");

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
                    }
                });
//        NotificationStatus previousNotificationStatus = retrievePreviousNotificationStatus();
//        System.out.println(previousNotificationStatus);
//
//        NotificationStatus newNotificationStatus = new NotificationStatus();
//        newNotificationStatus.setTimestamp(LocalDateTime.now());
//
//        Notification notification = null;
//
//        if (storageInfo.getChargeLevel() > 90) {
//            if (Objects.isNull(previousNotificationStatus.getTreshold90())) {
//                newNotificationStatus.setTreshold90(LocalDate.now());
//                notification = Notification.builder()
//                        .value1("Battery charge level > 90%")
//                        .build();
//            }
//        }
//
//        if (storageInfo.getChargeLevel() < 30) {
//            if (Objects.isNull(previousNotificationStatus.getTreshold30())) {
//                newNotificationStatus.setTreshold30(LocalDate.now());
//                notification = Notification.builder()
//                        .value1("Battery charge level < 30%")
//                        .build();
//            }
//        }
//
////        if (Objects.nonNull(notification)) {
////            sendNotification(notification);
////        }
//
//        System.out.println(newNotificationStatus);
//        storeNewNotificationStatus(newNotificationStatus);
    }
}
