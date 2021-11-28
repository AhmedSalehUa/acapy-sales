package com.ahmed.acapysales.calls;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmed.acapysales.LoginActivity;
import com.ahmed.acapysales.MainActivity;
import com.ahmed.acapysales.Networking.JasonReponser;
import com.ahmed.acapysales.R;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.acapysales.LoginActivity.IP;
import static com.ahmed.acapysales.LoginActivity.MY_APP_PRFS;
import static com.ahmed.acapysales.LoginActivity.USER_ID_PREFS;
import static com.ahmed.acapysales.LoginActivity.USER_PREFS;

public class CallsFragment extends Fragment {
    private ListView mListView;
    private CallsAdapter mAdapter;
    private Context mContext = getContext();

    public CallsFragment() {
        super();
    }

    SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.calls_activity, container, false);
        prefs = this.getActivity().getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE);
        mListView = (ListView) rootview.findViewById(R.id.list_of_calls);
        FloatingActionButton addClient = (FloatingActionButton) rootview.findViewById(R.id.addCall);
        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddCall.class));
            }
        });
        List<calls> list = new ArrayList<>();
        mAdapter = new CallsAdapter(getContext(), list);
        mListView.setAdapter(mAdapter);
        mListView.setAdapter(mAdapter);
        ImageView empyt = (ImageView) rootview.findViewById(R.id.empty);
        mListView.setEmptyView(empyt);
        mAdapter.setMode(Attributes.Mode.Single);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);

            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }

        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String api = getApi() + "userId=" + prefs.getString(USER_ID_PREFS, "0");
        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        Log.e("api",api);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    jasonObj = update.getUserId();

                    if (jasonObj.equals("0")) {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    } else {
                        fetchCallsFromJason(jasonObj);
                        pDialog.setTitleText("Success!")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.dismiss();
                    }
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);

        return rootview;
    }

    private String getApi() {
        return "https://" + prefs.getString(IP, "192.168.1.90") + "/salesAcapyApi/getCalls.php?";
    }

    String jasonObj;

    private List<calls> fetchCallsFromJason(String json) {
        List<calls> list = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(jasonObj);

            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonObject1 = jsonObject.getJSONObject(sa.get(i).toString());
                list.add(new calls(Integer.parseInt(jsonObject1.getString("id")),
                        Integer.parseInt(prefs.getString(USER_ID_PREFS, "0")), i,
                        jsonObject1.getString("organization"), jsonObject1.getString("description"), jsonObject1.getString("date"), jsonObject1.getString("time")));
            }
            mAdapter.addAll(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
