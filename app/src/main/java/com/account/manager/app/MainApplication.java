package com.account.manager.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MainApplication extends Application {

    public void ISLogin(Context context, String str, String str2, String str3) {
        SharedPreferences.Editor edit = context.getSharedPreferences("F_Login", 0).edit();
        edit.putString("IS_Login", str);
        edit.putString("PIN", str2);
        edit.putString("FPASS", str3);
        edit.commit();
    }

    public String getfirstlogin(Context context) {
        return context.getSharedPreferences("F_Login", 0).getString("IS_Login", "");
    }

    public String getPin(Context context) {
        return context.getSharedPreferences("F_Login", 0).getString("PIN", "");
    }

    public String getforgot_ans(Context context) {
        return context.getSharedPreferences("F_Login", 0).getString("FPASS", "");
    }
}
