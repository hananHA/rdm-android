package com.example.rdm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.rdm.Model.App;
import com.example.rdm.Model.TicketList;
import com.example.rdm.Model.TicketListAdapter;
import com.example.rdm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketListActivity extends AppCompatActivity {

    public static List<JSONObject> ticketArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView hasText;
    //a list to store all the products
    List<TicketList> ticketListClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketListClass = new ArrayList<>();
        hasText = findViewById(R.id.hasTicket);
        hasText.setVisibility(View.INVISIBLE);

        loadListTicket();

    }

    public void loadListTicket() {


        try {

            JSONArray jsonarray = new JSONArray(App.listTicketResponse);
            if (jsonarray.length() == 0) {
                hasText.setVisibility(View.VISIBLE);

            } else {


                int i;
                JSONObject json_data;
                JSONObject ticket = null;
                for (i = 0; i < jsonarray.length(); i++) {
                    json_data = jsonarray.getJSONObject(i);
                    ticket = (JSONObject) json_data.get("ticket");
                    ticketArrayList.add(i, ticket);
                }


                for (i = 0; i < ticketArrayList.size(); i++) {
                    ticketListClass.add(
                            new TicketList(
                                    ticketArrayList.get(i).getInt("id"),
                                    ticketArrayList.get(i).getString("description"),
                                    ticketArrayList.get(i).getString("status"),
                                    ticketArrayList.get(i).getString("classification")
                            ));

                }
//
                //creating recyclerview adapter
                TicketListAdapter adapter = new TicketListAdapter(this, ticketListClass);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

        } catch (Exception e) {
            Log.e("error load list ticket", e.getMessage());

        }
    }
}




