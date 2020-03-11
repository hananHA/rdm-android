package com.gp.salik.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.salik.R;
import com.gp.salik.activities.MainNavActivity;
import com.gp.salik.activities.TicketDetailsFragment;

import java.util.List;

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
        switch (ticket.getStatus()){
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
        holder.ticketInfo.setText(ticket.getDescription()); //String.valueOf(ticket.getId())
        holder.status.setText(ticket.getStatus());
        holder.date.setText("التصنيف: " + (ticket.getClassification()));
        holder.ticket_id.setText(String.valueOf(ticket.getId()));


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
                    FragmentTransaction trans = ((MainNavActivity)mCtx).getSupportFragmentManager()
                            .beginTransaction();
                    trans.replace(R.id.root_frame, new TicketDetailsFragment());
                    trans.commit();
                }
            });

        }

    }


}