package com.example.rdm.api;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TicketClient {

    String BASE_URL = " http://www.ai-rdm.website/api/ticket/";

    //
//    @FormUrlEncoded
//    @Headers({
//            "Accept: application/json",
//    })
//    @Multipart
//    @POST("create")
//    Call<ResponseBody> createTicket(@Field("description") String description
//    );
////    @Header("Authorization") String authHeader
//
//
//    @POST("create")
//    @Multipart
//    @Headers({
//            "Accept: application/json",
//    })
//    Call<ResponseBody> addTicket(@Part("latitude") double latitude,
//                                 @Part("longitude") double longitude,
//                                 @Part("city") int city,
//                                 @Part("neighborhood") int neighborhood,
//                                 @Part MultipartBody.Part[] filePart,
//                                 @Header("Authorization") String auth,
//                                 @Part("description") String description
//
//
//    );

    @POST("create")
    @Headers({
            "Accept: application/json",

    })
    Call<ResponseBody> addTicket(
            @Body RequestBody body,
            @Header("Authorization") String auth
    );


}


