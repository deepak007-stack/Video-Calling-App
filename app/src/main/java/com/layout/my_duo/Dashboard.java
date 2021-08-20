package com.layout.my_duo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class Dashboard extends AppCompatActivity {

    private EditText codebox;
    private Button joinBtn,shareBtn;
    URL serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        codebox = (EditText) findViewById(R.id.codebox);
        joinBtn = (Button) findViewById(R.id.join_btn);
        shareBtn = (Button) findViewById(R.id.share_btn);

        try {
            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverUrl)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }
         catch (MalformedURLException e) {
            e.printStackTrace();
        }

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(codebox.getText().toString())
                        .setWelcomePageEnabled(false)
                        .build();

                JitsiMeetActivity.launch(Dashboard.this,options);

            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String link = codebox.getText().toString();
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_SEND);
               intent.setType("text/plain");
               intent.putExtra(Intent.EXTRA_SUBJECT,"Lets going to video calling ...");
               intent.putExtra(Intent.EXTRA_TEXT , link);
               startActivity(intent);
            }
        });

    }
}