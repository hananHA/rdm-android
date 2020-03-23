package com.gp.salik.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmpTicketDetailsFragment extends Fragment {


    View v;
    public static List<JSONObject> ticketHistories = new ArrayList<>();
    public static List<JSONObject> userRating = new ArrayList<>();
    private static List<ImageView> td_photoList = new ArrayList<>();
    public int ticket_id = Integer.parseInt(App.TICKET);
    public String comment = null;
    public int rating = 0;
    private String status;
    private JSONObject ticket = App.ticketListMap.get(ticket_id);

    private ImageView emp_tphoto0, emp_tphoto1, emp_tphoto2, emp_tphoto3;
    private EditText emp_tdesc, emp_tloc;
    Button upload_photo, nothingToDo;

    public EmpTicketDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.ticket_details_emp_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        emp_tphoto0 = v.findViewById(R.id.emp_tphoto0);
        emp_tphoto1 = v.findViewById(R.id.emp_tphoto1);
        emp_tphoto2 = v.findViewById(R.id.emp_tphoto2);
        emp_tphoto3 = v.findViewById(R.id.emp_tphoto3);


        emp_tdesc = v.findViewById(R.id.emp_tdesc);
        emp_tloc = v.findViewById(R.id.emp_tloc);


        loadImage();
        emp_tdesc.setText(getDesc());
        emp_tloc.setText(getNeighborhood());


        emp_tloc.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String url;

                try {
                    String latitude, longitude;

                    latitude = ticket.getJSONArray("location").getJSONObject(0).getString("latitude");
                    longitude = ticket.getJSONArray("location").getJSONObject(0).getString("longitude");
                    url = "http://www.google.com/maps/place/" + latitude + "," + longitude;
                    Log.d("url map", url);


                } catch (Exception e) {
                    Log.d("url map", e.getMessage());

                    url = null;
                }
                if (!url.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);

                    browserIntent.setData(Uri.parse(url));
                    startActivity(browserIntent);
                }


            }
        });


        upload_photo = v.findViewById(R.id.uploadFix);
        nothingToDo = v.findViewById(R.id.nothingToDo);

        try {

            status = ticket.getJSONObject("ticket").getString("status");
            Log.d("status: ", status);
            if (!status.equalsIgnoreCase("IN_PROGRESS")) {
                upload_photo.setVisibility(View.GONE);


            } else {
                nothingToDo.setVisibility(View.GONE);

            }

        } catch (Exception e) {
            Log.d("er", e.getMessage());
        }

        nothingToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EmpTicketListFragment());
                trans.commit();
            }
        });


        upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EmpPhotoUploadingFragment(ticket_id));
                trans.commit();
            }
        });


        //TODO: get ticket details

        return v;
    }


    private void loadImage() {
        td_photoList.add(0, (ImageView) v.findViewById(R.id.emp_tphoto0));
        td_photoList.add(1, (ImageView) v.findViewById(R.id.emp_tphoto1));
        td_photoList.add(2, (ImageView) v.findViewById(R.id.emp_tphoto2));
        td_photoList.add(3, (ImageView) v.findViewById(R.id.emp_tphoto3));
        for (int i = 0; i < td_photoList.size(); i++) {
            td_photoList.get(i).setVisibility(View.GONE);
        }
        try {
            JSONArray photos = ticket.getJSONArray("photos");
            for (int i = 0; i < photos.length(); i++) {

                String URL = "http://www.ai-rdm.website/public/storage/photos/";
                String role_id = ((JSONObject) photos.get(i)).get("role_id").toString();
                Log.e("role id", role_id);
                if (!role_id.equalsIgnoreCase("1")) {
                    // this not photo from user (from company or employee)

                } else {
                    td_photoList.get(i).setVisibility(View.VISIBLE);
                    String image_name = ((JSONObject) photos.get(i)).get("photo_name").toString();
                    String full_path = URL + image_name;

                    Picasso.get().load(full_path).into(td_photoList.get(i));


                }
            }
            td_photoList.clear();


        } catch (Exception e) {
            Log.e("load image", e.getMessage());

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
}
