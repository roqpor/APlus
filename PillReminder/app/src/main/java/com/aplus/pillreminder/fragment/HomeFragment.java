package com.aplus.pillreminder.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aplus.pillreminder.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btnAdd;
    private HomeFragmentListener listener;

    public interface HomeFragmentListener {
        void onBtnAddPress();
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setup(view);

        return view;
    }

    private void setup(View view) {
        listener = (HomeFragmentListener) getActivity();

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                onBtnAdd();
                break;
        }
    }

    private void onBtnAdd() {
        listener.onBtnAddPress();
    }
}
