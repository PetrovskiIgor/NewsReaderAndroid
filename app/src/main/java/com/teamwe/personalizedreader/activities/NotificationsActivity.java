package com.teamwe.personalizedreader.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamwe.personalizedreader.mynews.R;

public class NotificationsActivity extends AppCompatActivity {
    RelativeLayout layoutNext;
    Toolbar toolbar;
    SwitchCompat switchButton;
    TextView txtSwitch;

    // da se dodade SharedPreferences i da se dodade povik do server za menuvanje na status
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        colorAboveTheToolbar();
        toolbar = (Toolbar) findViewById(R.id.toolbarNotifications);
        toolbar.setTitle(getResources().getString(R.string.title_notifications_activity));
        setSupportActionBar(toolbar);

        configureLayoutNext();
        configureSwitchButton();

    }



    private void configureLayoutNext() {

        layoutNext = (RelativeLayout) toolbar.findViewById(R.id.layoutNext);
        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    private void configureSwitchButton() {
        txtSwitch = (TextView)findViewById(R.id.txtNotif);
        switchButton = (SwitchCompat)findViewById(R.id.idSwitch);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtSwitch.setText(getResources().getString(R.string.notifications_on));
                } else {
                    txtSwitch.setText(getResources().getString(R.string.notifications_off));
                }
            }
        });
    }

    private void exit() {
        this.finish();
    }

    private void colorAboveTheToolbar() {

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryColorDark));
        }

    }

}
