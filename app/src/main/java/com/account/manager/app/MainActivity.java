package com.account.manager.app;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int MULTIPLE_PERMISSIONS = 1;
    MainApplication mainApplication;
    TextView add_person;
    TextView trancation;
    TextView report;
    TextView generate_report;
    Dialog dialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        mainApplication = (MainApplication) getApplicationContext();
        add_person = findViewById(R.id.add_party);
        trancation = findViewById(R.id.trancation);
        report = findViewById(R.id.report);
        generate_report = findViewById(R.id.generate_report);
        if (VERSION.SDK_INT >= 23) {
            checkPermissions();
        }
        add_person.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open add person activity
                startActivity(new Intent(getApplicationContext(), AddPersonActivity.class));
            }
        });

        trancation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open add trancation activity
                startActivity(new Intent(getApplicationContext(), AddViewDeleteTransactionActivity.class));
            }
        });

        report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open show transation date wise activity
                startActivity(new Intent(getApplicationContext(), GetDateWiseReportActivity.class));
            }
        });

        generate_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open show all generated reports activity
                startActivity(new Intent(getApplicationContext(), PdfGenrateActivity.class));
            }
        });
    }

    private boolean checkPermissions() {
        String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        ArrayList arrayList = new ArrayList();
        for (String str : permissions) {
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[arrayList.size()]), MULTIPLE_PERMISSIONS);
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "Please allow all Permission.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ExitDialoge();
    }

    private void ExitDialoge() {
        Builder builder = new Builder(this);
        builder.setTitle("Exit").setMessage("Do You Want To Exit ?").setCancelable(true).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        this.dialog = builder.show();
    }

}
