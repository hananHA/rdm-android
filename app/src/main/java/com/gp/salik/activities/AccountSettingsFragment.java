package com.gp.salik.activities;

import androidx.fragment.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.gp.salik.R;

public class AccountSettingsFragment extends Fragment {

    private static final String TAG = "AccountSettingsFragment";

    private View view;

    private EditText user_name, user_email, user_phone;
    private Button edit_profile, save_changes;
    private RadioButton radio_male, radio_female;
    private Spinner s_neighborhood, s_city;

    public AccountSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_settings_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        user_name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);
        user_phone = view.findViewById(R.id.user_phone);
        edit_profile = view.findViewById(R.id.editProfile);
        save_changes = view.findViewById(R.id.saveChanges);
        radio_male = view.findViewById(R.id.radio_male);
        radio_female = view.findViewById(R.id.radio_female);
        s_neighborhood = view.findViewById(R.id.spinner_neighborhood);
        s_city = view.findViewById(R.id.spinner_city);

        save_changes.setVisibility(View.GONE);

        //TODO: get data from the database
        //TODO: city and neighborhood spinners

        user_name.setEnabled(false);
        user_email.setEnabled(false);
        user_phone.setEnabled(false);
        s_neighborhood.setEnabled(false);
        s_city.setEnabled(false);
        radio_male.setEnabled(false);
        radio_female.setEnabled(false);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_changes.setVisibility(View.VISIBLE);
                edit_profile.setVisibility(View.GONE);
                user_name.setEnabled(true);
                user_email.setEnabled(true);
                user_phone.setEnabled(true);
                s_neighborhood.setEnabled(true);
                s_city.setEnabled(true);
                radio_male.setEnabled(true);
                radio_female.setEnabled(true);

                user_name.setTextColor(getResources().getColor(R.color.gray));
                user_email.setTextColor(getResources().getColor(R.color.gray));
                user_phone.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save changes to database
            }
        });

        return view;
    }
}
