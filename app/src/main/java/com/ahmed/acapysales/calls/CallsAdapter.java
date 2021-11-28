package com.ahmed.acapysales.calls;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.acapysales.Networking.JasonReponser;
import com.ahmed.acapysales.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.acapysales.LoginActivity.IP;
import static com.ahmed.acapysales.LoginActivity.MY_APP_PRFS;

public class CallsAdapter extends BaseSwipeAdapter {

    private Context mContext;
    List<calls> list;
    CatLoadingView mView;
    LayoutInflater inflter;
    SharedPreferences prefs;
    public CallsAdapter(Context mContext, List<calls> list) {
        prefs = mContext.getSharedPreferences(MY_APP_PRFS, MODE_PRIVATE);
        this.mContext = mContext;
        this.list = list;
        inflter = (LayoutInflater.from(mContext));
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.list_items, null);
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.clock));
            }
        });

        v.findViewById(R.id.delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(position);
            }
        });
        v.findViewById(R.id.delay_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delay(position);
            }
        });
        return v;
    }

    private void delay(int position) {

    }

    int position;
    String userId;
    private void delete(int p) {
        position = p;
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("هل انت متاكد؟")
                .setContentText("لن تستطيع استعادة العميل مرة اخري!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        String api = getApi() + "id=" + list.get(position).getId();
                        final JasonReponser update = new JasonReponser();
                        update.setFinish(false);
                        update.execute(api);
                        final Handler handler = new Handler();
                        Runnable runnableCode = new Runnable() {
                            @Override
                            public void run() {
                                if (update.isFinish()) {
                                    userId = update.getUserId();
                                    if (Integer.parseInt(userId) > 0) {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                        sDialog.setTitleText("Deleted!")
                                                .setContentText("تم الحذف بنجاح")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    } else {

                                        sDialog.setTitleText("Failed!")
                                                .setContentText("لم يتم الحذف")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    }
                                } else {
                                    handler.postDelayed(this, 100);
                                }
                            }
                        };
                        handler.post(runnableCode);

                    }
                })
                .show();
    }

    private String getApi() {
        return "https://" + prefs.getString(IP, "192.168.1.90") + "/salesAcapyApi/delCall.php?";
    }
    @Override
    public void fillValues(int position, View convertView) {
        calls call = list.get(position);
        TextView ClientName = (TextView) convertView.findViewById(R.id.location_name);
        ClientName.setText(call.getClient_name());
        TextView Details = (TextView) convertView.findViewById(R.id.call_description);
        Details.setText(call.getDetails());
        TextView Date = (TextView) convertView.findViewById(R.id.date_of_call);
        Date.setText(call.getDate());
        TextView Time = (TextView) convertView.findViewById(R.id.time_of_call);
        Time.setText(call.getTime());
//        Button button = (Button) convertView.findViewById(R.id.delete_item);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
//                list.remove(position);
//                notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<calls> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
