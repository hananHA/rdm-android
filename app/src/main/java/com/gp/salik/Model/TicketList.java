package com.gp.salik.Model;

public class TicketList {
    private int id;
    private String description, status, classification , status_ar , created_at;

    public TicketList(int id, String description, String status, String classification , String status_ar , String created_at) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.classification = classification;
        this.status_ar = status_ar;
        this.created_at = created_at;
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

    public String getStatus_ar() {
        return status_ar;
    }

    public String getCreated_at() {
        return created_at;
    }
}
