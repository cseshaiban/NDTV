package com.ndtv.shaiban.ndtv.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndtv.shaiban.ndtv.R;
import com.ndtv.shaiban.ndtv.pojo.PatientList;

import java.text.ParseException;
import java.util.ArrayList;

import com.ndtv.shaiban.ndtv.textdrawable.TextDrawable;

/**
 * Created by shaiban on 5/4/16.
 */
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private ArrayList<PatientList> arraylist;
    private OnItemClickListener onItemClickListener;

    public PatientAdapter(ArrayList<PatientList> list, OnItemClickListener onItemClickListener) {
        this.arraylist = list;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onPatientClick(View view, int patientId) throws ParseException;

        public void onAccept(View view, int patientId, String name, String date) throws ParseException;

        public void onCancel(View view, int patientId, String name, String date) throws ParseException;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name, age, gender, city, phone, date, bloodgroup, accept, cancel;
        public final ImageView imageView;
        public final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.name);
            age = (TextView) view.findViewById(R.id.age);
            gender = (TextView) view.findViewById(R.id.gender);
            city = (TextView) view.findViewById(R.id.city);
            date = (TextView) view.findViewById(R.id.date);
            phone = (TextView) view.findViewById(R.id.phone);
            bloodgroup = (TextView) view.findViewById(R.id.bloodgroup);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            accept = (TextView) view.findViewById(R.id.accept);
            cancel = (TextView) view.findViewById(R.id.cancel);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_list, null);
        ViewHolder ml = new ViewHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PatientList patientList = arraylist.get(position);
        holder.name.setText("" + patientList.name);
        holder.gender.setText("Gender: " + patientList.gender);
        holder.city.setText("City: " + patientList.city);
        holder.phone.setText("Contact: " + patientList.mobile);

        String date = patientList.date;
        String time = "", ndate = "";
        if (!TextUtils.isEmpty(date)) {
            ndate = date.substring(0, 10);
            time = date.substring(11, date.length());
        }
        holder.date.setText("Date: " + ndate + " Time: " + time);

        holder.name.setText("" + patientList.name);
        holder.age.setText("Age :" + patientList.age);
        holder.bloodgroup.setText("Blood group :" + patientList.bloodGroup);

        TextDrawable drawable = TextDrawable.builder().buildRoundRect(patientList.name, Color.RED, 10);
        holder.imageView.setImageDrawable(drawable);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClickListener.onPatientClick(v, patientList.id);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteApproved(position);
                    onItemClickListener.onAccept(v, patientList.id, patientList.name, patientList.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteApproved(position);
                    onItemClickListener.onCancel(v, patientList.id, patientList.name, patientList.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    private void deleteApproved(int position) {

        try {
            Log.d("DELETE", "Position " + position);
            arraylist.remove(position);
            notifyItemRemoved(position);
            Log.d("DELETE", "deleteApproved ");


        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (NullPointerException e) {

        } catch (IndexOutOfBoundsException e) {

        }

    }


}
