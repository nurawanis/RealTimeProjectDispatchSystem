package RTP_Project.example.Client;

import java.io.Serializable;

public class Incident implements Serializable {
    private final int id;
    private final String description;
    private String status = "OPEN";

    public Incident(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}