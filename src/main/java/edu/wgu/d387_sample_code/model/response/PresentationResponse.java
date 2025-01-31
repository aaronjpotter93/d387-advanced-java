package edu.wgu.d387_sample_code.model.response;

import java.util.List;

public class PresentationResponse {
    private String day;
    private List<String> zonedTimes;

    public PresentationResponse(String day, List<String> zonedTimes) {
        this.day = day;
        this.zonedTimes = zonedTimes;
    }

    public List<String> getZonedTimes() {
        return zonedTimes;
    }

    public void setZonedTimes(List<String> zonedTimes) {
        this.zonedTimes = zonedTimes;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

