package edu.wgu.d387_sample_code.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wgu.d387_sample_code.model.response.Welcome;
import edu.wgu.d387_sample_code.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(ResourceConstants.WELCOME_MESSAGE_V1)
@CrossOrigin
public class WelcomeMessage {

    @Autowired
    ThreadService threadService;

    @RequestMapping(
            path = "/threads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getWelcomeMessages() {
        List<String> languages = Arrays.asList("en", "es", "de", "ru", "fr", "zh", "ja", "ko", "ar");
        CompletionService<Welcome> completionService = threadService.spinUpThreads(languages);

        try {
            List<String> messages = IntStream.range(0, languages.size())
                    .mapToObj(i -> {
                        try {
                            return completionService.take().get();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .sorted()
                    .map(Welcome::toString)
                    .collect(Collectors.toList());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(messages);

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
