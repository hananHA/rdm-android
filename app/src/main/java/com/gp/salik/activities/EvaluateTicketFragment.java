package com.gp.salik.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonArray;
import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;
import com.squareup.picasso.Picasso;

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

public class EvaluateTicketFragment extends Fragment {

    private View v;
    private String comment;
    private int ticket_id, rating;
    private boolean isShow;
    private EditText td_comment;
    private RatingBar td_rating_bar;
    private Button evalTicket, backTicket;
    private ImageView td_photo0, td_photo1, td_photo2, td_photo3;
    private static List<ImageView> td_photoList = new ArrayList<>();
    private JSONObject ticket;


    public EvaluateTicketFragment() {
    }

    public EvaluateTicketFragment(int ticket_id, boolean isShow) {
        this.ticket_id = ticket_id;
        this.isShow = isShow;
    }

    public EvaluateTicketFragment(String comment, int ticket_id, int rating, boolean isShow) {
        this.comment = comment;
        this.ticket_id = ticket_id;
        this.rating = rating;
        this.isShow = isShow;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.evalute_ticket_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        td_comment = v.findViewById(R.id.td_comment);
        td_rating_bar = v.findViewById(R.id.rating);
        evalTicket = v.findViewById(R.id.evlTicket);
        backTicket = v.findViewById(R.id.backTicket);
        if (isShow) {
            ticket = App.ticketListMap.get(ticket_id);

            evalTicket.setVisibility(View.INVISIBLE);
            td_comment.setText(comment);
            td_comment.setEnabled(false);
            td_rating_bar.setRating(rating);
            td_rating_bar.setEnabled(false);
            loadImage();

        } else {
            backTicket.setVisibility(View.INVISIBLE);

        }
        backTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new TicketDetailsFragment());
                trans.commit();
            }
        });

        evalTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = td_comment.getText().toString();
                rating = (int) td_rating_bar.getRating();
                Log.e("num of star is: ", String.valueOf(rating));
                if (comment.trim().isEmpty() && rating == 0) {
                    td_comment.setError("الرجاء كتابة ملاحظة للتقييم");
                    td_comment.requestFocus();
                    Toast.makeText(getActivity().getApplicationContext(), "الرجاء اختيار نجمة على الأقل للتقييم ", Toast.LENGTH_LONG).show();
                } else if (comment.trim().isEmpty()) {
                    td_comment.setError("الرجاء كتابة ملاحظة للتقييم");
                    td_comment.requestFocus();

                } else if (rating == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "الرجاء اختيار نجمة على الأقل للتقييم ", Toast.LENGTH_LONG).show();

                } else {
                    App.confirmMessage = "تأكيد اعتماد التقييم";
                    Intent intent = new Intent(getActivity(), ConfirmGreen.class);
                    startActivity(intent);
                    rateTicket();

                }
            }
        });


        return v;
    }

    public void rateTicket() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TicketClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        TicketClient api = retrofit.create(TicketClient.class);
        Call<ResponseBody> call = api.rateTicket(ticket_id, comment, rating, "Bearer " + App.token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        //TODO: when add rate successfully refresh all ticket list
                        listTicket();


                    } else {
                        if (response.code() == 422 || response.code() == 401 || response.code() == 500 || response.code() == 400) {
                            Log.e("error rating ticket ", "error code is: " + response.code() + "ticket_id is: " + ticket_id);
                            Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من الحساب ", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getActivity(), MainNavActivity.class);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    Log.e("error when rate ticket", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من الاتصال بالإنترنت ", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void listTicket() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TicketClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        TicketClient api = retrofit.create(TicketClient.class);
        Call<JsonArray> call = api.listTicket("Bearer " + App.token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    if (response.isSuccessful()) {
                        App.listTicketResponse = response.body().toString();
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        App.TICKET_NUM = jsonArray.length();

                        Intent intent = new Intent(getActivity(), MainNavActivity.class);
                        startActivity(intent);
//                        FragmentTransaction trans = getFragmentManager()
//                                .beginTransaction();
//                        trans.replace(R.id.root_frame, new TicketsListFragment());
//                        trans.commit();


                        Toast.makeText(getActivity().getApplicationContext(), "تم إضافة التقييم بنجاح ", Toast.LENGTH_LONG).show();


                    } else {
                        if (response.code() == 401 || response.code() == 500 || response.code() == 400) {

                            Log.e("error list ticket ", "error code is: " + response.code() + "ticket_id is: " + ticket_id);
                            Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من الحساب ", Toast.LENGTH_LONG).show();


                        }
                        if (response.code() == 422) {
                            Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من صيغة رقم الجوال ", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (Exception e) {
                    Log.e("error when list ticket", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من الاتصال بالإنترنت ", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadImage() {
        td_photoList.add(0, (ImageView) v.findViewById(R.id.fix_photo0));
        td_photoList.add(1, (ImageView) v.findViewById(R.id.fix_photo1));
        td_photoList.add(2, (ImageView) v.findViewById(R.id.fix_photo2));
        td_photoList.add(3, (ImageView) v.findViewById(R.id.fix_photo3));
        for (int i = 0; i < td_photoList.size(); i++) {
            td_photoList.get(i).setVisibility(View.GONE);
        }
        try {


            JSONArray photos = ticket.getJSONArray("photos");
            int x = 0;
            for (int i = 0; i < photos.length(); i++) {
                String URL = "http://www.ai-rdm.website/public/storage/photos/";
                String role_id = ((JSONObject) photos.get(i)).get("role_id").toString();
                Log.e("role id", role_id);
                boolean plus = false;
                if (!role_id.equalsIgnoreCase("1")) {

                    td_photoList.get(x).setVisibility(View.VISIBLE);
                    String image_name = ((JSONObject) photos.get(i)).get("photo_name").toString();
                    String full_path = URL + image_name;
                    Log.e("path", full_path);

                    Picasso.get().load(full_path).into(td_photoList.get(x));
                    plus = true;


                } else {
                    plus = false;

                    // this not photo from emp or comp or admin (from user so not fix photo)
                }
                if (plus) {
                    x++;

                }
            }
            td_photoList.clear();

        } catch (Exception e) {
            Log.e("load image", e.getMessage());

        }

    }
}
