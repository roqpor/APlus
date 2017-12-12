package com.aplus.pillreminder.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.adapter.EatLogAdapter;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EatLogFragment extends Fragment {

    public static final String KEY_DATE = "date";
    @BindView(R.id.listMissed) RecyclerView recyclerViewMissed;
    @BindView(R.id.listTaken) RecyclerView recyclerViewTaken;
    private EatLogAdapter adapterMissed, adapterTaken;
    private List<EatLog> listMissed, listTaken;
    private long date;
    private PillReminderDb db;

    public EatLogFragment() {
        // Required empty public constructor
    }

    public EatLogFragment newInstance(long date) {
        Bundle args = new Bundle();
        args.putLong(KEY_DATE, date);
        EatLogFragment fragment = new EatLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseManager.getInstance().getDb();
        adapterMissed = new EatLogAdapter(getActivity());
        adapterTaken = new EatLogAdapter(getActivity());
        listMissed = new ArrayList<>();
        listTaken = new ArrayList<>();
        date = getArguments().getLong(KEY_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eat_log, container, false);
        ButterKnife.bind(this, view);
        setup();
        loadEatLogs();
        return view;
    }

    private void setup() {
        recyclerViewMissed.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMissed.setAdapter(adapterMissed);
        recyclerViewTaken.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTaken.setAdapter(adapterTaken);
    }

    private void loadEatLogs() {
        new AsyncTask<Long, Void, List<EatLog>>() {
            @Override
            protected List<EatLog> doInBackground(Long... longs) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(longs[0]);

                // Date at 00:00.00
                Date dayStart = calendar.getTime();

                // Date at 23:59.59
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                Date dayEnd = calendar.getTime();

                return db.eatLogDao().getEatLogs(dayStart, dayEnd);
            }

            @Override
            protected void onPostExecute(List<EatLog> eatLogs) {
                for (EatLog eatLog : eatLogs) {
                    if (eatLog.isTaken()) {
                        listTaken.add(eatLog);
                    } else {
                        listMissed.add(eatLog);
                    }
                }

                adapterMissed.setData(listMissed);
                adapterTaken.setData(listTaken);
                adapterMissed.notifyDataSetChanged();
                adapterTaken.notifyDataSetChanged();
            }
        }.execute(date);
    }
}
