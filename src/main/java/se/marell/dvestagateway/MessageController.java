/*
 * Created by Daniel Marell 14-03-02 12:02
 */
package se.marell.dvestagateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.marell.dvestagateway.apimodel.SystemMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebSocketController wsController;

    private String receivedMessageBody;

    @RequestMapping(value = "/system/{systemId}/message", method = RequestMethod.POST)
    public ResponseEntity<SystemMessage> postSystemMessage(
            @PathVariable("systemId") String systemId,
            @RequestParam(value = "timeout", defaultValue = "5000") int timeout,
            @RequestBody String messageBody) {
        logger.info("postSystemMessage, systemId: {}, messageBody: {}", systemId, messageBody);
        String messageId = wsController.sendSystemMessage(systemId, messageBody);

        final CountDownLatch latch = new CountDownLatch(1);
        wsController.addSystemMessageResponseListener(messageId, listenerMessageBody -> {
            latch.countDown();
            receivedMessageBody = listenerMessageBody;
        });
        try {
            // Wait for response
            if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        return new ResponseEntity<>(new SystemMessage(messageId, receivedMessageBody), HttpStatus.OK);
    }
}
