package se.marell.dvestagateway.websockets;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import se.marell.dvestagateway.apimodel.SystemConnectMessage;

import java.lang.reflect.Type;

public class ConnectSystemMessageHandler extends StompSessionHandlerAdapter {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return SystemConnectMessage.class;
    }
};
