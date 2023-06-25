package be.parus17.enersmart.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Container(containerName = "various")
public class NotificationStatus {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private String id;
    @PartitionKey
    private String type = "es-notification-status";

    private boolean treshold90;
    private boolean treshold30;
}