package com.example.android.homework7;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;
    Firebase mRefUsers, mRefMessages, mRef, mRefUsersInfo, mRefMessagesUser;

    private TextView userName, userGender;
    private ImageView userAvatar;

    DatabaseReference rootRef;

    String passedUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = (TextView) findViewById(R.id.profile_name_tv);
        userGender = (TextView) findViewById(R.id.profile_gender_tv);
        userAvatar = (ImageView) findViewById(R.id.profile_avatar_iv);

        mAuth = FirebaseAuth.getInstance();
        mRef = new Firebase("https://homework7-425f5.firebaseio.com");
        mRefUsers = mRef.child("Users");
        mRefMessages = mRef.child("Messages");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mRefUsersInfo = mRefUsers.child(user.getUid());
                    mRefMessagesUser = mRefMessages.child(user.getUid());
                    mRefUsersInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User temp = dataSnapshot.getValue(User.class);
                            userName.setText(temp.getName());
                            if (temp.getGender().equals("na")) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                                LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                                final View v = inflater.inflate(R.layout.profile_gender_alert_layout, null);
                                dialog.setView(v);

                                final RadioGroup rg = (RadioGroup) v.findViewById(R.id.profile_alert_rg);

                                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (rg.getCheckedRadioButtonId() == -1) {
                                            return;
                                        } else {
                                            String gender = ((RadioButton) v.findViewById(rg.getCheckedRadioButtonId()))
                                                    .getText().toString();
                                            mRefUsersInfo.child("gender").setValue(gender);
                                            userGender.setText(gender);
                                        }
                                    }
                                });
                                dialog.show();
                            } else {
                                userGender.setText(temp.getGender());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if (user.getPhotoUrl() != null) {
                        Picasso.with(ProfileActivity.this).load(user.getPhotoUrl()).into(userAvatar);
                    }
                }
            }
        };

        mRefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User u = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ViewPager vp = (ViewPager) findViewById(R.id.profile_viewpager);
        PagerAdapter adapter =
                new PagerAdapter(getSupportFragmentManager(), ProfileActivity.this);
        vp.setAdapter(adapter);

        TabLayout tl = (TabLayout) findViewById(R.id.profile_tablayout);
        tl.setupWithViewPager(vp);

        for (int i = 0; i < tl.getTabCount(); i++) {
            TabLayout.Tab tab = tl.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {
        String [] tabs = new String[] {getResources().getString(R.string.users_btn),
            getResources().getString(R.string.messages_btn)};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new UsersFragment();
                case 1:
                    return new MessagesFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // generate title based on item position
            return tabs[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(ProfileActivity.this).inflate(
                    R.layout.profile_custom_tab_layout, null);
            TextView tabTitle = (TextView) tab.findViewById(R.id.custom_text);
            tabTitle.setText(tabs[position]);
            return tab;
        }
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


