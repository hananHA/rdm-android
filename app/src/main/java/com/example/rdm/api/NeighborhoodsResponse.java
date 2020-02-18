package com.example.rdm.api;

import com.google.gson.annotations.SerializedName;

public class NeighborhoodsResponse {

    @SerializedName("neighborhoods")
    private Neighborhoods neighborhoods;

    public Neighborhoods getNeighborhoods() {
        return neighborhoods;
    }

    public void setNeighborhoods(Neighborhoods neighborhoods) {
        this.neighborhoods = neighborhoods;
    }
}
