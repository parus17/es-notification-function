package be.parus17.enersmart.battery.notification;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import java.util.Optional;

public class BatteryNotificationHandler extends FunctionInvoker<Optional<?>, Void> {

    @FunctionName("batteryNotification")
    public void execute(
            @TimerTrigger(name = "keepAliveTrigger", schedule = "0 */1 * * * *") String timerInfo,
            ExecutionContext context
    ) {
        context.getLogger().warning("Using Java (" + System.getProperty("java.version") + ")");
        handleRequest(Optional.empty(), context);
    }
}