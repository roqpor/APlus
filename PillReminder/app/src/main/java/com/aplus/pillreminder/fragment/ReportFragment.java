package com.aplus.pillreminder.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    public static String TAG = ReportFragment.class.getSimpleName();

    @BindView(R.id.container_report) FrameLayout container_report;
    private PillReminderDb db;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseManager.getInstance().getDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, rootView);

        loadEatLogs();

        return rootView;
    }

    private void loadEatLogs() {
        new AsyncTask<Void, Void, List<EatLog>>() {
            @Override
            protected List<EatLog> doInBackground(Void... voids) {
                return db.eatLogDao().getAll();
            }

            @Override
            protected void onPostExecute(List<EatLog> eatLogs) {
                for (EatLog e : eatLogs) {
                    Log.wtf("EATLOGS", "Name="+e.getPillName()+
                            ",Date="+e.getDate().toString()+
                            ",isTaken="+e.isTaken()+
                            ",pill_id="+e.getPillId());
                }
            }
        }.execute();
    }
}
