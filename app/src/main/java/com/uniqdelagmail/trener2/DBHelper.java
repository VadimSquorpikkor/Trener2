package com.uniqdelagmail.trener2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SquorpikkoR on 05.09.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gymBase";
    public static final String TABLE_EXERCISE1 = "exercise1";

    public static final String KEY_ID = "_id";
    public static final String KEY_RESULT = "result";
    public static final String KEY_NEW_WEIGHT_COUNT = "dayRecordCount";
    public static final String KEY_HISTORY_RECORD_COUNT = "historyRecordCount";
    public static final String KEY_DATE = "date";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_EXERCISE1 + "("
                + KEY_ID + "integer primary key," + KEY_RESULT + " text,"
                + KEY_NEW_WEIGHT_COUNT + " text," + KEY_HISTORY_RECORD_COUNT + " text,"
                + KEY_DATE + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + TABLE_EXERCISE1);

        onCreate(db);
    }
}
