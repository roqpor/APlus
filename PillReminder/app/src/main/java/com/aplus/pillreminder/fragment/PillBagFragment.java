package com.aplus.pillreminder.fragment;


import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.adapter.PillInfoAdapter;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.Pill;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

// TODO: Edit pill.
/**
 * A simple {@link Fragment} subclass.
 */
public class PillBagFragment extends Fragment {

    public static String TAG = PillBagFragment.class.getSimpleName();
    private PillReminderDb pillReminderDb;
    private List<Pill> pillList;
    private PillInfoAdapter adapter;
    private SwipeMenuListView listView;

    public PillBagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pillReminderDb = Room.databaseBuilder(getActivity(),
                PillReminderDb.class, "PillReminder.db")
                .fallbackToDestructiveMigration()
                .build();

        pillList = new ArrayList<>();
        adapter = new PillInfoAdapter(getActivity(), R.layout.item_pill_info_list, pillList);

        queryPills();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pill_bag, container, false);

        setup(view);

        return view;
    }

    private void setup(View view) {
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        createSwipeMenu();
    }

    private void queryPills() {
        new AsyncTask<Void, Void, List<Pill>>() {
            @Override
            protected List<Pill> doInBackground(Void... voids) {
                return pillReminderDb.pillDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Pill> pills) {
                adapter.clear();
                adapter.addAll(pills);
            }
        }.execute();
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
                Pill pill = pillList.get(position);

                switch (index) {
                    case 0:
                        // delete pill
                        deletePill(pill);
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });
    }

    private void deletePill(final Pill pill) {
        // TODO: delete reminder schedule

        new AsyncTask<Void, Void, Pill>() {
            @Override
            protected Pill doInBackground(Void... voids) {
                pillReminderDb.pillDao().delete(pill);

                return null;
            }

            @Override
            protected void onPostExecute(Pill pill) {
                queryPills();
            }
        }.execute();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
