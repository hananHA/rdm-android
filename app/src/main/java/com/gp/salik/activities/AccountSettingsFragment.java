package com.gp.salik.activities;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;
import com.gp.salik.api.UserClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        if (App.USER_NAME == null || App.USER_NAME.equalsIgnoreCase("null")) {
            user_name.setHint("قم بتحديث اسمك هنا");
        } else {
            user_name.setText(App.USER_NAME);

        }

        if (App.USER_PHONE == null || App.USER_PHONE.equalsIgnoreCase("null")) {
            user_phone.setHint("05XXXXXXXX");
        } else {
            user_phone.setText(App.USER_PHONE);

        }
        user_email.setText(App.USER_EMAIL);

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
                user_phone.setEnabled(true);
//                user_email.setEnabled(true);
//                user_phone.setEnabled(true);
//                s_neighborhood.setEnabled(true);
//                s_city.setEnabled(true);
//                radio_male.setEnabled(true);
//                radio_female.setEnabled(true);

                user_name.setTextColor(getResources().getColor(R.color.gray));
                user_email.setTextColor(getResources().getColor(R.color.gray));
                user_phone.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save changes to database
                if (user_name.getText().toString().trim().isEmpty() || user_phone.getText().toString().trim().isEmpty()) {
                    user_name.setError("لا يمكن ترك الاسم أو رقم الجوال فارغ");
                    user_name.requestFocus();
                } else if (user_name.getText().toString().trim().equalsIgnoreCase(App.USER_NAME)
                        && user_phone.getText().toString().trim().equalsIgnoreCase(App.USER_PHONE)) {
                    user_name.setError("الرجاء كتابة اسم أو رقم جوال مختلف للحفظ");
                    user_name.requestFocus();

                } else if (!isSaudiPhone(user_phone.getText().toString().trim())) {
                    user_phone.setError("الرجاء التأكد من صيغة رقم الجوال");
                    user_phone.requestFocus();
                } else {
                    App.confirmMessage = "تأكيد تعديل الملف الشخصي";
                    Intent intent = new Intent(getActivity(), ConfirmGreen.class);
                    startActivity(intent);
                    updateProfile();

                }

            }
        });

        return view;
    }

    private void updateProfile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserClient.BASE_URL_USER)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        UserClient api = retrofit.create(UserClient.class);
        Call<ResponseBody> call = api.updateProfile(user_name.getText().toString().trim(), user_phone.getText().toString().trim(), "Bearer " + App.token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());

                        App.USER_NAME = res.getJSONObject("user_info").getString("name");
                        App.USER_EMAIL = res.getJSONObject("user_info").getString("email");
                        App.USER_PHONE = res.getJSONObject("user_info").getString("phone");

                        SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                        editUserInfo.putString("name", App.USER_NAME);
                        editUserInfo.putString("email", App.USER_EMAIL);
                        editUserInfo.putString("phone", App.USER_PHONE);
                        editUserInfo.apply();
                        Intent intent = new Intent(getActivity(), MainNavActivity.class);
                        Toast.makeText(getActivity().getApplicationContext(), "تم تحديث الملف الشخصي بنجاح ", Toast.LENGTH_LONG).show();

                        startActivity(intent);
                        //TODO: when  successfully update profile
                    } else {
                        if (response.code() == 422 || response.code() == 401 || response.code() == 500 || response.code() == 400) {
                            Toast.makeText(getActivity().getApplicationContext(), "الرجاء التحقق من الحساب ", Toast.LENGTH_LONG).show();


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

    public boolean isSaudiPhone(String text) {

        return text.matches("^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$");
    }
}
