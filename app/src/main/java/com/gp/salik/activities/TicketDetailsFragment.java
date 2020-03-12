package com.gp.salik.activities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketDetailsFragment extends Fragment {

    private View v;
    public static List<JSONObject> ticketHistories = new ArrayList<>();
    public static List<JSONObject> userRating = new ArrayList<>();
    public static List<ImageView> td_photoList = new ArrayList<>();
    private boolean isImageFitToScreen;


    public int ticket_id = Integer.parseInt(App.TICKET);
    public String comment = null;
    public int rating = 0;
    private JSONObject ticket = App.ticketListMap.get(ticket_id);
    private ImageView td_photo0, td_photo1, td_photo2, td_photo3;
    private EditText td_neighborhood, td_desc;
    private Button delete_ticket, eval_ticket, showEval, other_status;


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


        td_desc = v.findViewById(R.id.td_description);
        td_neighborhood = v.findViewById(R.id.td_neighborhood);
        delete_ticket = v.findViewById(R.id.deleteTicket);
        eval_ticket = v.findViewById(R.id.evalTicket);
        showEval = v.findViewById(R.id.showEval);
        other_status = v.findViewById(R.id.workInTicket);

        delete_ticket.setVisibility(View.INVISIBLE);
        eval_ticket.setVisibility(View.INVISIBLE);
        showEval.setVisibility(View.INVISIBLE);
        other_status.setVisibility(View.INVISIBLE);


        td_desc.setEnabled(false);
        td_neighborhood.setEnabled(false);


        loadImage();
        td_desc.setText(getDesc());
        td_neighborhood.setText(getNeighborhood());
        getStatusRateDelete();


        delete_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: delete ticket
                Toast.makeText(getActivity().getApplicationContext(), " كود حذف التذكرة هنا ", Toast.LENGTH_LONG).show();

//                FragmentTransaction trans = getFragmentManager()
//                        .beginTransaction();
//                trans.replace(R.id.root_frame, new EvaluateTicketFragment());
//                trans.commit();
            }
        });

        eval_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EvaluateTicketFragment(ticket_id, false));
                trans.commit();
            }
        });

        showEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EvaluateTicketFragment(comment, ticket_id, rating, true));
                trans.commit();
            }
        });

        return v;
    }

    private void loadImage() {
        td_photoList.add(0, (ImageView) v.findViewById(R.id.td_photo0));
        td_photoList.add(1, (ImageView) v.findViewById(R.id.td_photo1));
        td_photoList.add(2, (ImageView) v.findViewById(R.id.td_photo2));
        td_photoList.add(3, (ImageView) v.findViewById(R.id.td_photo3));
        for (int i = 0; i < td_photoList.size(); i++) {
            td_photoList.get(i).setVisibility(View.INVISIBLE);
        }
        try {

            JSONArray photos = ticket.getJSONArray("photos");
            for (int i = 0; i < photos.length(); i++) {
                td_photoList.get(i).setVisibility(View.VISIBLE);
                String URL = "http://www.ai-rdm.website/public/storage/photos/";
                String role_id = ((JSONObject) photos.get(i)).get("role_id").toString();
                Log.e("role id", role_id);
                String image_name = ((JSONObject) photos.get(i)).get("photo_name").toString();
                String full_path = URL + image_name;
                Picasso.get().load(full_path).into(td_photoList.get(i));
            }
            td_photoList.clear();


        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private String getDesc() {


        String desc = null;
        try {
            desc = ticket.getJSONObject("ticket").getString("description");

        } catch (Exception e) {
            desc = null;
        }
        if (!desc.equalsIgnoreCase("null")) {
            return desc;
        } else {
            return "لا يوجد وصف";
        }
    }

    private String getNeighborhood() {


        String neighborhood = null;
        try {
            neighborhood = ticket.getJSONArray("location").getJSONObject(0).getString("neighborhood");

        } catch (Exception e) {
            neighborhood = null;
        }
        if (!neighborhood.equalsIgnoreCase("null")) {
            return neighborhood;
        } else {
            return "لا يوجد حي";
        }
    }

    private void getStatusRateDelete() {

        String status = null;

        try {
            status = ticket.getJSONObject("ticket").getString("status");
            JSONArray rate = ticket.getJSONArray("userRating");

            if (status.equalsIgnoreCase("OPEN")) {
                delete_ticket.setVisibility(View.VISIBLE);
            } else if (rate.isNull(0) && status.equalsIgnoreCase("CLOSED")) {
                eval_ticket.setVisibility(View.VISIBLE);
            } else if (!rate.isNull(0)) {
                comment = rate.getJSONObject(0).getString("comment");
                rating = Integer.parseInt(rate.getJSONObject(0).getString("rating"));
                showEval.setVisibility(View.VISIBLE);

            } else {
                other_status.setVisibility(View.VISIBLE);

            }


        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void deleteTicket() {

    }


}
