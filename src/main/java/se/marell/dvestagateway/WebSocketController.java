/*
 * Created by Daniel Marell 03/03/16.
 */
package se.marell.dvestagateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import se.marell.dvestagateway.apimodel.SystemConnectMessage;
import se.marell.dvestagateway.apimodel.SystemMessage;
import se.marell.dvestagateway.apimodel.SystemMessageResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class WebSocketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<SystemMessageResponseListenerEntry> responseListeners = new CopyOnWriteArrayList<>();

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("system-connect")
    public void systemConnect(@Payload SystemConnectMessage message) {
        logger.info("systemConnect, message: {}", message);
    }

    @MessageMapping("system-message-response")
    public void systemMessageResponse(@Payload SystemMessage message) {
        logger.info("systemMessageResponse, message: {}", message);
        notifyAndRemoveResponseListeners(message);
    }

    private void notifyAndRemoveResponseListeners(SystemMessage response) {
        List<SystemMessageResponseListenerEntry> listeners = new ArrayList<>(responseListeners);
        for (SystemMessageResponseListenerEntry e : listeners) {
            if (e.getMessageId().equals(response.getMessageId())) {
                e.getListener().responseReceived(response.getMessageBody());
                responseListeners.remove(e);
            }
        }
    }

    public String sendSystemMessage(String systemId, String messageBody) {
        logger.info("sendSystemMessage, systemId: {}, message: {}", systemId, messageBody);
        String messageId = UUID.randomUUID().toString();
        template.convertAndSendToUser(systemId,
                "/system-message-request",
                new SystemMessage(messageId, messageBody));
        return messageId;
    }

    public void addSystemMessageResponseListener(String messageId, SystemMessageResponseListener listener) {
        responseListeners.add(new SystemMessageResponseListenerEntry(messageId, listener));
    }

    @Data
    @AllArgsConstructor
    private static class SystemMessageResponseListenerEntry {
        String messageId;
        SystemMessageResponseListener listener;
    }
}
