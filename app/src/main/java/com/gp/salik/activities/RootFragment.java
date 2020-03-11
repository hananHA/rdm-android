package com.gp.salik.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gp.salik.R;

public class RootFragment extends Fragment {

	private static final String TAG = "RootFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
		View view = inflater.inflate(R.layout.root_fragment, container, false);

		Log.e("NAME", "Root Fragment");
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.root_frame, new TicketsListFragment());
		transaction.commit();

		return view;
	}

}
