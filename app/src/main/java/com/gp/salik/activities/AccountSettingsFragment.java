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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.gp.salik.Model.App;
import com.gp.salik.Model.Neighborhood;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;
import com.gp.salik.api.UserClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
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
    private TextView text_city, textN1, textN2, neighborhoodText;

    private Button edit_profile, save_changes;
    private RadioButton radio_male, radio_female;
    public static HashMap<Integer, String> neighborhoodsListMap = new HashMap<Integer, String>();


    private Spinner s_neighborhood, s_city;
    String resNeighborhoods;
    String[] spinnerArray;
    int city = 6; // city_id for Makkah is 6 in db;
    HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();


    private static List<Neighborhood> neighborhoodList = new ArrayList<>();


    public AccountSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
        text_city = view.findViewById(R.id.text_city);
        textN1 = view.findViewById(R.id.textN1);
        textN2 = view.findViewById(R.id.textN2);
        neighborhoodText = view.findViewById(R.id.neighborhoodText);

        // now only change neighborhood
        text_city.setVisibility(View.GONE);
        s_city.setVisibility(View.GONE);
        //


        save_changes.setVisibility(View.GONE);
        try {
            setNeighborhoods();

        } catch (Exception e) {
            Log.e("error set nigh", e.getMessage());
        }
        if (!App.USER_NEIGHBORHOOD.equalsIgnoreCase("null")) {
            Log.e("gone", "inside gone");
            String name_ar_nei = neighborhoodsListMap.get(Integer.parseInt(App.USER_NEIGHBORHOOD));
            textN1.setVisibility(View.GONE);
            s_neighborhood.setVisibility(View.GONE);
            neighborhoodText.setText(name_ar_nei);


        } else {
            Log.e("gone", "not gone");
            textN2.setVisibility(View.GONE);
            neighborhoodText.setVisibility(View.GONE);
        }


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
        Log.e("user gender", "gender: " + App.USER_GENDER);

        if (App.USER_GENDER == null || App.USER_GENDER.equalsIgnoreCase("null")) {
            Log.e("iam here", "iam here");
            radio_male.setChecked(false);
            radio_female.setChecked(false);

        } else {
            if (App.USER_GENDER.equalsIgnoreCase("MALE")) {
                radio_male.setChecked(true);
                radio_female.setChecked(false);


            } else {
                Log.e("iam here women", "iam here women");

                radio_male.setChecked(false);
                radio_female.setChecked(true);

            }


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
                s_neighborhood.setEnabled(true);
                radio_male.setEnabled(true);
                radio_female.setEnabled(true);
                //
                textN2.setVisibility(View.GONE);
                neighborhoodText.setVisibility(View.GONE);
                textN1.setVisibility(View.VISIBLE);
                s_neighborhood.setVisibility(View.VISIBLE);


//                user_email.setEnabled(true);
//                user_phone.setEnabled(true);
//                s_neighborhood.setEnabled(true);
//                s_city.setEnabled(true);

                user_name.setTextColor(getResources().getColor(R.color.gray));
                user_email.setTextColor(getResources().getColor(R.color.gray));
                user_phone.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save changes to database
                String n_id = String.valueOf(spinnerMap.get(s_neighborhood.getSelectedItemPosition()));
//                if (user_name.getText().toString().trim().isEmpty() || user_phone.getText().toString().trim().isEmpty()) {
//                    user_name.setError("لا يمكن ترك الاسم أو رقم الجوال فارغ");
//                    user_name.requestFocus();
//                } else
                if (user_name.getText().toString().trim().equalsIgnoreCase(App.USER_NAME)
                        && user_phone.getText().toString().trim().equalsIgnoreCase(App.USER_PHONE)
                        && n_id.equalsIgnoreCase(App.USER_NEIGHBORHOOD)
                        && user_phone.getText().toString().trim().equalsIgnoreCase(App.USER_PHONE)) {
                    user_name.setError("لا يوجد شيء للحفظ");
                    user_name.requestFocus();

                } else if (!user_name.getText().toString().trim().isEmpty() && !isSaudiPhone(user_phone.getText().toString().trim())) {
                    user_phone.setError("الرجاء التأكد من صيغة رقم الجوال");
                    user_phone.requestFocus();
                } else {
                    String name;
                    String phone;
                    String gender;
                    String city;

                    if (user_name.getText().toString().trim().length() == 0)
                        name = null;
                    else
                        name = user_name.getText().toString().trim();


                    if (user_phone.getText().toString().trim().length() == 0)
                        phone = null;
                    else
                        phone = user_phone.getText().toString().trim();


                    if (radio_male.isChecked())
                        gender = "MALE";
                    else if (radio_female.isChecked())
                        gender = "FEMALE";
                    else if (!radio_male.isChecked() && !radio_female.isChecked())
                        gender = null;
                    else
                        gender = null;


                    App.confirmMessage = "تأكيد تعديل الملف الشخصي";
                    Intent intent = new Intent(getActivity(), ConfirmGreen.class);
                    startActivity(intent);
                    Log.e("name", name + "how");
                    Log.e("phone", phone + "how");
                    Log.e("n_id", n_id + "how");
                    Log.e("gender", gender + "how");

                    updateProfile(name, phone, null, n_id, gender);

                }

            }
        });


        return view;

    }

    private void updateProfile(String name, String phone, String city_id, String n_id, String gender) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserClient.BASE_URL_USER)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (name != null)
            builder.addFormDataPart("name", name);

        if (phone != null)
            builder.addFormDataPart("phone", phone);

        if (city_id != null)
            builder.addFormDataPart("city", city_id);

        if (n_id != null)
            builder.addFormDataPart("neighborhood", n_id);

        if (gender != null)
            builder.addFormDataPart("gender", gender);


        MultipartBody requestBody = builder.build();

        UserClient api = retrofit.create(UserClient.class);
        Call<ResponseBody> call = api.updateProfile(requestBody, "Bearer " + App.token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());

                        App.USER_NAME = res.getJSONObject("user_info").getString("name");
                        App.USER_EMAIL = res.getJSONObject("user_info").getString("email");
                        App.USER_PHONE = res.getJSONObject("user_info").getString("phone");
                        App.USER_NEIGHBORHOOD = res.getJSONObject("user_info").getString("neighborhood_id");
                        App.USER_GENDER = res.getJSONObject("user_info").getString("gender");


                        SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                        editUserInfo.putString("name", App.USER_NAME);
                        editUserInfo.putString("email", App.USER_EMAIL);
                        editUserInfo.putString("phone", App.USER_PHONE);
                        editUserInfo.putString("neighborhood_id", App.USER_NEIGHBORHOOD);
                        editUserInfo.putString("gender", App.USER_GENDER);


                        editUserInfo.apply();
                        Intent intent = new Intent(getActivity(), MainNavActivity.class);

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

    public void setNeighborhoods() {

        resNeighborhoods = App.sharedPreferences.getString("neighborhoodsResponse", null);
        try {
            JSONArray jsonArray = new JSONArray(resNeighborhoods);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (jsonArray.getJSONObject(i));
                int id = Integer.parseInt(jsonObject.getString("id"));
                String ne_ar = jsonObject.getString("name_ar");
                neighborhoodsListMap.put(id, ne_ar);
            }

        } catch (Exception e) {
            Log.e("erro match ", e.getMessage());
        }

        Log.e("res of ne", resNeighborhoods);

        Type listType = new TypeToken<List<Neighborhood>>() {

        }.getType();
        neighborhoodList = getNeFromJson(resNeighborhoods, listType);
        spinnerArray = new String[neighborhoodList.size()];
        for (int i = 0; i < neighborhoodList.size(); i++) {
            spinnerMap.put(i, neighborhoodList.get(i).getId());
            spinnerArray[i] = neighborhoodList.get(i).getName_ar();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_neighborhood.setAdapter(adapter);
    }

    public static <T> List<T> getNeFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }
}
