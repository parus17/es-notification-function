package be.parus17.enersmart;

import com.microsoft.azure.functions.ExecutionContext;
import org.springframework.cloud.function.json.JsonMapper;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class Uppercase implements Function<Message<String>, String> {
    private final JsonMapper mapper;

    public Uppercase(JsonMapper jsonMapper) {
        this.mapper = jsonMapper;
    }

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
}
