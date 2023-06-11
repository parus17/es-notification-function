package be.parus17.enersmart.battery.powerflow;

import be.parus17.enersmart.battery.powerflow.model.BatteryPowerFlowResponse;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import java.util.Optional;

public class BatteryPowerFlowHandler extends FunctionInvoker<Optional<?>, BatteryPowerFlowResponse> {

    @FunctionName("batteryPowerFlow")
    public BatteryPowerFlowResponse execute(
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