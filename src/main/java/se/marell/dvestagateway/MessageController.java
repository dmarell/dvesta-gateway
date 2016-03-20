/*
 * Created by Daniel Marell 14-03-02 12:02
 */
package se.marell.dvestagateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.marell.dvestagateway.apimodel.*;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebSocketController wsController;

    private String receivedStringMessageBody;
    private ByteDataMessageBody receivedByteData;

    @RequestMapping(value = "/system/{systemId}/message", method = RequestMethod.POST)
    public ResponseEntity<SystemMessage> postSystemMessage(
            @PathVariable("systemId") String systemId,
            @RequestParam(value = "timeout", defaultValue = "5000") int timeout,
            @ModelAttribute MessageBody messageBody
    ) {
        String messageId = UUID.randomUUID().toString();
        logger.info("postSystemMessage, messageId: {}, systemId: {}, messageBody: {}", systemId, messageBody);

        final CountDownLatch latch = new CountDownLatch(1);
        wsController.addSystemMessageResponseListener(messageId, listenerMessageBody -> {
            receivedStringMessageBody = listenerMessageBody;
            latch.countDown();
        });
        wsController.sendSystemMessage(messageId, systemId, messageBody.getCommand());

        try {
            // Wait for response
            if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        return new ResponseEntity<>(new SystemMessage(messageId, receivedStringMessageBody), HttpStatus.OK);
    }

    @RequestMapping(value = "/system/{systemId}/message2", method = RequestMethod.POST)
    public ResponseEntity<SystemMessage> postSystemMessage2(
            @PathVariable("systemId") String systemId,
            @RequestParam(value = "timeout", defaultValue = "5000") int timeout,
            @RequestBody MessageBody messageBody
    ) {
        String messageId = UUID.randomUUID().toString();
        logger.info("postSystemMessage2, messageId: {}, systemId: {}, messageBody: {}", messageId, systemId, messageBody);
        final CountDownLatch latch = new CountDownLatch(1);
        wsController.addSystemMessageResponseListener(messageId, listenerMessageBody -> {
            receivedStringMessageBody = listenerMessageBody;
            latch.countDown();
        });
        wsController.sendSystemMessage(messageId, systemId, messageBody.getCommand());

        try {
            // Wait for response
            if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        return new ResponseEntity<>(new SystemMessage(messageId, receivedStringMessageBody), HttpStatus.OK);
    }

    @RequestMapping(value = "/system/{systemId}/camera/{cameraName}/image", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCameraImage(
            @PathVariable("systemId") String systemId,
            @PathVariable("cameraName") String cameraName,
            @RequestParam(value = "timeout", defaultValue = "5000") int timeout) {
        String messageId = UUID.randomUUID().toString();
        logger.info("getCameraImage, messageId: {}, systemId: {}, cameraName: {}", messageId, systemId, cameraName);
        final CountDownLatch latch = new CountDownLatch(1);
        wsController.addByteDataMessageResponseListener(messageId, (responseCode, mediaType, data) -> {
            receivedByteData = new ByteDataMessageBody(responseCode, mediaType, Base64.getEncoder().encodeToString(data));
            latch.countDown();
        });
        wsController.sendSystemMessage(messageId, systemId, "cam " + cameraName);

        try {
            // Wait for response
            if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }

        if (receivedByteData.getResponseCode() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType(receivedByteData.getMediaType()));
        return new ResponseEntity<>(
                Base64.getDecoder().decode(receivedByteData.getBase64EncodedData()),
                responseHeaders, HttpStatus.OK);
    }
}