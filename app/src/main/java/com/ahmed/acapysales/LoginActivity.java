package com.ahmed.acapysales;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.acapysales.Networking.JasonReponser;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_APP_PRFS = "MyPrefsFile";
    public static final String LOGIN_PREFS = "login";
    public static final String USER_PREFS = "user";
    public static final String USER_ID_PREFS = "user_id"; public static final String IP = "ip";
    EditText username;
    EditText password;
    String userId = "0";
    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
          prefs = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE);
        String statue = prefs.getString(LOGIN_PREFS, "false");

        Dexter.withActivity(this).withPermissions(Arrays.asList(Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE)).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("permission ok", "ok");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Snackbar.make(findViewById(android.R.id.content), "Permissions Denied", Snackbar.LENGTH_SHORT).show();
            }
        }).check();
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(username.getText().toString().equals("IP")){
                    Log.e("ip",password.getText().toString());
                    SharedPreferences.Editor editor = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE).edit();
                    editor.putString(IP, password.getText().toString()); editor.apply();
                }
            }
        });
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String api = getApi() + "username=" + username.getText().toString() +
                        "&password=" + password.getText().toString();
                final JasonReponser update = new JasonReponser();
                update.setFinish(false);
                update.execute(api);
                final Handler handler = new Handler();
                Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        if (update.isFinish()) {
                            userId = update.getUserId();
                            if (Integer.parseInt(userId)>0) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE).edit();
                                editor.putString(LOGIN_PREFS, "true");
                                editor.putString(USER_PREFS, username.getText().toString());
                                Log.e("USER_ID_PREFS", userId);
                                editor.putString(USER_ID_PREFS, userId);
                                editor.apply(); Log.e("id",userId);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            handler.postDelayed(this, 100);
                        }
                    }
                };
                handler.post(runnableCode);


            }
        });
        if (statue.equals("false")) {
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private String getApi() {
        return "https://"+prefs.getString(IP, "192.168.1.90")+"/salesAcapyApi/authentication.php?";
    }
}
