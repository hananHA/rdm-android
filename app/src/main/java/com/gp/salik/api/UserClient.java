package com.gp.salik.api;

import com.gp.salik.Model.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserClient {

    String BASE_URL = " http://www.ai-rdm.website/api/auth/";
    String BASE_URL_USER = "http://www.ai-rdm.website/api/user/";


    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
    })
    @POST("login")
    Call<ResponseBody> postLogin(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
    })
    @POST("register")
    Call<ResponseBody> postRegister(@Field("email") String email, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

    @POST("update")
    @Headers({
            "Accept: application/json",

    })
    Call<ResponseBody> updateProfile(

            @Body RequestBody body,
            @Header("Authorization") String auth
    );


}


