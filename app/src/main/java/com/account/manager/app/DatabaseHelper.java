package com.account.manager.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String KEY_Address = "address";
    public static final String KEY_NAME = "name";
    public static final String KEY_Number = "phoneno";
    private static final String DATABASE_NAME = "Account_DB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PersonRecord = "tblrecords";
    private static final String TAG = "DBAdapter";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("create table tblrecords (id integer primary key autoincrement,name text not null, phoneno text not null,date text not null,address text not null);");
            sQLiteDatabase.execSQL("create table tbltransaction (tid integer primary key autoincrement,rowid text not null, creditamt text not null,debitamt text not null,balanceamt text not null ,paydate text not null,note text not null);");
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblrecords");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbltransaction");
        onCreate(sQLiteDatabase);
    }

    public void insertRecords(String str, String str2, String str3, String str4) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str);
        contentValues.put(KEY_Number, str2);
        contentValues.put("date", str3);
        contentValues.put(KEY_Address, str4);
        writableDatabase.insert(TABLE_PersonRecord, null, contentValues);
        writableDatabase.close();
    }

}
