package com.account.manager.app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GetDateWiseReportActivity extends AppCompatActivity {
    public SimpleDateFormat dateFormatter;
    public EditText fromDateEtxt;
    public DatePickerDialog fromDatePickerDialog;
    ArrayList<String> CreditAmount = new ArrayList<>();
    ArrayList<String> DebitAmount = new ArrayList<>();
    Double FinalBalance = Double.valueOf(0.0d);
    Double FinalCredit = Double.valueOf(0.0d);
    Double FinalDebit = Double.valueOf(0.0d);
    String GetFromDate = "";
    ArrayList<String> PartyBalance = new ArrayList<>();
    ArrayList<String> PartyNote = new ArrayList<>();
    ArrayList<String> Paydate = new ArrayList<>();
    TextView TCr;
    TextView TDbt;
    ArrayList<String> UserID = new ArrayList<>();
    DatabaseHelper databaseHelper;
    Button show;
    
    TableLayout tableLayout;
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_datewise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        this.dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        this.fromDateEtxt = (EditText) findViewById(R.id.startdt);
        this.fromDateEtxt.setInputType(0);
        this.fromDateEtxt.requestFocus();
        this.TCr = (TextView) findViewById(R.id.txt_cr);
        this.TDbt = (TextView) findViewById(R.id.txt_dbt);
        tableLayout = (TableLayout) findViewById(R.id.tbl);
        this.show = (Button) findViewById(R.id.btn_show);
        setDateTimeField();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DATE, instance.get(Calendar.DATE));
        this.fromDateEtxt.setText(this.dateFormatter.format(instance.getTime()));
        this.GetFromDate = this.fromDateEtxt.getText().toString();
        this.show.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (fromDateEtxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter date", Toast.LENGTH_SHORT).show();
                    return;
                }
                GetFromDate = fromDateEtxt.getText().toString();
                FinalCredit = Double.valueOf(0.0d);
                FinalDebit = Double.valueOf(0.0d);
                FinalBalance = Double.valueOf(0.0d);
                GetPartyTransactionRecords();
            }
        });
    }

    @SuppressLint({"WrongConstant"})
    public void GetPartyTransactionRecords() {
        this.UserID.clear();
        this.DebitAmount.clear();
        this.CreditAmount.clear();
        this.PartyBalance.clear();
        this.Paydate.clear();
        this.PartyNote.clear();
        Cursor GetAllTransaction = databaseHelper.GetAllTransaction(this.GetFromDate);
        if (GetAllTransaction.moveToFirst()) {
            do {
                DisplayContact(GetAllTransaction);
            } while (GetAllTransaction.moveToNext());
            return;
        }
        Toast.makeText(getBaseContext(), "No Transaction Found!", 0).show();
        tableLayout.setVisibility(8);
        this.TCr.setVisibility(8);
        this.TDbt.setVisibility(8);
        databaseHelper.close();
    }

    @SuppressLint({"WrongConstant"})
    private void DisplayContact(Cursor cursor) {
        Cursor cursor2 = cursor;
        this.UserID.add(cursor2.getString(0));
        this.DebitAmount.add(cursor2.getString(1));
        this.CreditAmount.add(cursor2.getString(2));
        this.PartyBalance.add(cursor2.getString(3));
        this.Paydate.add(cursor2.getString(4));
        this.PartyNote.add(cursor2.getString(5));
        if (tableLayout != null) {
            tableLayout.setVisibility(0);
            this.TCr.setVisibility(0);
            this.TDbt.setVisibility(0);
            tableLayout.removeAllViews();
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            TextView textView = new TextView(getApplicationContext());
            textView.setText("Date");
            textView.setGravity(17);
            textView.setTypeface(null, 1);
            textView.setTextSize(16.0f);
            textView.setTextColor(getResources().getColor(R.color.White));
            textView.setPadding(10, 15, 10, 15);
            tableRow.addView(textView, new LayoutParams(0, -1, 1.0f));
            TextView textView2 = new TextView(getApplicationContext());
            textView2.setText("Cr.Amt");
            textView2.setGravity(17);
            textView2.setTextSize(16.0f);
            textView2.setTextColor(getResources().getColor(R.color.White));
            textView2.setPadding(10, 15, 10, 15);
            textView2.setTypeface(null, 1);
            tableRow.addView(textView2, new LayoutParams(0, -1, 1.0f));
            TextView textView3 = new TextView(getApplicationContext());
            textView3.setText("Dbt.Amt");
            textView3.setGravity(17);
            textView3.setTextSize(16.0f);
            textView3.setTextColor(getResources().getColor(R.color.White));
            textView3.setPadding(10, 15, 10, 15);
            textView3.setTypeface(null, 1);
            tableRow.addView(textView3, new LayoutParams(0, -1, 1.0f));
            TextView textView4 = new TextView(getApplicationContext());
            textView4.setText("Note");
            textView4.setGravity(17);
            textView4.setTextSize(16.0f);
            textView4.setTextColor(getResources().getColor(R.color.White));
            textView4.setPadding(10, 15, 10, 15);
            textView4.setTypeface(null, 1);
            tableRow.addView(textView4, new LayoutParams(0, -1, 1.0f));
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(-2, -2));
            for (int i = 0; i < this.UserID.size(); i++) {
                TableRow tableRow2 = new TableRow(getApplicationContext());
                tableRow2.setBackgroundResource(R.drawable.nlinearlayout_bg1);
                TextView textView5 = new TextView(getApplicationContext());
                textView5.setText((CharSequence) this.Paydate.get(i));
                textView5.setGravity(17);
                textView5.setTextColor(getResources().getColor(R.color.Black));
                textView5.setTextSize(14.0f);
                textView5.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView5, new LayoutParams(0, -1, 1.0f));
                TextView textView6 = new TextView(getApplicationContext());
                textView6.setText((CharSequence) this.CreditAmount.get(i));
                textView6.setGravity(17);
                textView6.setTextSize(14.0f);
                textView6.setTextColor(getResources().getColor(R.color.Green));
                textView6.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView6, new LayoutParams(0, -1, 1.0f));
                TextView textView7 = new TextView(getApplicationContext());
                textView7.setText((CharSequence) this.DebitAmount.get(i));
                textView7.setGravity(17);
                textView7.setTextColor(getResources().getColor(R.color.Red));
                textView7.setTextSize(14.0f);
                textView7.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView7, new LayoutParams(0, -1, 1.0f));
                TextView textView8 = new TextView(getApplicationContext());
                textView8.setText((CharSequence) this.PartyNote.get(i));
                textView8.setGravity(17);
                textView8.setTextColor(getResources().getColor(R.color.Black));
                textView8.setTextSize(13.0f);
                textView8.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView8, new LayoutParams(0, -1, 1.0f));
                tableLayout.addView(tableRow2, new TableLayout.LayoutParams(-2, -2));
            }
            this.FinalDebit = Double.valueOf(this.FinalDebit.doubleValue() + Double.parseDouble(cursor2.getString(1)));
            this.FinalCredit = Double.valueOf(this.FinalCredit.doubleValue() + Double.parseDouble(cursor2.getString(2)));
            this.FinalBalance = Double.valueOf(this.FinalCredit.doubleValue() - this.FinalDebit.doubleValue());
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("RESULT_BAL_Amt= ");
            sb.append(this.FinalCredit);
            sb.append("  ");
            sb.append(this.FinalDebit);
            sb.append(" = ");
            sb.append(this.FinalBalance);
            printStream.println(sb.toString());
            TextView textView9 = this.TCr;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Total Credit Amount : ");
            sb2.append(this.FinalCredit);
            sb2.append(" /-");
            textView9.setText(sb2.toString());
            TextView textView10 = this.TDbt;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Total Debit Amount : ");
            sb3.append(this.FinalDebit);
            sb3.append(" /-");
            textView10.setText(sb3.toString());
        }
    }

    private void setDateTimeField() {
        this.fromDateEtxt.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        Calendar instance = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Calendar instance = Calendar.getInstance();
                instance.set(i, i2, i3);
                fromDateEtxt.setText(dateFormatter.format(instance.getTime()));
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
        this.fromDatePickerDialog = datePickerDialog;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
