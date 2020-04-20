package com.account.manager.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String KEY_Balance = "balanceamt";
    public static final String KEY_Credit = "creditamt";
    public static final String KEY_Address = "address";
    public static final String KEY_DATEPay = "paydate";
    public static final String KEY_Debit = "debitamt";
    public static final String KEY_Note = "note";
    public static final String KEY_NAME = "name";
    public static final String KEY_RowID = "rowid";
    public static final String KEY_Number = "phoneno";
    private static final String DATABASE_NAME = "Account_DB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PersonRecord = "tblrecords";
    private static final String TABLE_PersonTransaction = "tbltransaction";
    private static final String TAG = "DBAdapter";

    private Context mContext;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
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

    public Cursor GetAllparty() {
        return getReadableDatabase().rawQuery("SELECT id,name,phoneno,date,address FROM tblrecords order by id desc ", null);
    }

    public Cursor GetSingleparty(long j) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id,name,phoneno,date,address FROM tblrecords where id = ");
        sb.append(j);
        return readableDatabase.rawQuery(sb.toString(), null);
    }

    public void inserttransaction(String str, String str2, String str3, String str4, String str5, String str6) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_RowID, str);
        contentValues.put(KEY_Credit, str2);
        contentValues.put(KEY_Debit, str3);
        contentValues.put(KEY_Balance, str4);
        contentValues.put(KEY_DATEPay, str5);
        contentValues.put(KEY_Note, str6);
        writableDatabase.insert(TABLE_PersonTransaction, null, contentValues);
        writableDatabase.close();
    }

    public Cursor GetPartyTransaction(long j) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT tid,debitamt,creditamt,balanceamt,paydate,note FROM tbltransaction where rowid = ");
        sb.append(j);
        return readableDatabase.rawQuery(sb.toString(), null);
    }

    public Cursor GetAllTransaction(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT tid,debitamt,creditamt,balanceamt,paydate,note FROM tbltransaction WHERE paydate like '");
        sb.append(str);
        sb.append("%' order by tid desc");
        return readableDatabase.rawQuery(sb.toString(), null);
    }

    public void UpdatePartyInfo(String str, String str2, String str3, String str4, String str5) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, str2);
            contentValues.put(KEY_Number, str3);
            contentValues.put("date", str4);
            contentValues.put(KEY_Address, str5);
            String str6 = TABLE_PersonRecord;
            StringBuilder sb = new StringBuilder();
            sb.append("id = ");
            sb.append(str);
            writableDatabase.update(str6, contentValues, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(e.getMessage());
            sb2.append(" ");
            sb2.append(e.toString());
            Log.i("TAG = ", sb2.toString());
        }
    }

    public void UpdateTransactionRecord(String str, String str2, String str3, String str4, String str5) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_Credit, str2);
            contentValues.put(KEY_Debit, str3);
            contentValues.put(KEY_DATEPay, str4);
            contentValues.put(KEY_Note, str5);
            String str6 = TABLE_PersonTransaction;
            StringBuilder sb = new StringBuilder();
            sb.append("tid = ");
            sb.append(str);
            writableDatabase.update(str6, contentValues, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(e.getMessage());
            sb2.append(" ");
            sb2.append(e.toString());
            Log.i("TAG = ", sb2.toString());
        }
    }

    public void delparty(long j) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str = TABLE_PersonRecord;
            StringBuilder sb = new StringBuilder();
            sb.append(" id =  ");
            sb.append(j);
            writableDatabase.delete(str, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(e.getMessage());
            sb2.append(" ");
            sb2.append(e.toString());
            Log.i("TAG", sb2.toString());
        }
    }

    public void delrecords(long j) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str = TABLE_PersonTransaction;
            StringBuilder sb = new StringBuilder();
            sb.append(" rowid =  ");
            sb.append(j);
            writableDatabase.delete(str, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(e.getMessage());
            sb2.append(" ");
            sb2.append(e.toString());
            Log.i("TAG", sb2.toString());
        }
    }

    public void DeletTransaction(long j) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str = TABLE_PersonTransaction;
            StringBuilder sb = new StringBuilder();
            sb.append(" tid =  ");
            sb.append(j);
            writableDatabase.delete(str, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(e.getMessage());
            sb2.append(" ");
            sb2.append(e.toString());
            Log.i("TAG", sb2.toString());
        }
    }

    public Cursor GetTransaction() {
        return getReadableDatabase().rawQuery("SELECT tid,debitamt,creditamt,balanceamt,paydate,note FROM tbltransaction", null);
    }


    public void backup(String outFileName) {

        //database path
        final String inFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);

            File file = new File(Environment.getExternalStorageDirectory()+"/AccountManager");

            File file3 = new File(file, outFileName);

            FileInputStream fis = new FileInputStream(dbFile);


            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {

        final String outFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File file = new File(Environment.getExternalStorageDirectory()+"/AccountManager");

            File file3 = new File(file, inFileName);

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(mContext, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
