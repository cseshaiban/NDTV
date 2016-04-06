package com.ndtv.shaiban.ndtv.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ndtv.shaiban.ndtv.DatabaseHelper;
import com.ndtv.shaiban.ndtv.R;
import com.ndtv.shaiban.ndtv.adapters.SplashAdapter;
import com.ndtv.shaiban.ndtv.pojo.PatientList;
import com.ndtv.shaiban.ndtv.util.AppUtil;

import java.util.ArrayList;

/**
 * Created by shaiban on 5/4/16.
 */
public class SplashScreen extends AppCompatActivity {
    private TextView skip, upcoming;
    private RecyclerView recyclerView;
    private ArrayList<PatientList> patientLists = new ArrayList<PatientList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        skip = (TextView) findViewById(R.id.skip);

        recyclerView = (RecyclerView) findViewById(R.id.splash_recycler_view);
        upcoming = (TextView) findViewById(R.id.upcoming);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        Cursor cursor = DatabaseHelper.getInstance(this).fetchAcceptedAppointments();
        patientLists.addAll(AppUtil.getPatientListFromCursor(cursor));

        if (patientLists.size() != 0)
            recyclerView.setAdapter(new SplashAdapter(patientLists));
        else
            upcoming.setText("WELCOME \n NO UPCOMNING APPOINTMENT ");

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
