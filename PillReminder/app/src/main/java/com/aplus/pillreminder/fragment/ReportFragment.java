package com.aplus.pillreminder.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    public static String TAG = ReportFragment.class.getSimpleName();
    @BindView(R.id.listView) ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listDate;
    private PillReminderDb db;
    private ReportFragmentListener listener;

    public interface ReportFragmentListener {
        void onDateItemClicked(Date date);
    }

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseManager.getInstance().getDb();
        listDate = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.item_date_list, listDate);
        listener = (ReportFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, rootView);

        setup();

        loadEatLogs();

        return rootView;
    }

    private void setup() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    Date date = formatter.parse(listDate.get(position));
                    listener.onDateItemClicked(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadEatLogs() {
        new AsyncTask<Void, Void, List<EatLog>>() {
            @Override
            protected List<EatLog> doInBackground(Void... voids) {
                return db.eatLogDao().getAll();
            }

            @Override
            protected void onPostExecute(List<EatLog> eatLogs) {
                for (EatLog eatLog : eatLogs) {
                    Log.wtf("EATLOGS", "Name="+eatLog.getPillName()+
                            ",Date="+eatLog.getDate().toString()+
                            ",isTaken="+eatLog.isTaken()+
                            ",pill_id="+eatLog.getPillId());

                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String date = formatter.format(eatLog.getDate());
                    if (!listDate.contains(date)) {
                        listDate.add(date);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
