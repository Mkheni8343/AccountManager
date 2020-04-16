package com.account.manager.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    Button btnok;
    MainApplication mainApplication;
    EditText edt_qa;
    EditText edtpin;
    EditText edtpin1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_register);
        edtpin = (EditText) findViewById(R.id.pin);
        edtpin1 = (EditText) findViewById(R.id.pin1);
        edt_qa = (EditText) findViewById(R.id.answer);
        btnok = (Button) findViewById(R.id.btn_ok);
        mainApplication = (MainApplication) getApplicationContext();
        btnok.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (edtpin.getText().toString().isEmpty()) {
                    edtpin.setError("Enter PIN");
                } else if (edtpin1.getText().toString().isEmpty()) {
                    edtpin1.setError("Confirm PIN");
                } else if (edt_qa.getText().toString().isEmpty()) {
                    edt_qa.setError("Enter Text");
                } else if (edtpin.getText().toString().equals(edtpin1.getText().toString())) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    mainApplication.ISLogin(getApplicationContext(), "true", edtpin1.getText().toString(), edt_qa.getText().toString());
                    finish();
                } else {
                    edtpin1.setError("PIN not match!");
                }
            }
        });
    }
}
