package com.flavorplus.util.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteSearchHistoryHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_HISTORY = " create table HISTORY( _id INTEGER PRIMARY KEY AUTOINCREMENT, str TEXT NOT NULL);";
    private static final String DB_NAME = "SEARCH_HISTORY.DB";
    private static final int DB_VERSION = 1;
    public static final String STR = "str";
    public static final String TABLE_NAME_HISTORY = "HISTORY";

    public SQLiteSearchHistoryHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS HISTORY");
        onCreate(db);
    }
}
