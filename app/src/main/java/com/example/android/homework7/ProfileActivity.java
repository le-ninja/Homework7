package com.example.android.homework7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout fragSwap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.profile_users_btn).setOnClickListener(this);
        findViewById(R.id.profile_messages_btn).setOnClickListener(this);
        fragSwap = (FrameLayout) findViewById(R.id.profile_frame_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_users_btn:
                break;
            case R.id.profile_messages_btn:
                break;
            default:
                break;
        }
    }

    private void usersFragmentLoad() {

    }

    private void messagesFragmentLoad() {

    }
}
