package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.model.response.PresentationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.time.format.FormatStyle;
import java.util.Locale;

@RestController
@RequestMapping(ResourceConstants.PRESENTATION_MESSAGE_V1 )
@CrossOrigin
public class PresentationMessage {

    @RequestMapping(
            path = "/times",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<PresentationResponse> getTimeZones() {
        List<ZonedDateTime> zonedTimes = new ArrayList<>();

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate day = LocalDate.of(2025, 9, 22);
        LocalTime time = LocalTime.of(11, 30);
        LocalDateTime dayTime = LocalDateTime.of(day, time);
        ZonedDateTime presentation = ZonedDateTime.of(dayTime, zoneId);

        ZonedDateTime etTime = presentation.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime mtTime = presentation.withZoneSameInstant(ZoneId.of("America/Denver"));
        ZonedDateTime utcTime = presentation.withZoneSameInstant(ZoneId.of("UTC"));

        zonedTimes.add(etTime);
        zonedTimes.add(mtTime);
        zonedTimes.add(utcTime);

        // Format the date of the presentation
        DateTimeFormatter dayFormatter = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.MEDIUM);
        String formattedDay = dayFormatter.format(day);

        // Format the time of the presentation
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
                .appendLiteral(" ")
                .appendZoneText(TextStyle.FULL).toFormatter()
                .withLocale(Locale.US);
        List<String> formattedTimeZones = new ArrayList<>();
        for (ZonedDateTime zoneDateTime : zonedTimes) {
            formattedTimeZones.add(zoneDateTime.format(timeFormatter));
        }

        // Instantiate json-ready object
        PresentationResponse livePresentation = new PresentationResponse(formattedDay, formattedTimeZones);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(livePresentation);
    }
}

