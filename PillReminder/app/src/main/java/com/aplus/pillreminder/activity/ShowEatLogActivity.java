package com.aplus.pillreminder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.fragment.EatLogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowEatLogActivity extends AppCompatActivity {

    public static final String KEY_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_eat_log);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long date = getIntent().getLongExtra(KEY_DATE, 0);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String dateString = formatter.format(new Date(date));
        setTitle(dateString);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new EatLogFragment().newInstance(date))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
