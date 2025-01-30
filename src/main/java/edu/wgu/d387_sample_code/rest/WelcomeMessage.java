package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.model.response.WelcomeResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@RestController
@RequestMapping(ResourceConstants.WELCOME_MESSAGE_V1)
@CrossOrigin
public class WelcomeMessage {

    static ExecutorService messageExecutor = newFixedThreadPool(2);

    @RequestMapping(
            path = "/threads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<WelcomeResponse>> getWelcomeMessages() {
        List<WelcomeResponse> welcomeResponses = new ArrayList<>();
        messageExecutor.submit(() -> {
            Properties properties = new Properties();
            try {
                InputStream stream = new ClassPathResource("welcome_en.properties").getInputStream();
                properties.load(stream);
                WelcomeResponse welcomeMessage = new WelcomeResponse(1, "en", (properties.getProperty("welcome")));
                welcomeResponses.add(welcomeMessage);
            } catch (Exception e) {
                WelcomeResponse welcomeMessage = new WelcomeResponse(1, "en", (properties.getProperty("error")));
                welcomeResponses.add(welcomeMessage);
                e.printStackTrace();
            }
        });
        messageExecutor.submit(() -> {
            Properties properties = new Properties();
            try {
                InputStream stream = new ClassPathResource("welcome_fr.properties").getInputStream();
                properties.load(stream);
                WelcomeResponse welcomeMessage = new WelcomeResponse(2, "fr", (properties.getProperty("welcome")));
                welcomeResponses.add(welcomeMessage);
            } catch (Exception e) {
                WelcomeResponse welcomeMessage = new WelcomeResponse(2, "fr", (properties.getProperty("error")));
                welcomeResponses.add(welcomeMessage);
                e.printStackTrace();
            }
        });

        // FIXME * on some runs this only returns one welcome message object.
        // FIXME * on most runs, thread 1 gets displayed before thread 2. design flaw somewhere?
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(welcomeResponses);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(welcomeResponses);
        }
    }
}
