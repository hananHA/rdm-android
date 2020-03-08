package com.gp.salik.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.salik.Model.App;
import com.gp.salik.Model.TicketList;
import com.gp.salik.Model.TicketListAdapter;
import com.gp.salik.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketsListFragment extends Fragment {

    public static List<JSONObject> ticketArrayList;

    View v;
    private RecyclerView ticketsRecycler;
    List<TicketList> ticketListClass;
    TextView hasText;

    public TicketsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tickets_list_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        hasText = v.findViewById(R.id.hasTicket);
        hasText.setVisibility(View.GONE);

        ticketArrayList = new ArrayList<>();

        ticketsRecycler = v.findViewById(R.id.recyclerTickets);
        ticketListClass = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ticketsRecycler.setLayoutManager(llm);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        ticketsRecycler.addItemDecoration(itemDecorator);



        loadListTicket();

        return v;
    }

    //TODO: This doesn't work unless the user submit a ticket
    public void loadListTicket() {
        try {

            JSONArray jsonarray = new JSONArray(App.listTicketResponse);
            if (jsonarray.length() == 0) {
                hasText.setVisibility(View.VISIBLE);
            } else {
                hasText.setVisibility(View.GONE);
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
