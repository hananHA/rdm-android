package com.gp.salik.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gp.salik.R;

public class EmpPhotoUploadingFragment extends Fragment {


    View v;
    private ImageView up_photo0, up_photo1, up_photo2, up_photo3;
    Button close_ticket;

    public EmpPhotoUploadingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.photo_uploading_emp_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        up_photo0 = v.findViewById(R.id.up_photo0);
        up_photo1 = v.findViewById(R.id.up_photo1);
        up_photo2 = v.findViewById(R.id.up_photo2);
        up_photo3 = v.findViewById(R.id.up_photo3);

        up_photo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        up_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        up_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        up_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        close_ticket = v.findViewById(R.id.closeTicket);
        close_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: upload pictures
            }
        });

        return v;
    }
}
