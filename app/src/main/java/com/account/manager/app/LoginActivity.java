package com.account.manager.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.PrintStream;

public class LoginActivity extends AppCompatActivity {
    String GetAnswer;
    String GetLang = "English";
    String Get_PIN;
    TextView Txt_Forgot;
    Button btndone;
    MainApplication mainApplication;
    EditText edtpin;
    EditText edtpin1;
    TextView pinchange;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        this.edtpin = (EditText) findViewById(R.id.edt_pin);
        this.btndone = (Button) findViewById(R.id.btn_done);
        this.pinchange = (TextView) findViewById(R.id.pinchange);
        this.Txt_Forgot = (TextView) findViewById(R.id.forgot);
        this.mainApplication = (MainApplication) getApplicationContext();
        this.Get_PIN = this.mainApplication.getPin(getApplicationContext());
        this.GetAnswer = this.mainApplication.getforgot_ans(getApplicationContext());
        PrintStream printStream = System.out;
        printStream.println("PIN = "+Get_PIN);

        this.btndone.setOnClickListener(new OnClickListener() {
            @SuppressLint({"WrongConstant"})
            public void onClick(View view) {
                if (LoginActivity.this.edtpin.getText().toString().isEmpty()) {
                    LoginActivity.this.edtpin.setError("Enter PIN");
                } else if (LoginActivity.this.edtpin.getText().toString().equals(LoginActivity.this.Get_PIN)) {
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Wrong PIN !!", 0).show();
                }
            }
        });
        this.Txt_Forgot.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.forgotpass);
                dialog.setCancelable(true);
                final EditText editText = (EditText) dialog.findViewById(R.id.edt_ans);
                final TextView textView = (TextView) dialog.findViewById(R.id.txt_pin);
                ((Button) dialog.findViewById(R.id.btn_show)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (editText.getText().toString().isEmpty()) {
                            editText.setError("Not empty");
                        } else if (editText.getText().toString().equalsIgnoreCase(LoginActivity.this.GetAnswer)) {
                            textView.setText("Your PIN = "+Get_PIN);
                        } else {
                            editText.setError("Text not match!!");
                        }
                    }
                });
                dialog.show();
            }
        });
        this.pinchange.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.pinchange);
                dialog.setCancelable(true);
                final EditText editText = (EditText) dialog.findViewById(R.id.edtoldpin);
                final EditText editText2 = (EditText) dialog.findViewById(R.id.edtnewpin);
                ((Button) dialog.findViewById(R.id.done)).setOnClickListener(new OnClickListener() {
                    @SuppressLint({"WrongConstant"})
                    public void onClick(View view) {
                        if (editText.getText().toString().isEmpty()) {
                            editText.setError("Enter old PIN");
                        } else if (editText2.getText().toString().isEmpty()) {
                            editText2.setError("Enter New PIN");
                        } else if (editText.getText().toString().equals(LoginActivity.this.Get_PIN)) {
                            LoginActivity.this.mainApplication.ISLogin(LoginActivity.this.getApplicationContext(),"true", editText2.getText().toString(), LoginActivity.this.GetAnswer);
                            Toast.makeText(LoginActivity.this, "PIN Change Successfully.", Toast.LENGTH_LONG).show();
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Old PIN Is Not Correct.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}
