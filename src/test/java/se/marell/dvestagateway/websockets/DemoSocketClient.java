/*
 * Created by Daniel Marell 2015-11-06 18:29
 */
package se.marell.dvestagateway.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import se.marell.dvestagateway.apimodel.SystemConnectMessage;

import java.util.Arrays;

/**
 * http://docs.spring.io/spring-framework/docs/4.2.2.RELEASE/spring-framework-reference/html/websocket.html#websocket-fallback-sockjs-client
 */
public class DemoSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(DemoSocketClient.class);

    public static void main(String[] args) throws Exception {
        final String systemId = "s1";

//        WebSocketClient transport = new StandardWebSocketClient();
        WebSocketClient transport = new SockJsClient(Arrays.asList((new WebSocketTransport(new StandardWebSocketClient()))));
        WebSocketStompClient client = new WebSocketStompClient(transport);
        client.setMessageConverter(new MappingJackson2MessageConverter());
//        WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
//        wsHeaders.
        StompSession s = client.connect("ws://localhost:8092/gateway-websock", new ConnectSystemMessageHandler()).get();

        s.subscribe("system-message-request." + systemId, new SystemCommandMessageFrameHandler(s));

        StompHeaders headers = new StompHeaders();
        headers.setDestination("/app/system-connect");

        s.send(headers, new SystemConnectMessage(systemId));
        System.in.read();
        s.disconnect();
    }
}
