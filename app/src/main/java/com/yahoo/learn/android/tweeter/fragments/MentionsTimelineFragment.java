package com.yahoo.learn.android.tweeter.fragments;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.learn.android.tweeter.application.TwitterApplication;
import com.yahoo.learn.android.tweeter.application.TwitterClient;
import com.yahoo.learn.android.tweeter.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ankurj on 2/14/2015.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient mClient;
    private TimelineResponseHandler     mResponseHandler;

    private long                        mEarliestTweetID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = TwitterApplication.getRestClient();
        mResponseHandler = new TimelineResponseHandler();
        mEarliestTweetID = 0;
    }


    @Override
    protected void populateTweetList() {
        mClient.getMentions(mEarliestTweetID, mResponseHandler);
    }

    public void clearTweets() {
        super.clearTweets();
        mEarliestTweetID = 0;
    }


    private class TimelineResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            if (response.length() > 0) {
                try {
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                    mEarliestTweetID = tweets.get(tweets.size() - 1).getTweetUniqueID();
                    addAll(tweets);
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
}
