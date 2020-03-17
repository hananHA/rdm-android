package com.gp.salik.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gp.salik.R;

public class EmpTicketDetailsFragment extends Fragment {


    View v;
    private ImageView emp_tphoto0, emp_tphoto1, emp_tphoto2, emp_tphoto3;
    private EditText emp_tdesc, emp_tloc;
    Button upload_photo;

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

        upload_photo = v.findViewById(R.id.uploadFix);
        upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EmpPhotoUploadingFragment());
                trans.commit();
            }
        });


        //TODO: get ticket details

        return v;
    }
}
