package com.example.android.homework7;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class ProfileActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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

    @Override
    public void onResume() {
        super.onResume();
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
                    return new UsersFragment();
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
}


