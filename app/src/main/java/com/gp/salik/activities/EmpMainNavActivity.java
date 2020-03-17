package com.gp.salik.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.gp.salik.Model.App;
import com.gp.salik.Model.ViewPagerAdapter;
import com.gp.salik.R;

public class EmpMainNavActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView logoutCircle, ticketsCircle, emp_name, emp_tickets_num;
    private ViewPagerAdapter vpadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_emp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        vpadapter = new ViewPagerAdapter(getSupportFragmentManager());

        logoutCircle = findViewById(R.id.logout_circle);
        ticketsCircle = findViewById(R.id.emp_tickets_circle);
        emp_name = findViewById(R.id.emp_name);
        emp_tickets_num = findViewById(R.id.emp_tickets_num);
        emp_tickets_num.setText(String.valueOf(App.TICKET_NUM));

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(vpadapter);

        if (App.USER_NAME == null || App.USER_NAME.equalsIgnoreCase("null")) {
            emp_name.setText("");

        } else {
            String mystring = App.USER_NAME;
            String arr[] = mystring.split(" ", 2);

            String firstWord = arr[0];   //first name of user

            emp_name.setText(firstWord);
        }


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        logoutCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: logout
                // TODO: make sure the activities are all closed
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

        ticketsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EmpTicketListFragment());
                trans.commit();
            }
        });
    }
}
