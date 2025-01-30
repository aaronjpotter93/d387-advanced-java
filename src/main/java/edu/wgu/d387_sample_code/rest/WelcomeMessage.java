package edu.wgu.d387_sample_code.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wgu.d387_sample_code.model.response.WelcomeResponse;
import edu.wgu.d387_sample_code.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ResourceConstants.WELCOME_MESSAGE_V1)
@CrossOrigin
public class WelcomeMessage {

    @Autowired
    ThreadService threadService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(
            path = "/threads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getWelcomeMessages() {
        try {
            List<WelcomeResponse> welcomeResponses = threadService.spinUpThreads();

            List<String> welcomeMessages = welcomeResponses.stream()
                    .map(WelcomeResponse::toString)
                    .collect(Collectors.toList());

            String jsonResponse = objectMapper.writeValueAsString(welcomeMessages);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);

        } catch (Exception e) {
            String errorResponse = "Error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorResponse);
        }
    }
}
