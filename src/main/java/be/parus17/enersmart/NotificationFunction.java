/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.parus17.enersmart;

import java.util.Map;
import java.util.function.Function;

import com.microsoft.azure.functions.ExecutionContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

/**
 * @author Oleg Zhurakousky
 * @author Chris Bono
 */
@SpringBootApplication
public class NotificationFunction {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NotificationFunction.class, args);
	}

//	@Bean
	public Function<Message<String>, String> uppercase(JsonMapper mapper) {
		return new Function<Message<String>, String>() {
			@Override
			public String apply(Message<String> message) {
				String value = message.getPayload();
				ExecutionContext context = (ExecutionContext) message.getHeaders().get("executionContext");
				try {
					Map<String, String> map = mapper.fromJson(value, Map.class);

					if (map != null)
						map.forEach((k, v) -> map.put(k, v != null ? v.toUpperCase() : null));

					if (context != null)
						context.getLogger().info(new StringBuilder().append("Function: ")
								.append(context.getFunctionName()).append(" is uppercasing ").append(value.toString()).toString());

					return mapper.toString(map);
				} catch (Exception e) {
					e.printStackTrace();
					if (context != null)
						context.getLogger().severe("Function could not parse incoming request");

					return ("Function error: - bad request");
				}
			}
		};
	}
}

