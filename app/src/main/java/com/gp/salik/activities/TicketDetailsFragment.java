package com.gp.salik.activities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gp.salik.Model.App;
import com.gp.salik.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketDetailsFragment extends Fragment {

    private View v;
    public static List<JSONObject> photosList = new ArrayList<>();
    public static List<JSONObject> ticketHistories = new ArrayList<>();
    public static List<JSONObject> userRating = new ArrayList<>();

    private ImageView td_photo0, td_photo1, td_photo2, td_photo3;
    private EditText td_desc;
    private Spinner neighborhood_spinner;
    private Button delete_ticket, eval_ticket;


    public TicketDetailsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.ticket_details_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        td_photo0 = v.findViewById(R.id.td_photo0);
        td_photo1 = v.findViewById(R.id.td_photo1);
        td_photo2 = v.findViewById(R.id.td_photo2);
        td_photo3 = v.findViewById(R.id.td_photo3);
        td_desc = v.findViewById(R.id.td_description);
        neighborhood_spinner = v.findViewById(R.id.td_spinner);
        delete_ticket = v.findViewById(R.id.deleteTicket);
        eval_ticket = v.findViewById(R.id.evalTicket);

        td_desc.setEnabled(false);
        neighborhood_spinner.setEnabled(false);

        if(App.opened == false) {
            delete_ticket.setVisibility(View.GONE);
        } else if(App.opened) {
            eval_ticket.setVisibility(View.GONE);
        }

        System.out.println(App.TICKET);
        int ticket_id = Integer.parseInt(App.TICKET);
        System.out.println(ticket_id);

        try {
            JSONObject ticket = App.ticketListMap.get(ticket_id);

            JSONArray photos = ticket.getJSONArray("photos");
            for (int i = 0 ; i < photos.length() ; i++) {
                JSONObject photo = (JSONObject) photos.get(0);
                
            }



            //Toast.makeText(getActivity().getApplicationContext(), "photo name: " + photo.getString("photo_name"), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }


//        getTicket(ticket_id);

        delete_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: delete ticket
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EvaluateTicketFragment());
                trans.commit();
            }
        });

        eval_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EvaluateTicketFragment());
                trans.commit();
            }
        });

        return v;
    }

//    public void getTicket(final int ticket_id) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(TicketClient.BASE_URL)
//                //Here we are using the GsonConverterFactory to directly convert json data to object
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(App.okHttpClientCall().build())
//                .build();
//
//        TicketClient api = retrofit.create(TicketClient.class);
//        Call<ResponseBody> call = api.getTicket(ticket_id, "Bearer " + App.token);
//
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if (response.isSuccessful()) {
//                        String res = response.body().string();
//                        JSONObject obj = new JSONObject(res);
//                        JSONObject ticket = (JSONObject) obj.get("ticket");
//                        JSONObject location = (JSONObject) obj.get("ticket");
//
//                        JSONArray photos = new JSONArray();
//                        photos.put(obj.get("photos"));
////                        for (int i = 0; i < photos.length(); i++) {
////                            JSONObject photo = photos.getJSONObject(i);
////                            photosList.add(i, photo);
////                        }
//
//                        JSONArray ticketHistories = new JSONArray();
//                        photos.put(obj.get("ticketHistories"));
//
//                        JSONArray userRating = new JSONArray();
//                        photos.put(obj.get("userRating"));
//
//
//                        Toast.makeText(getActivity().getApplicationContext(), "id: " + ticket_id, Toast.LENGTH_LONG).show();
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
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من اتصالك بالإنترنت والمحاولة لاحقا ", Toast.LENGTH_LONG).show();
//
//
//            }
//        });
//
//
//    }
}
