package com.gp.salik.Model;

public class TicketList {
    private int id;
    private String description, status, classification;

    public TicketList(int id, String description, String status, String classification) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.classification = classification;
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

    public String getClassification() {
        return classification;
    }
}
