package com.ahmed.acapysales.calls;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.acapysales.Networking.JasonReponser;
import com.ahmed.acapysales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ahmed.acapysales.LoginActivity.IP;
import static com.ahmed.acapysales.LoginActivity.MY_APP_PRFS;
import static com.ahmed.acapysales.LoginActivity.USER_ID_PREFS;

public class AddCall extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText pickedDate, pickedTime;
    EditText disc, client;
    SharedPreferences prefs;
    String[] clients;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_call_activity);
        prefs = getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE);
        disc = findViewById(R.id.descrip);

        pickedDate = (EditText) findViewById(R.id.call_date);
        pickedTime = (EditText) findViewById(R.id.call_time);
        client = (EditText) findViewById(R.id.client);
        pickedDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        pickedTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        final ImageView datePicker = (ImageView) findViewById(R.id.add_date);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCall.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                pickedDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        final ImageView timePicker = (ImageView) findViewById(R.id.add_time);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCall.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                pickedTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        ImageView setClient = (ImageView) findViewById(R.id.add_client);
        setClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute("https://" + prefs.getString(IP, "192.168.1.90") + "/salesAcapyApi/listClient.php?userId=" + prefs.getString(USER_ID_PREFS, "USERNAME"));
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    jasonObj = update.getUserId();

                    if (jasonObj.equals("0")) {
                        Toast.makeText(AddCall.this, "failed", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            fetchClientsFromJason(jasonObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    handler.postDelayed(this, 100);
                }
            }

            private void fetchClientsFromJason(String jasonObj) throws JSONException {
                JSONObject jsonObject = new JSONObject(jasonObj);
                JSONArray sa = jsonObject.names();
                clients =new String[sa.length()];
                for (int i = 0; i < sa.length(); i++) {
                    clients[i] =  jsonObject.getJSONObject(sa.get(i).toString()).getString("organization") ;

                }
            }
        };
        handler.post(runnableCode);
    }

    boolean[] checkedColors;
    List<String> clientList;

    private void openDialog() {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Clients");

        b.setItems(clients, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                client.setText(clients[which]);

            }

        });

        b.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.done, menu);
        return true;
    }
    String jasonObj;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                final SweetAlertDialog pDialog = new SweetAlertDialog(AddCall.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                String api = getApi() + "client_id=" + client.getText().toString() +
                        "&date=" + pickedDate.getText().toString() +
                        "&time=" + pickedTime.getText().toString() +
                        "&description=" + disc.getText().toString() +
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

                            if (jasonObj.equals("0")) {
                                Toast.makeText(AddCall.this, "failed", Toast.LENGTH_SHORT).show();
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
        return "https://" + prefs.getString(IP, "192.168.1.90") + "/salesAcapyApi/addCall.php?";
    }
}
