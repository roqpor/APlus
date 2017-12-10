package com.aplus.pillreminder.fragment;


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
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;
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
public class PillBagFragment extends Fragment implements SwipeMenuListView.OnMenuItemClickListener {

    public static String TAG = PillBagFragment.class.getSimpleName();
    private PillReminderDb db;
    private List<PillWithRemindTime> pillWithRemindTimes;
    private PillInfoAdapter adapter;
    public SwipeMenuListView listView;
    private OnSwipeMenuClickListener listener;

    public interface OnSwipeMenuClickListener {
        void onEditClicked(PillWithRemindTime pillWithRemindTime);
    }

    public PillBagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseManager.getInstance().getDb();
        pillWithRemindTimes = new ArrayList<>();
        adapter = new PillInfoAdapter(getActivity(), R.layout.item_pill_info_list, pillWithRemindTimes);
        listener = (OnSwipeMenuClickListener) getActivity();

        loadPills();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pill_bag, container, false);

        setup(view);

        return view;
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        PillWithRemindTime pillWithRemindTime = pillWithRemindTimes.get(position);

        switch (index) {
            case 0:
                // edit pill
                listener.onEditClicked(pillWithRemindTime);
                break;
            case 1:
                // delete pill
                deletePill(pillWithRemindTime.getPill());
                break;
        }
        // false : close the menu; true : not close the menu
        return true;
    }

    public void loadPills() {
        new AsyncTask<Void, Void, List<PillWithRemindTime>>() {
            @Override
            protected List<PillWithRemindTime> doInBackground(Void... voids) {
                return db.pillDao().loadPillsWithRemindTimes();
            }

            @Override
            protected void onPostExecute(List<PillWithRemindTime> pillWithRemindTimes) {
                adapter.clear();
                adapter.addAll(pillWithRemindTimes);
            }
        }.execute();
    }

    private void setup(View view) {
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        createSwipeMenu();
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(getActivity());
                editItem.setBackground(android.R.color.darker_gray);
                editItem.setWidth(dp2px(90));
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(android.R.color.holo_red_light);
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(this);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void deletePill(final Pill pill) {
        // TODO: delete reminder schedule

        new AsyncTask<Void, Void, Pill>() {
            @Override
            protected Pill doInBackground(Void... voids) {
                db.pillDao().delete(pill);

                return null;
            }

            @Override
            protected void onPostExecute(Pill pill) {
                loadPills();
            }
        }.execute();
    }
}
