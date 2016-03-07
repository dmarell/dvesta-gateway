/*
 * Created by Daniel Marell 14-06-03 16:51
 */
package se.marell.dvestagateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Integration test deploying the app on tomcat and calling the endpoint /doodles.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DoodleControllerIT {
    static final String REST_URL = "http://localhost:8080";

//    @Autowired
//    DoodleMessageRepository doodleMessageRepository;

    RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldStoreAndReadGreeting() throws Exception {
//        assertThat(getDoodles().length == 0, is(true));
//
//        ResponseEntity<Long> response = restTemplate.postForEntity(
//                REST_URL + "/doodles",
//                new DoodleMessage("Homer", "Message 1"), Long.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        long id = response.getBody();
//        assertThat(id > 0L, is(true));
//        DoodleMessage[] result = getDoodles();
//        assertThat(result.length, is(1));
//        DoodleMessage doodle = result[0];
//        assertThat(doodle.getAuthor(), is("Homer"));
//        assertThat(doodle.getMessage(), is("Message 1"));
//        doodle = findById(id);
//        assertThat(doodle.getMessage(), is("Message 1"));
    }

//    DoodleMessage findById(long id) {
//        ResponseEntity<DoodleMessage> response = restTemplate.getForEntity(
//                REST_URL + "/doodles/{id}", DoodleMessage.class, id);
//        return response.getBody();
//    }

//    DoodleMessage[] getDoodles() {
//        ResponseEntity<DoodleMessage[]> response = restTemplate.getForEntity(
//                REST_URL + "/doodles", DoodleMessage[].class);
//        return response.getBody();
//    }
}
