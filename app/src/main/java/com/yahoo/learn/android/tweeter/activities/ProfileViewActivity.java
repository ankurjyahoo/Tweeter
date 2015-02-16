package com.yahoo.learn.android.tweeter.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.fragments.UserHeaderFragment;
import com.yahoo.learn.android.tweeter.fragments.UserTimelineFragment;
import com.yahoo.learn.android.tweeter.models.TwitterUser;


public class ProfileViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        TwitterUser user = (TwitterUser) getIntent().getSerializableExtra("user");

        getSupportActionBar().setTitle("@" + user.getScreenName());

        if (savedInstanceState == null) {
            UserHeaderFragment headerFragment = UserHeaderFragment.newInstance(user);
            UserTimelineFragment timelineFragment = UserTimelineFragment.newInstance(user.getScreenName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.flContainer, timelineFragment);
            ft.replace(R.id.flUserHeader, headerFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onGoHome(MenuItem item) {
        finish();
    }
}
