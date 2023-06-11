package be.parus17.enersmart.battery.info;

import be.parus17.enersmart.battery.info.model.BatteryInfoResponse;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import java.util.Optional;

public class BatteryInfoHandler extends FunctionInvoker<Optional<?>, BatteryInfoResponse> {

    @FunctionName("batteryInfo")
    public BatteryInfoResponse execute(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Void> request,
            ExecutionContext context
    ) {
        context.getLogger().warning("Using Java (" + System.getProperty("java.version") + ")");
        return handleRequest(Optional.empty(), context);
    }
}