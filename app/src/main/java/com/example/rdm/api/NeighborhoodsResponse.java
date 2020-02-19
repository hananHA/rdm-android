package com.example.rdm.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NeighborhoodsResponse {


    @SerializedName("neighborhoods")
    @Expose
    private RestResponse restResponse;

    public RestResponse getRestResponse() {
        return restResponse;
    }

    public void setRestResponse(RestResponse restResp) {
        restResponse = restResp;
    }


}
