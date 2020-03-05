package com.gp.salik.Model;

import com.google.gson.annotations.SerializedName;

public class Neighborhood {

    @SerializedName("id")
    private String id;

    @SerializedName("name_ar")
    private String name_ar;

    @SerializedName("name_en")
    private String name_en;

    public Neighborhood(String name_ar, String id) {
        this.id = id;
        this.name_ar = name_ar;

    }


    @SerializedName("city_id")
    private String city_id;

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

    //to display object as a string in spinner
    @Override
    public String toString() {
        return name_ar;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Neighborhood) {
            Neighborhood c = (Neighborhood) obj;
            if (c.getName_ar().equals(name_ar) && c.getId() == id) return true;
        }

        return false;
    }


}


