package com.gp.salik.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gp.salik.Model.App;
import com.gp.salik.R;

public class ConfirmGreen extends Activity {

    private TextView confirm_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_green);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (height > 1900) {
            getWindow().setLayout((int) (width * 1.), (int) (height * .58));
        } else {
            getWindow().setLayout((int) (width * 1.), (int) (height * .7));
        }

        WindowManager.LayoutParams wlp = getWindow().getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(wlp);

        confirm_ok = findViewById(R.id.confirm_good);
        Log.e("app confirm", App.confirmMessage + "  000");
        confirm_ok.setText(App.confirmMessage);
        confirm_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send ticket + list tickets

            }
        });

        Handler handler = new Handler();
        int time = 2000;//Millis
        if (App.confirmMessage.equalsIgnoreCase("تأكيد إضافة التذكرة")
                || App.confirmMessage.equalsIgnoreCase("تأكيد رفع الصور وإغلاق التذكرة")) {
            time = 1000000;
        }

        handler.postDelayed(new Runnable() {
            public void run() {

                finish();
            }
        }, time);


    }
}
