package com.account.manager.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddViewDeleteTransactionActivity extends AppCompatActivity {
    private static final String TAG = "AddViewDeleteTrans";
    ArrayList<String> CreditAmount = new ArrayList<>();
    ArrayList<String> Date_pay = new ArrayList<>();
    ArrayList<String> DebitAmount = new ArrayList<>();
    Double FinalBalance = Double.valueOf(0.0d);
    Double FinalCredit = Double.valueOf(0.0d);
    Double FinalDebit = Double.valueOf(0.0d);
    ArrayList<String> PartyBalance = new ArrayList<>();
    ArrayList<String> PartyNote = new ArrayList<>();
    ArrayList<String> Paydate = new ArrayList<>();
    ArrayList<String> Record_date = new ArrayList<>();
    Spinner SpnParty;
    ArrayList<String> SpnRecord_date = new ArrayList<>();
    ArrayList<String> SpnRecord_no = new ArrayList<>();
    ArrayList<String> SpnUser_Address = new ArrayList<>();
    ArrayList<String> SpnUser_Phone = new ArrayList<>();
    ArrayList<String> SpnUser_name = new ArrayList<>();
    TextView TotalCr;
    TextView TotalDbt;
    ArrayList<String> UserID = new ArrayList<>();
    ArrayList<String> User_Address = new ArrayList<>();
    ArrayList<String> User_Id = new ArrayList<>();
    ArrayList<String> User_Phone = new ArrayList<>();
    ArrayList<String> User_name = new ArrayList<>();
    ListView complainList;
    Cursor cursor;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        SpnParty = (Spinner) findViewById(R.id.spn_partylist);
        TotalCr = (TextView) findViewById(R.id.txt_credit);
        TotalDbt = (TextView) findViewById(R.id.txt_debit);
        GetTransaction();
        complainList = (ListView) findViewById(R.id.recordList);
        SpnRecord_no.add(0, "0");
        SpnUser_name.add(0, "- Search Party -");
        SpnUser_Phone.add(0, "0");
        SpnRecord_date.add(0, "0");
        SpnUser_Address.add(0, "0");
        Getrecords();
        SpnParty.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, this.SpnUser_name));
        SpnParty.setOnItemSelectedListener(new ItemsOnItemSelectedListener());
    }

    @SuppressLint({"WrongConstant"})
    private void Getrecords() {
        this.cursor = databaseHelper.GetAllparty();
        if (this.cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Party Found...", 1).show();
        }
        if (this.cursor.moveToFirst()) {
            do {
                DisplayContact(this.cursor);
            } while (this.cursor.moveToNext());
        }
        databaseHelper.close();
    }

    private void DisplayContact(Cursor cursor2) {
        User_Id.add(cursor2.getString(0));
        User_name.add(cursor2.getString(1));
        User_Phone.add(cursor2.getString(2));
        Record_date.add(cursor2.getString(3));
        User_Address.add(cursor2.getString(4));
        SpnRecord_no.add(cursor2.getString(0));
        SpnUser_name.add(cursor2.getString(1));
        SpnUser_Phone.add(cursor2.getString(2));
        SpnRecord_date.add(cursor2.getString(3));
        SpnUser_Address.add(cursor2.getString(4));
        complainList.setAdapter(new getComplaintList());
        complainList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(getApplicationContext(), CreditDebitOfPersonActivity.class);
                intent.putExtra("P_ID", (String) User_Id.get(i));
                intent.putExtra("P_NAME", (String) User_name.get(i));
                intent.putExtra("P_NUMBER", (String) User_Phone.get(i));
                intent.putExtra("P_ADDRESS", (String) User_Address.get(i));
                intent.putExtra("R_Date", (String) Record_date.get(i));
                startActivity(intent);
                finish();
            }
        });
    }

    public void CleatItems() {
        this.User_Id.clear();
        this.User_name.clear();
        this.User_Phone.clear();
        this.User_Address.clear();
        this.Date_pay.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void GetTransaction() {
        this.UserID.clear();
        this.DebitAmount.clear();
        this.CreditAmount.clear();
        this.PartyBalance.clear();
        this.Paydate.clear();
        this.PartyNote.clear();
        Cursor GetTransaction = databaseHelper.GetTransaction();
        if (GetTransaction.moveToFirst()) {
            do {
                DisplayRecord(GetTransaction);
            } while (GetTransaction.moveToNext());
            return;
        }
        databaseHelper.close();
    }

    private void DisplayRecord(Cursor cursor2) {
        UserID.add(cursor2.getString(0));
        DebitAmount.add(cursor2.getString(1));
        CreditAmount.add(cursor2.getString(2));
        PartyBalance.add(cursor2.getString(3));
        Paydate.add(cursor2.getString(4));
        PartyNote.add(cursor2.getString(5));
        FinalDebit = Double.valueOf(this.FinalDebit.doubleValue() + Double.parseDouble(cursor2.getString(1)));
        FinalCredit = Double.valueOf(this.FinalCredit.doubleValue() + Double.parseDouble(cursor2.getString(2)));
        Log.e(TAG, "DisplayRecord: "+ "RESULT_BAL_Amt= "+FinalCredit+"  "+ FinalDebit );
        TotalCr.setText("Overall Credit = "+FinalCredit+" /-");
        TotalDbt.setText("Overall Debit = "+FinalDebit+" /-");
    }

    public class ItemsOnItemSelectedListener implements OnItemSelectedListener {
        public ItemsOnItemSelectedListener() {
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (i != 0) {
                try {
                    Intent intent = new Intent(getApplicationContext(), CreditDebitOfPersonActivity.class);
                    intent.putExtra("P_ID", (String) SpnRecord_no.get(i));
                    intent.putExtra("P_NAME", (String) SpnUser_name.get(i));
                    intent.putExtra("P_NUMBER", (String) SpnUser_Phone.get(i));
                    intent.putExtra("P_ADDRESS", (String) SpnUser_Address.get(i));
                    intent.putExtra("R_Date", (String) SpnRecord_date.get(i));
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class getComplaintList extends BaseAdapter {
        private LayoutInflater layoutInflater;

        @SuppressLint({"WrongConstant"})
        public getComplaintList() {
            this.layoutInflater = (LayoutInflater) getApplicationContext().getSystemService("layout_inflater");
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public int getCount() {
            return User_Id.size();
        }

        public Object getItem(int i) {
            return Integer.valueOf(i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.layoutInflater.inflate(R.layout.event_list_row, null);
            }
            TextView textView = (TextView) view.findViewById(R.id.names);
            TextView textView2 = (TextView) view.findViewById(R.id.dates);
            String str = (String) User_name.get(i);
            if (str.length() > 25) {
                StringBuilder sb = new StringBuilder();
                sb.append(str.substring(0, 25));
                sb.append("...");
                str = sb.toString();
            }
            textView.setText(str);
            textView2.setText((String) Record_date.get(i));
            return view;
        }
    }
}
