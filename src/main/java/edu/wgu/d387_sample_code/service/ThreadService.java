package edu.wgu.d387_sample_code.service;

import edu.wgu.d387_sample_code.model.response.Welcome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

@Service
public class ThreadService {

    // FIXME * english always gets printed last, while the first to print is usually between arabic, chinese, or japanese. something is super fishy about that. maybe this logic isn't truly simulating an even race.
    public CompletionService<Welcome> spinUpThreads(List<String> languages) {
        ExecutorService messageExecutor = Executors.newFixedThreadPool(languages.size());
        CompletionService<Welcome> completionService = new ExecutorCompletionService<>(messageExecutor);

        for (String lang : languages) {
            completionService.submit(() -> {
                long startTime = System.nanoTime();
                Properties properties = new Properties();
                try (InputStream stream = new ClassPathResource("welcome_" + lang + ".properties").getInputStream();
                     Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {

                    properties.load(reader);
                    String message = properties.getProperty("welcome");
                    long endTime = System.nanoTime();
                    double duration = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
                    return new Welcome(lang, duration, message);
                } catch (Exception e) {
                    double duration = (System.nanoTime() - startTime) / 1_000_000.0;
                    return new Welcome(lang, duration, "Error: " + e.getMessage());
                }
            });
        }
        return completionService;
    }
}