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
import se.marell.dvestagateway.apimodel.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class WebSocketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<SystemMessageResponseListenerEntry> stringMessageResponseListeners = new CopyOnWriteArrayList<>();
    private List<ByteDataMessageResponseListenerEntry> byteDataMessageResponseListeners = new CopyOnWriteArrayList<>();

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("system-connect")
    public void systemConnect(@Payload SystemConnectMessage message) {
        logger.info("systemConnect, message: {}", message);
    }

    @MessageMapping("system-message-response")
    public void systemMessageResponse(@Payload SystemMessage message) {
        logger.info("systemMessageResponse, message: {}", message);
        notifyAndRemoveSystemMessageResponseListeners(message);
    }

    @MessageMapping("byte-data-message-response")
    public void byteDataMessageResponse(@Payload ByteDataMessage message) {
        logger.info("byteDataMessageResponse, message: {}", message);
        notifyAndRemoveByteDataMessageResponseListeners(message);
    }

    private void notifyAndRemoveSystemMessageResponseListeners(SystemMessage response) {
        List<SystemMessageResponseListenerEntry> listeners = new ArrayList<>(stringMessageResponseListeners);
        for (SystemMessageResponseListenerEntry e : listeners) {
            if (e.getMessageId().equals(response.getMessageId())) {
                stringMessageResponseListeners.remove(e);
                e.getListener().responseReceived(response.getMessageBody());
            }
        }
    }

    private void notifyAndRemoveByteDataMessageResponseListeners(ByteDataMessage response) {
        List<ByteDataMessageResponseListenerEntry> listeners = new ArrayList<>(byteDataMessageResponseListeners);
        for (ByteDataMessageResponseListenerEntry e : listeners) {
            if (e.getMessageId().equals(response.getMessageId())) {
                byteDataMessageResponseListeners.remove(e);
                e.getListener().responseReceived(
                        response.getMessageBody().getResponseCode(),
                        response.getMessageBody().getMediaType(),
                        Base64.getDecoder().decode(response.getMessageBody().getBase64EncodedData()));
            }
        }
    }

    public void sendSystemMessage(String messageId, String systemId, String messageBody) {
        logger.info("sendSystemMessage, systemId: {}, message: {}", systemId, messageBody);
        template.convertAndSendToUser(systemId,
                "/system-message-request",
                new SystemMessage(messageId, messageBody));
    }

    public void addSystemMessageResponseListener(String messageId, SystemMessageResponseListener listener) {
        stringMessageResponseListeners.add(new SystemMessageResponseListenerEntry(messageId, listener));
    }

    public void addByteDataMessageResponseListener(String messageId, ByteDataMessageResponseListener listener) {
        byteDataMessageResponseListeners.add(new ByteDataMessageResponseListenerEntry(messageId, listener));
    }

    @Data
    @AllArgsConstructor
    private static class SystemMessageResponseListenerEntry {
        String messageId;
        SystemMessageResponseListener listener;
    }

    @Data
    @AllArgsConstructor
    private static class ByteDataMessageResponseListenerEntry {
        String messageId;
        ByteDataMessageResponseListener listener;
    }
}
