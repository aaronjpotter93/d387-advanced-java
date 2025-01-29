package edu.wgu.d387_sample_code.model.response;

public class Welcome implements Comparable<Welcome> {

    private String languageCode;
    private double duration;
    private String message;

    public Welcome(String languageCode, double duration, String message) {
        this.languageCode = languageCode;
        this.duration = duration;
        this.message = message;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public double getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(Welcome other) {
        return Double.compare(this.duration, other.duration);
    }

    @Override
    public String toString() {
        return String.format("[%s] Thread [%.3f ms] : %s", languageCode.toUpperCase(), duration, message);
    }

}
