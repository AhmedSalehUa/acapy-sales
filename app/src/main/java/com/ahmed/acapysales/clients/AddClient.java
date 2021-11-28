package com.ahmed.acapysales.clients;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.acapysales.Networking.JasonReponser;
import com.ahmed.acapysales.R;
import com.ahmed.acapysales.calls.AddCall;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ahmed.acapysales.LoginActivity.IP;
import static com.ahmed.acapysales.LoginActivity.MY_APP_PRFS;
import static com.ahmed.acapysales.LoginActivity.USER_ID_PREFS;
import static com.ahmed.acapysales.LoginActivity.USER_PREFS;

public class AddClient extends AppCompatActivity {
    EditText organization, client, relation, location, email, tele1, tele2;
    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_activity);
        prefs = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE);
        organization = (EditText) findViewById(R.id.addclient_orgName);
        client = (EditText) findViewById(R.id.addclient_clientName);
        relation = (EditText) findViewById(R.id.addclient_relation);
        location = (EditText) findViewById(R.id.addclient_location);
        email = (EditText) findViewById(R.id.addclient_mail);
        tele1 = (EditText) findViewById(R.id.addclient_tele1);
        tele2 = (EditText) findViewById(R.id.addclient_tele2);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done, menu);
        return true;
    }

    String jasonObj;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.submit:
                final SweetAlertDialog pDialog = new SweetAlertDialog(AddClient.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                String api = getApi() + "organization=" + organization.getText().toString() +
                        "&location=" + location.getText().toString() +
                        "&name=" + client.getText().toString() +
                        "&relation=" + relation.getText().toString() +
                        "&email=" + email.getText().toString() +
                        "&tele1=" + tele1.getText().toString() +
                        "&tele2=" + tele2.getText().toString() +
                        "&userId=" + prefs.getString(USER_ID_PREFS, "USERNAME");
                final JasonReponser update = new JasonReponser();
                update.setFinish(false);
                update.execute(api);
                final Handler handler = new Handler();
                Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        if (update.isFinish()) {
                            jasonObj = update.getUserId();
Log.e("pp",jasonObj);
                            if (jasonObj.equals("0")) {
                                Toast.makeText(AddClient.this, "failed", Toast.LENGTH_SHORT).show();
                                pDialog.setTitleText("Failed!")
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            } else {
                                pDialog.setTitleText("Success!")
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                            }

                        } else {
                            handler.postDelayed(this, 100);
                        }
                    }
                };
                handler.post(runnableCode);
                break;
        }
        return true;
    }

    private String getApi() {
        return "https://" + prefs.getString(IP, "192.168.1.90") + "/salesAcapyApi/addClient.php?";
    }
}
