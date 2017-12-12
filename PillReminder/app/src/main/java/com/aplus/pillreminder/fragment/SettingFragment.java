package com.aplus.pillreminder.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.aplus.pillreminder.GlobalVariable;
import com.aplus.pillreminder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public static String TAG = SettingFragment.class.getSimpleName();

    @BindView(R.id.sw_setting_notification)
    Switch sw_setting_notification;

    public SettingFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);

        if(GlobalVariable.getIsEnabled(getActivity())){

            sw_setting_notification.setChecked(true);
        } else {

            sw_setting_notification.setChecked(false);
        }

        setEventListener();

        return rootView;
    }

    private void setEventListener(){
        sw_setting_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getContext(), "On", Toast.LENGTH_SHORT).show();
                    GlobalVariable.setIsEnabled(getActivity(), true);
                } else {
                    Toast.makeText(getContext(), "Off", Toast.LENGTH_SHORT).show();
                    GlobalVariable.setIsEnabled(getActivity(), false);
                }
            }
        });
    }

}
