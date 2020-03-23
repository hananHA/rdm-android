package com.gp.salik.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.gp.salik.Model.App;
import com.gp.salik.Model.TicketList;
import com.gp.salik.Model.TicketListAdapter;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EmpTicketListFragment extends Fragment {
    private List<TicketList> ticketListClass;


    View v;
    private RecyclerView ticketsRecycler;
    TextView hasTicket;

    public EmpTicketListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tickets_list_emp_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        hasTicket = v.findViewById(R.id.hasTicket_emp);
        hasTicket.setVisibility(View.GONE);

        ticketsRecycler = v.findViewById(R.id.recyclerTickets_emp);
        ticketListClass = new ArrayList<>();


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ticketsRecycler.setLayoutManager(llm);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        ticketsRecycler.addItemDecoration(itemDecorator);

        //TODO: load emp tickets

        loadListTicket();


        return v;
    }

    public void loadListTicket() {
        try {

            JSONArray jsonarray = new JSONArray(App.listTicketResponse);
            if (jsonarray.length() == 0) {
                hasTicket.setVisibility(View.VISIBLE);
            } else {
                hasTicket.setVisibility(View.GONE);
                int i;
                JSONObject json_data;
                JSONObject ticket = null;
                App.ticketArrayList.clear();
                App.ticketListMap.clear();
                for (i = 0; i < jsonarray.length(); i++) {
                    json_data = jsonarray.getJSONObject(i);
                    ticket = (JSONObject) json_data.get("ticket");
                    int t_id = ticket.getInt("id");
                    App.ticketArrayList.add(0, ticket);
                    App.ticketListMap.put(t_id, json_data);
                }
                for (i = 0; i < App.ticketArrayList.size(); i++) {
                    ticketListClass.add(
                            new TicketList(
                                    App.ticketArrayList.get(i).getInt("id"),
                                    App.ticketArrayList.get(i).getString("description"),
                                    App.ticketArrayList.get(i).getString("status"),
                                    App.ticketArrayList.get(i).getString("classification"),
                                    App.ticketArrayList.get(i).getString("status_ar"),
                                    App.ticketArrayList.get(i).getString("created_at")

                            ));
                }

                //creating recyclerview adapter
                TicketListAdapter adapter = new TicketListAdapter(this.getContext(), ticketListClass);

                //setting adapter to recyclerview
                ticketsRecycler.setAdapter(adapter);
            }

        } catch (Exception e) {
            Log.e("error load list ticket", e.getMessage());

        }
    }
}
