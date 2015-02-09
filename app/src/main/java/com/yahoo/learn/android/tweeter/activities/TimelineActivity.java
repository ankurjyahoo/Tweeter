package com.yahoo.learn.android.tweeter.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.adapters.EndlessScrollListener;
import com.yahoo.learn.android.tweeter.adapters.TweetAdapter;
import com.yahoo.learn.android.tweeter.application.TwitterApplication;
import com.yahoo.learn.android.tweeter.application.TwitterClient;
import com.yahoo.learn.android.tweeter.models.Tweet;
import com.yahoo.learn.android.tweeter.models.TwitterUser;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    public static final int             REQUEST_CODE_POST = 21;
    private static final int            MAX_RESULTS = 300;
    private TwitterClient               mClient;
    private TweetAdapter                mTweetAdapter;
    private final ArrayList<Tweet>      mTweets = new ArrayList<Tweet>();
    private ListView                    mListViewTweets;
    private TimelineResponseHandler     mResponseHandler;
    public static TwitterUser           mUser;
    private UserResponseHandler         mUserDetailsHandler;
    private long                        mEarliestTweetID = 0;
    private long                        mLatestTweetID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mClient = TwitterApplication.getRestClient();

        mListViewTweets = (ListView) findViewById(R.id.lvTweets);
        mTweetAdapter = new TweetAdapter(this, mTweets);

        mListViewTweets.setAdapter(mTweetAdapter);
        mResponseHandler = new TimelineResponseHandler();
        mUserDetailsHandler = new UserResponseHandler();

        mListViewTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                populateTimeline();
            }
        });



        initializeUser();
        populateTimeline();
    }

    private void initializeUser() {
        mClient.getUser(mUserDetailsHandler);
    }

    private void populateTimeline()
    {
        if (mTweets.size() >= MAX_RESULTS)
            return;

        mClient.getTimeline(mEarliestTweetID, mResponseHandler);
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
            clearTweets();

            // Get the timeline again
            populateTimeline();
        }
    }

    private void clearTweets() {
        mTweets.clear();
        mTweetAdapter.notifyDataSetChanged();
        mEarliestTweetID = 0;
        mLatestTweetID = 0;
    }


    private class TimelineResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            if (response.length() > 0) {
                try {
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                    mEarliestTweetID = tweets.get(tweets.size() - 1).getTweetUniqueID();
                    mTweetAdapter.addAll(tweets);
                    mLatestTweetID = mTweets.get(0).getTweetUniqueID();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if (errorResponse != null)
                Log.d("TIMELINE_ERROR", errorResponse.toString());
            throwable.printStackTrace();
        }
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

