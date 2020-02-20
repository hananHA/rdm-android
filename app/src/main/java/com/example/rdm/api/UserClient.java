package com.example.rdm.api;

import com.example.rdm.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserClient {

    String BASE_URL = " http://www.ai-rdm.website/api/auth/";

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
    })
    @POST("login")
    Call<User> postLogin(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
    })
    @POST("register")
    Call<User> postRegister(@Field("email") String email, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

}


