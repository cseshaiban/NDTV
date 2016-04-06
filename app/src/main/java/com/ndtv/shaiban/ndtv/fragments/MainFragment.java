package com.ndtv.shaiban.ndtv.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ndtv.shaiban.ndtv.DatabaseHelper;
import com.ndtv.shaiban.ndtv.R;
import com.ndtv.shaiban.ndtv.adapters.PatientAdapter;
import com.ndtv.shaiban.ndtv.pojo.PatientList;
import com.ndtv.shaiban.ndtv.util.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by shaiban on 5/4/16.
 */
public class MainFragment extends Fragment {

    private Toolbar mToolbar;
    private TextView click, reset;
    private ArrayList<PatientList> patientLists;
    private PatientAdapter patientAdapter;
    private RecyclerView patientRecyclerView;
    private EditText hour, minute;
    private TextView submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_main, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("NDTV - Appointment Page");

        patientRecyclerView = (RecyclerView) rootView.findViewById(R.id.patient_recyclerview);

        patientRecyclerView.setLayoutManager(new LinearLayoutManager(patientRecyclerView.getContext()));
        patientRecyclerView.setHasFixedSize(true);


        reset = (TextView) rootView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance(getContext()).flushDb();
                loadAppointmentRequest();
                setFragmentAdapter();
            }
        });

        click = (TextView) rootView.findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmegencyTimerDialog(getContext());
            }
        });

        loadAppointmentRequest();
        setFragmentAdapter();

        return rootView;
    }

    private void showEmegencyTimerDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyMaterialTheme);
        final View dialogView = View.inflate(context, R.layout.layout_emergency_time, null);
        hour = (EditText) dialogView.findViewById(R.id.hour);
        minute = (EditText) dialogView.findViewById(R.id.minute);
        submit = (TextView) dialogView.findViewById(R.id.submit);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateTime(hour.getText().toString(), minute.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    private void updateTime(String hour, String minute) throws ParseException {

        ArrayList<PatientList> patientLists = new ArrayList<PatientList>();
        Cursor cursor = DatabaseHelper.getInstance(getActivity()).fetchAcceptedAppointments();
        patientLists.addAll(AppUtil.getPatientListFromCursor(cursor));

        int h = 0, m = 0;

        if (!TextUtils.isEmpty(hour)) {
            h = Integer.parseInt(hour);
        }
        if (!TextUtils.isEmpty(minute)) {
            m = Integer.parseInt(minute);
        }

        for (int i = 0; i < patientLists.size(); i++) {
            String updatedTime = AppUtil.addEmergencyTime(patientLists.get(i).getDate(), h, m);
            DatabaseHelper.getInstance(getContext()).updateEmergencyTime(patientLists.get(i).getId(), updatedTime);
        }
    }

    private void setFragmentAdapter() {
        patientAdapter = new PatientAdapter(patientLists, new PatientAdapter.OnItemClickListener() {
            @Override
            public void onPatientClick(View view, int patientId) throws ParseException {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, PatientDetailFragment.newInstance(patientId));
                transaction.addToBackStack(null).commitAllowingStateLoss();
            }

            @Override
            public void onAccept(View view, int patientId, String name, String date) throws ParseException {
                if (AppUtil.compareDates(AppUtil.getTimeFromDate(date))) {
                    DatabaseHelper.getInstance(getContext()).addAcceptedAppointmentInBD(patientId, name, "accept", date);
                } else {
                    DatabaseHelper.getInstance(getContext()).addAcceptedAppointmentInBD(patientId, name, "accept", AppUtil.setAppointmentToNextDay(date));
                }
            }

            @Override
            public void onCancel(View view, int patientId, String name, String date) throws ParseException {
                DatabaseHelper.getInstance(getContext()).addAcceptedAppointmentInBD(patientId, name, "reject", date);
            }
        });
        patientRecyclerView.setAdapter(patientAdapter);
    }

    private void loadAppointmentRequest() {

        //Fetches All data fom JSON
        String json = AppUtil.loadPatientListFromAsset(getActivity(), "patient_list_api.json");

        //Fetches Acted Data from Local Db
        ArrayList<Integer> actedId = new ArrayList<Integer>();
        Cursor cursor = DatabaseHelper.getInstance(getActivity()).fetchActedAppointments();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                actedId.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();

        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jArry = obj.getJSONArray("patients");
            patientLists = new ArrayList<>();

            for (int i = 0; i < jArry.length(); i++) {
                JSONObject jo_inside = jArry.getJSONObject(i);

                PatientList patientList = new PatientList();

                int id = jo_inside.getInt("id");
                patientList.setName(jo_inside.getString("name"));
                patientList.setId(id);
                patientList.setAge(jo_inside.getInt("age"));
                patientList.setGender(jo_inside.getString("gender"));
                patientList.setCity(jo_inside.getString("city"));
                patientList.setBloodGroup(jo_inside.getString("bloodGroup"));
                patientList.setAddress(jo_inside.getString("address"));
                patientList.setEmailId(jo_inside.getString("emailId"));
                patientList.setProfile(jo_inside.getString("profile"));
                patientList.setMobile(jo_inside.getString("mobileNo"));
                String date = jo_inside.getString("date");
                patientList.setDate(date.substring(0, 19));

                if (!actedId.contains(id))
                    patientLists.add(patientList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
