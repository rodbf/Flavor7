package com.flavorplus.util.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private Context context;
    private SQLiteDatabase database;
    private SQLiteSearchHistoryHelper dbHelper;

    public DBManager(Context c){
        this.context = c;
    }

    public DBManager open() throws SQLException{
        this.dbHelper = new SQLiteSearchHistoryHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.database.close();
        this.dbHelper.close();
    }

    public void insert(String str){
        ContentValues contentValue = new ContentValues();
        contentValue.put(SQLiteSearchHistoryHelper.STR, str);
        this.database.insert(SQLiteSearchHistoryHelper.TABLE_NAME_HISTORY, null, contentValue);
    }

    public Cursor fetch(String str){
        Cursor cursor = this.database.query(SQLiteSearchHistoryHelper.TABLE_NAME_HISTORY, new String[]{SQLiteSearchHistoryHelper.STR}, "str = ?", new String[]{str}, null, null, null, "1");
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchLike(String str){
        Cursor cursor = this.database.query(SQLiteSearchHistoryHelper.TABLE_NAME_HISTORY, new String[]{SQLiteSearchHistoryHelper.STR}, "str like ?", new String[]{"%"+str+"%"}, null, null, "str" , "3");
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(long _id){
        this.database.delete(SQLiteSearchHistoryHelper.TABLE_NAME_HISTORY, "_id=" + _id, null);
    }

}
