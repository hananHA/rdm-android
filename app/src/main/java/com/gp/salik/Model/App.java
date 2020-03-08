package com.gp.salik.Model;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class App {
    public static String token = null;
    public static String test = null;
    public static SharedPreferences sharedPreferences;
    public static String resNe = null;
    public static List<Neighborhood> neighborhoodListApp = new ArrayList<>();
    public static String listTicketResponse = null;
    public static String TICKET = null;



    public static OkHttpClient.Builder okHttpClientCall() {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout


        httpClient.addInterceptor(logging);

        return httpClient;


    }


}
