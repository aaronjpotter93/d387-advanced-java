package edu.wgu.d387_sample_code.service;

import edu.wgu.d387_sample_code.model.response.WelcomeResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

@Service
public class ThreadService {
    private final ExecutorService messageExecutor = Executors.newFixedThreadPool(2);

    public List<WelcomeResponse> spinUpThreads() throws InterruptedException {

        List<Callable<WelcomeResponse>> tasks = new ArrayList<>();
        tasks.add(this::loadFrenchMessage);
        tasks.add(this::loadEnglishMessage);

        List<Future<WelcomeResponse>> futures = messageExecutor.invokeAll(tasks);

        List<WelcomeResponse> responses = new ArrayList<>();
        for (Future<WelcomeResponse> future : futures) {
            try {
                responses.add(future.get());
            } catch (ExecutionException e) {
                responses.add(new WelcomeResponse("error", 0, "Failed to load message"));
            }
        }
        return responses;
    }

    private WelcomeResponse loadFrenchMessage() {
        return loadMessage("welcome_fr.properties", "fr");
    }

    private WelcomeResponse loadEnglishMessage() {
        return loadMessage("welcome_en.properties", "en");
    }

    private WelcomeResponse loadMessage(String filename, String languageCode) {
        long startTime = System.nanoTime();
        try (InputStream stream = new ClassPathResource(filename).getInputStream()) {
            Properties properties = new Properties();
            properties.load(stream);
            String message = properties.getProperty("welcome");
            double duration = (System.nanoTime() - startTime) / 1_000_000.0;
            return new WelcomeResponse(languageCode, duration, message);
        } catch (Exception e) {
            double duration = (System.nanoTime() - startTime) / 1_000_000.0;
            return new WelcomeResponse(languageCode, duration, "Error: " + e.getMessage());
        }
    }
}