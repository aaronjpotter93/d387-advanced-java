package edu.wgu.d387_sample_code.model.response;

public class WelcomeResponse {

    private int id;
    private String language;
    private String message;

    public WelcomeResponse(int id, String language, String message) {
        this.id = id;
        this.language = language;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
