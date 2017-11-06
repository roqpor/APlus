package com.aplus.pillreminder.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.RemindTime;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPillFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "AddPillFragment";

    private ImageButton imgBtnAddTime;
    private List<RemindTime> timeList;
    private ArrayAdapter<RemindTime> adapter;
    private SwipeMenuListView listView;
    private AddPillFragmentListener listener;

    public interface AddPillFragmentListener {
        void onImgBtnAddTimePressed();
    }

    public AddPillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeList = new ArrayList<>();
        adapter = new ArrayAdapter<RemindTime>(getActivity(), android.R.layout.simple_list_item_1, timeList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pill, container, false);

        setup(view);

        return view;
    }

    private void setup(View view) {
        imgBtnAddTime = view.findViewById(R.id.imgBtnAddTime);
        imgBtnAddTime.setOnClickListener(this);

        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        createSwipeMenu();

        listener = (AddPillFragmentListener) getActivity();
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(android.R.color.holo_red_light);
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_forever_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                RemindTime remindTime = timeList.get(position);

                switch (index) {
                    case 0:
                        deleteTime(remindTime);
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnAddTime:
                onImgBtnAddTime();
                break;
        }
    }

    private void onImgBtnAddTime() {
        listener.onImgBtnAddTimePressed();
    }

    public void addTime(RemindTime remindTime) {
        timeList.add(remindTime);
        Collections.sort(timeList, new Comparator<RemindTime>() {
            @Override
            public int compare(RemindTime t1, RemindTime t2) {
                return t1.getTotalMinute() - t2.getTotalMinute();
            }
        });
        listView.invalidateViews();
    }

    private void deleteTime(RemindTime remindTime) {
        timeList.remove(remindTime);
        listView.invalidateViews();
    }
}
