package com.gp.salik.Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.salik.R;
import com.gp.salik.activities.EmpMainNavActivity;
import com.gp.salik.activities.EmpTicketDetailsFragment;
import com.gp.salik.activities.MainNavActivity;
import com.gp.salik.activities.TicketDetailsFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.gp.salik.Model.App.USER_ROLE;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketListViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;
    View v;

    //we are storing all the products in a list
    private List<TicketList> ticketLists;


    //getting the context and product list with constructor
    public TicketListAdapter(Context mCtx, List<TicketList> ticketLists) {
        this.mCtx = mCtx;
        this.ticketLists = ticketLists;
    }

    @Override
    public TicketListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.ticket_row, null);
        v = view;
        return new TicketListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketListViewHolder holder, int position) {
        //getting the product of the specified position
        TicketList ticket = ticketLists.get(position);

        //binding the data with the viewholder views
        switch (ticket.getStatus()) {
            case "OPEN":
                v.setBackgroundResource(R.drawable.opened_row_bg);
                break;
            case "ASSIGNED":
                v.setBackgroundResource(R.drawable.assigned_row_bg);
                break;
            case "CLOSED":
                v.setBackgroundResource(R.drawable.closed_row_bg);
                break;
        }
        if (ticket.getDescription().length() > 25) {
            holder.ticketInfo.setText(ticket.getDescription().substring(0, 25) + "..."); //String.valueOf(ticket.getId())

        } else if (ticket.getDescription().equalsIgnoreCase("null") || ticket.getDescription().isEmpty() || ticket.getDescription() == null) {
            holder.ticketInfo.setText("لا يوجد وصف للتذكرة");

        } else {
            holder.ticketInfo.setText(ticket.getDescription());
        }

        holder.status.setText(ticket.getStatus_ar());
        String subDate = ticket.getCreated_at().substring(0, 10);
        String date = "التاريخ: " + subDate;
        holder.date.setText(date);
        holder.ticket_id.setText(String.valueOf(ticket.getId()));

//        try {
//            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(subDate);
//
//            Log.e("date String ", date.toString());
//            holder.date.setText(dateAr + date);
//
//
//        } catch (Exception e) {
//            Log.e("error date", e.getMessage());
//            holder.date.setText(date2);
//
//        }


    }


    @Override
    public int getItemCount() {
        return ticketLists.size();
    }


    class TicketListViewHolder extends RecyclerView.ViewHolder {


        TextView ticketInfo, status, date, ticket_id;

        public TicketListViewHolder(View itemView) {
            super(itemView);

            ticketInfo = itemView.findViewById(R.id.ticket_desc);
            status = itemView.findViewById(R.id.ticket_status);
            date = itemView.findViewById(R.id.ticket_date);
            ticket_id = itemView.findViewById(R.id.ticket_id);

            ticket_id.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = ticket_id.getText().toString();
                    App.TICKET = id;
                    if (status.getText().toString().contains("OPEN")) {
                        App.opened = true;
                    } else {
                        App.opened = false;
                    }
                    if(USER_ROLE.equals("4")) {
                        FragmentTransaction trans = ((EmpMainNavActivity) mCtx).getSupportFragmentManager()
                                .beginTransaction();
                        trans.replace(R.id.root_frame, new EmpTicketDetailsFragment());
                        trans.commit();
                    } else {
                        FragmentTransaction trans = ((MainNavActivity) mCtx).getSupportFragmentManager()
                                .beginTransaction();
                        trans.replace(R.id.root_frame, new TicketDetailsFragment());
                        trans.commit();
                    }
                }
            });

        }

    }


}