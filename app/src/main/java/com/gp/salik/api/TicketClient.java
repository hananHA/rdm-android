package com.gp.salik.api;

import com.google.gson.JsonArray;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TicketClient {

    String BASE_URL = " http://www.ai-rdm.website/api/ticket/";


    @POST("create")
    @Headers({
            "Accept: application/json",

    })
    Call<ResponseBody> addTicket(
            @Body RequestBody body,
            @Header("Authorization") String auth
    );


    @GET("neighborhoods")
    @Headers({
            "Accept: application/json",

    })
    Call<JsonArray> getNeighborhoods(
            @Header("Authorization") String auth
    );


    @GET("list")
    @Headers({
            "Accept: application/json",

    })
    Call<JsonArray> listTicket(
            @Header("Authorization") String auth
    );
    //JsonArray


    @POST("show")
    @FormUrlEncoded
    @Headers({
            "Accept: application/json",

    })
    Call<ResponseBody> getTicket(

            @Field("ticket_id") int ticket_id,
            @Header("Authorization") String auth
    );


}


