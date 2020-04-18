package com.account.manager.app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPersonActivity extends AppCompatActivity {
    public EditText address;
    public SimpleDateFormat dateFormatter;
    public EditText edtdate;
    public DatePickerDialog fromDatePickerDialog;
    public EditText name;
    public EditText number;
    String GetDate;
    String GetName;
    String GetNumber = "";
    String Getaddress = "";
    Button addrecords;
    DatabaseHelper databaseHelper;
    String languageToLoad;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_addperson);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        addrecords = (Button) findViewById(R.id.addrecord);
        edtdate = (EditText) findViewById(R.id.fromdate);
        name = (EditText) findViewById(R.id.edtname);
        number = (EditText) findViewById(R.id.edtphone);
        address = (EditText) findViewById(R.id.address);
        edtdate.setInputType(0);
        setDateTimeField();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DATE, instance.get(Calendar.DATE));
        this.edtdate.setText(simpleDateFormat.format(instance.getTime()));
        this.addrecords.setOnClickListener(new OnClickListener() {
            @SuppressLint({"WrongConstant"})
            public void onClick(View view) {
                if (name.getText().toString().length() == 0) {
                    name.setError("Enter Name");
                    return;
                }
                GetDate = edtdate.getText().toString();
                GetName = name.getText().toString();
                GetNumber = number.getText().toString();
                Getaddress = address.getText().toString();
                databaseHelper.insertRecords(GetName, GetNumber, GetDate, Getaddress);
                Toast.makeText(getBaseContext(), "Add Successfully...", 0).show();
                name.setText("");
                number.setText("");
                address.setText("");
            }
        });
    }

    private void setDateTimeField() {
        this.edtdate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        Calendar instance = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Calendar instance = Calendar.getInstance();
                instance.set(i, i2, i3);
                edtdate.setText(dateFormatter.format(instance.getTime()));
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
        this.fromDatePickerDialog = datePickerDialog;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
