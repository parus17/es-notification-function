package be.parus17.enersmart;

import java.util.function.Consumer;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableCosmosRepositories
public class EsNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsNotificationApplication.class, args);
	}

}
