package com.ndtv.shaiban.ndtv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by shaiban on 5/4/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int VERSION_CODE = 101;
    private static String DATABASE_NAME;

    static {
        DATABASE_NAME = "ndtvdatabase";
    }

    private static final String TABLE_NAME = "ndtvtable";
    private static final String KEY_ID = "key_id";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_STATUS = "key_status";
    private static final String KEY_APPOINTMENT_DATETIME = "key_appointment_date_time";

    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
             KEY_NAME + " TEXT," +
             KEY_STATUS + " TEXT," +
            KEY_APPOINTMENT_DATETIME + " DATETIME" + ")";

    private Context context;
    private static DatabaseHelper omniDatabaseHelper;

    public static DatabaseHelper getInstance(Context context) {
        if (omniDatabaseHelper == null) {
            omniDatabaseHelper = new DatabaseHelper(context);
        }
        return omniDatabaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_CODE);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public long addAcceptedAppointmentInBD(int id,String name, String status, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_STATUS, status);
        values.put(KEY_APPOINTMENT_DATETIME, date);

        // insert row
        long insertid = db.insert(TABLE_NAME, null, values);
        return insertid;
    }

    public Cursor fetchAcceptedAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        String filter = "accept";

        Cursor c = db.query(true, TABLE_NAME, new String[]{KEY_ID,KEY_NAME,
                        KEY_APPOINTMENT_DATETIME}, KEY_STATUS + " LIKE ?",
                new String[]{"%" + filter + "%"}, null, null, null,
                null);

        return c;
    }

    public Cursor fetchActedAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(true, TABLE_NAME, new String[]{KEY_ID}, null,
                null, null, null, null,
                null);

        return c;
    }

    public boolean updateEmergencyTime(int id, String updatedTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APPOINTMENT_DATETIME, updatedTime);

        return db.update(TABLE_NAME, values, KEY_ID + "=" + id, null) > 0;
    }

    public void flushDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, null, null);
    }
}
