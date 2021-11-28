package com.ahmed.acapysales;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ahmed.acapysales.Calender.BasicActivity;
import com.ahmed.acapysales.Calender.Todo;
import com.ahmed.acapysales.calls.CallsFragment;
import com.ahmed.acapysales.clients.clientsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ahmed.acapysales.LoginActivity.LOGIN_PREFS;
import static com.ahmed.acapysales.LoginActivity.MY_APP_PRFS;
import static com.ahmed.acapysales.LoginActivity.USER_PREFS;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    private BottomNavigationView bottomNav;

    int BottomMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        FrameLayout framaeLayouat = (FrameLayout) findViewById(R.id.fragment_container);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) framaeLayouat.getLayoutParams();
        BottomMargin = params.bottomMargin;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(prefs.getString(USER_PREFS, "USERNAME").toUpperCase());
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.calls:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CallsFragment()).commit();
                        break;
                    case R.id.clients:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new clientsFragment()).commit();
                        break;

                }
                return true;
            }
        });
        bottomNav.setVisibility(View.VISIBLE);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if (bottomNav.getSelectedItemId() != R.id.calls) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CallsFragment()).commit();
                    bottomNav.setSelectedItemId(R.id.calls);
                } else {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");
        mView.setCancelable(false);
        mView.setText("get Data ....");
        mView.setBackgroundColor(R.color.catColor);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CallsFragment()).commit();
                mView.dismiss();
            }
        }, 100);

    }

    CatLoadingView mView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.todolist:
                Toast.makeText(MainActivity.this,"soon!",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, BasicActivity.class);
//                startActivity(intent);
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE).edit();
                editor.putString(LOGIN_PREFS, "false");
                editor.apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }
}