package be.parus17.enersmart.battery.notification;

import be.parus17.enersmart.battery.notification.model.NotificationStatus;
import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface NotificationStatusRepository extends ReactiveCosmosRepository<NotificationStatus, String> {
    @Query(value = "SELECT top 1 * FROM c where c.type = 'es-notification-status' order by c._ts desc")
    Mono<NotificationStatus> findOne();
}
