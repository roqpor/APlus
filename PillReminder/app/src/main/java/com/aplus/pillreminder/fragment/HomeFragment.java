package com.aplus.pillreminder.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.adapter.PillAdapter;
import com.aplus.pillreminder.model.Pill;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.gridMorning)
    RecyclerView gridMorning;

    @BindView(R.id.gridAfternoon)
    RecyclerView gridAfternoon;

    @BindView(R.id.gridEvening)
    RecyclerView gridEvening;

    @BindView(R.id.gridNight)
    RecyclerView gridNight;

    private PillAdapter adapterMorning, adapterAfternoon, adapterEvening, adapterNight;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        setup(view);

        return view;
    }

    private void setup(View view) {
        adapterMorning = new PillAdapter(new ArrayList<Pill>());
        adapterAfternoon = new PillAdapter(new ArrayList<Pill>());
        adapterEvening = new PillAdapter(new ArrayList<Pill>());
        adapterNight = new PillAdapter(new ArrayList<Pill>());

        gridMorning.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        gridAfternoon.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        gridEvening.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        gridNight.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        gridMorning.setAdapter(adapterMorning);
        gridAfternoon.setAdapter(adapterAfternoon);
        gridEvening.setAdapter(adapterEvening);
        gridNight.setAdapter(adapterNight);
    }
}
