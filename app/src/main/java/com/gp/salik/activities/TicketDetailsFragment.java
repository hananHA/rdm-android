package com.gp.salik.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketDetailsFragment extends Fragment {

    private View v;
    String tID;
    public static List<JSONObject> photosList = new ArrayList<>();
    public static List<JSONObject> ticketHistories = new ArrayList<>();
    public static List<JSONObject> userRating = new ArrayList<>();


    public TicketDetailsFragment() {
    }

    /*public TicketDetailsFragment(String tID) {
        this.tID = tID;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_ticket, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        System.out.println(App.TICKET);
        int ticket_id = Integer.parseInt(App.TICKET);
        getTicket(ticket_id);

        return v;
    }

    public void getTicket(final int ticket_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TicketClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        TicketClient api = retrofit.create(TicketClient.class);
        Call<ResponseBody> call = api.getTicket(ticket_id, "Bearer " + App.token);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject obj = new JSONObject(res);
                        JSONObject ticket = (JSONObject) obj.get("ticket");
                        JSONObject location = (JSONObject) obj.get("ticket");

                        JSONArray photos = new JSONArray();
                        photos.put(obj.get("photos"));
//                        for (int i = 0; i < photos.length(); i++) {
//                            JSONObject photo = photos.getJSONObject(i);
//                            photosList.add(i, photo);
//                        }

                        JSONArray ticketHistories = new JSONArray();
                        photos.put(obj.get("ticketHistories"));

                        JSONArray userRating = new JSONArray();
                        photos.put(obj.get("userRating"));


                        Toast.makeText(getActivity().getApplicationContext(), "id: " + ticket_id, Toast.LENGTH_LONG).show();

                    } else {
                        if (response.code() == 422 || response.code() == 401 || response.code() == 500) {
                            Log.e("error list ticket ", "error code is: " + response.code());

                        }
                    }

                } catch (Exception e) {
                    Log.e("error when list ticket", e.getMessage());

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من اتصالك بالإنترنت والمحاولة لاحقا ", Toast.LENGTH_LONG).show();


            }
        });


//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    if (response.isSuccessful()) {
//                        App.listTicketResponse = response.body().toString();
//
//                        Intent intent = new Intent(MainActivity.this, TicketListActivity.class);
//                        startActivity(intent);
//
//
////                        Log.d("resList", res);
//
//                    } else {
//                        if (response.code() == 422 || response.code() == 401 || response.code() == 500) {
//                            Log.e("error list ticket ", "error code is: " + response.code());
//
//                        }
//                    }
//
//                } catch (Exception e) {
//                    Log.e("error when list ticket", e.getMessage());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//
//            }
//        });


    }
}
