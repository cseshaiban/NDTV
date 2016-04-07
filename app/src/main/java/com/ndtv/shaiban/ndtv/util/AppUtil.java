package com.ndtv.shaiban.ndtv.util;

import android.content.Context;
import android.database.Cursor;

import com.ndtv.shaiban.ndtv.pojo.PatientList;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by shaiban on 5/4/16.
 */
public class AppUtil {
    public static String loadPatientListFromAsset(Context context, String jsonFileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getTimeFromDate(String inputDate) {
        if (inputDate == null || inputDate.length() < 19) {
            return "";
        }
        SimpleDateFormat formatter1, formatter2;
        formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = formatter1.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter2 = new SimpleDateFormat("HH:mm:ss");
        String processedTime = formatter2.format(date);
        return processedTime;
    }


    public static boolean compareDates(String appointmentTime) throws ParseException {
        try {
            String string1 = "10:00:00";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            String string2 = "18:00:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            String someRandomTime = appointmentTime;
            Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                //True for time between 10am to 6pm
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String setAppointmentToNextDay(String date) throws ParseException {

        String dt = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(dt));

        cal.add(Calendar.DATE, 1); // change to next day

        cal.set(Calendar.HOUR,10);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        dt = sdf.format(cal.getTime());  // dt is now updated day

        if (!dt.isEmpty())
            return dt;
        else
            return null;
    }

    public static String addEmergencyTime(String date, int hour, int minute) throws ParseException {

        String dt = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        if (hour != 0)
            c.add(Calendar.HOUR, hour);

        if (minute != 0)
            c.add(Calendar.MINUTE, minute);

        dt = sdf.format(c.getTime());  // dt is now updated day

        if (!compareDates(getTimeFromDate(dt))) {
            return setAppointmentToNextDay(dt);
        } else {
            return dt;
        }
    }

    public static ArrayList<PatientList> getPatientListFromCursor(Cursor cursor){

        ArrayList<PatientList> patientLists = new ArrayList<PatientList>();

        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                PatientList patientList = new PatientList();

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("key_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("key_name"));
                String appointment = cursor.getString(cursor.getColumnIndexOrThrow("key_appointment_date_time"));

                patientList.setId(id);
                patientList.setName(name);
                patientList.setDate(appointment);

                patientLists.add(patientList);

            } while (cursor.moveToNext());

        if (cursor != null)
            cursor.close();

        return patientLists;
    }


}
