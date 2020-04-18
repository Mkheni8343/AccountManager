package com.account.manager.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {
    String First_Login;
    MainApplication mainApplication = null;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);
        mainApplication = (MainApplication) getApplicationContext();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                changeScreen();
            }
        }, 2000);
    }

    public void changeScreen() {
        this.First_Login = mainApplication.getfirstlogin(getApplicationContext());
        if (this.First_Login.equals("true")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return;
        }
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }
}
