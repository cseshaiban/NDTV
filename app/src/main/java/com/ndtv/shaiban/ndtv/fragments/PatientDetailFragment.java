package com.ndtv.shaiban.ndtv.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndtv.shaiban.ndtv.Contants;
import com.ndtv.shaiban.ndtv.R;
import com.ndtv.shaiban.ndtv.pojo.PatientDetail;
import com.ndtv.shaiban.ndtv.textdrawable.TextDrawable;
import com.ndtv.shaiban.ndtv.util.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by shaiban on 5/4/16.
 */
public class PatientDetailFragment extends Fragment {

    private Toolbar mToolbar;
    private int patientId;
    private Map<Integer, PatientDetail> patientDetailMap = new LinkedHashMap<Integer, PatientDetail>();
    public TextView name, diagnosis, symptoms, medication, toBetakon, comments, knownDisease, doctor, specialty, id;
    private ImageView image;


    public static PatientDetailFragment newInstance(int patientId) {
        PatientDetailFragment patientDetailFragment = new PatientDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Contants.PATIENT_ID, patientId);
        patientDetailFragment.setArguments(args);
        return patientDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getInt(Contants.PATIENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_patient_detail, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Patient Detail");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        name = (TextView) rootView.findViewById(R.id.name);
        id = (TextView) rootView.findViewById(R.id.id);
        diagnosis = (TextView) rootView.findViewById(R.id.diagnosis);
        symptoms = (TextView) rootView.findViewById(R.id.symptoms);
        medication = (TextView) rootView.findViewById(R.id.medication);
        toBetakon = (TextView) rootView.findViewById(R.id.toBetakon);
        comments = (TextView) rootView.findViewById(R.id.comments);
        knownDisease = (TextView) rootView.findViewById(R.id.knownDisease);
        doctor = (TextView) rootView.findViewById(R.id.doctor);
        specialty = (TextView) rootView.findViewById(R.id.specialty);
        image = (ImageView) rootView.findViewById(R.id.image_view);

        String json = AppUtil.loadPatientListFromAsset(getActivity(), "patient_detail_api.json");
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jArry = obj.getJSONArray("patientDetails");

            for (int i = 0; i < jArry.length(); i++) {
                JSONObject jo_inside = jArry.getJSONObject(i);

                PatientDetail patientDetail = new PatientDetail();

                // patientDetail.setSpecialty(jo_inside.getString("specialty"));
                patientDetail.setName(jo_inside.getString("name"));
                patientDetail.setId(jo_inside.getInt("id"));
                patientDetail.setDiagnosis(jo_inside.getString("diagnosis"));
                patientDetail.setSymptoms(jo_inside.getString("symptoms"));
                patientDetail.setMedication(jo_inside.getString("medication"));
                patientDetail.setToBetakon(jo_inside.getString("toBeTaken"));
                patientDetail.setComments(jo_inside.getString("comments"));

                JSONArray jsonKnownDisease = jo_inside.getJSONArray("knownDeseases");
                StringBuilder charknownDisease = new StringBuilder();

                String inside = null;
                for (int j = 0; j < jsonKnownDisease.length(); j++) {
                    inside = jsonKnownDisease.getString(j);
                    charknownDisease.append(inside + " ");
                }

                patientDetail.setKnownDisease(charknownDisease.toString());
                patientDetail.setDoctor(jo_inside.getString("doctor"));

                patientDetailMap.put(jo_inside.getInt("id"), patientDetail);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PatientDetail patientDetails = patientDetailMap.get(patientId);

        if (patientDetails != null) {
            ;
            name.setText("Name: " + patientDetails.name);
            id.setText("id: " + patientDetails.id);
            diagnosis.setText("Diagnosis: " + patientDetails.diagnosis);
            symptoms.setText("Symptoms: " + patientDetails.symptoms);
            medication.setText("Medication: " + patientDetails.medication);
            toBetakon.setText("ToBetakon :" + patientDetails.toBetakon);
            comments.setText("Comments:\n" + patientDetails.comments);
            knownDisease.setText("Known Disease: " + patientDetails.knownDisease);
            doctor.setText("Doctor :" + patientDetails.doctor);
            specialty.setText("Specialty: " + patientDetails.specialty);

            TextDrawable drawable = TextDrawable.builder().buildRoundRect(patientDetails.name, Color.DKGRAY, 10);
            image.setImageDrawable(drawable);
        }

        return rootView;
    }
}
