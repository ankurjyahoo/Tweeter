package com.yahoo.learn.android.tweeter.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.adapters.HomePagerAdapter;
import com.yahoo.learn.android.tweeter.application.TwitterApplication;
import com.yahoo.learn.android.tweeter.application.TwitterClient;
import com.yahoo.learn.android.tweeter.fragments.HomeTimelineFragment;
import com.yahoo.learn.android.tweeter.fragments.MentionsTimelineFragment;
import com.yahoo.learn.android.tweeter.fragments.UserTimelineFragment;
import com.yahoo.learn.android.tweeter.models.TwitterUser;

import org.apache.http.Header;
import org.json.JSONObject;

public class HomeActivity extends ActionBarActivity {

    public static final int             REQUEST_CODE_POST = 21;
    private TwitterClient               mClient;
    public static TwitterUser           mUser;
    private UserResponseHandler         mUserDetailsHandler;
    private ViewPager                   mViewPager;


    private HomeTimelineFragment        mHomeTimelineFragment;
    private MentionsTimelineFragment    mMentionsFragment;
//    private UserTimelineFragment mUserTimelineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mClient = TwitterApplication.getRestClient();

        mUserDetailsHandler = new UserResponseHandler();
        initializeUser();

        mHomeTimelineFragment = new HomeTimelineFragment();
        mMentionsFragment = new MentionsTimelineFragment();
//        mUserTimelineFragment = UserTimelineFragment.newInstance(mUser.getScreenName());

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(),new Fragment[] {mHomeTimelineFragment, mMentionsFragment, mUserTimelineFragment} ));
        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(),
                new Fragment[] {mHomeTimelineFragment, mMentionsFragment},
                new String[] {"Timeline", "Mentions"}));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(mViewPager);
    }

    private void initializeUser() {
        mClient.getUser(mUserDetailsHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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

    public void postTweet(MenuItem item) {
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_POST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_POST) {
            mHomeTimelineFragment.clearTweets();
            mMentionsFragment.clearTweets();
//            mUserTimelineFragment.clearTweets();

            // Get the timeline again
            mHomeTimelineFragment.populateTimeline();
            mMentionsFragment.populateTimeline();
//            mUserTimelineFragment.populateTimeline();
        }
    }

    public void onProfileView(MenuItem item) {
        launchProfileView(this, mUser);
    }

    public static void launchProfileView(Activity activity, TwitterUser user) {
        Intent intent = new Intent(activity, ProfileViewActivity.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }



    private class UserResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                mUser = new TwitterUser(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
