package com.example.rdm.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Neighborhoods {


    @SerializedName("id")
    private String id;

    @SerializedName("name_ar")
    private String name_ar;

    @SerializedName("name_en")
    private String name_en;


    @SerializedName("city_id")
    private String city_id;

    @SerializedName("neighborhoods")
    private List<Neighborhoods> neighborhoods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }


}

