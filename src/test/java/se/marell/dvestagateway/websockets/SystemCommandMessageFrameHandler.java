/*
 * Created by Daniel Marell 08/11/15.
 */
package se.marell.dvestagateway.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import se.marell.dvestagateway.apimodel.SystemMessage;

import java.lang.reflect.Type;

public class SystemCommandMessageFrameHandler implements StompFrameHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private StompSession session;

    public SystemCommandMessageFrameHandler(StompSession session) {
        this.session = session;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return SystemMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("handleFrame: payload: " + payload);
        sendResponse((SystemMessage) payload);
    }

    private void sendResponse(SystemMessage request) {
        StompHeaders headers = new StompHeaders();
        headers.setDestination("/app/system-message-response");
        session.send(headers, new SystemMessage(request.getMessageId(), "Yes! It worked."));
    }
}
