package com.gp.salik.activities;

import android.os.Build;
import android.os.Bundle;
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

import com.gp.salik.R;


public class EmpTicketListFragment extends Fragment {


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

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ticketsRecycler.setLayoutManager(llm);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        ticketsRecycler.addItemDecoration(itemDecorator);

        //TODO: load emp tickets

        return v;
    }
}
