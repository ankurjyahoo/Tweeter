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
public class UserTimelineFragment extends TweetsListFragment {
    private static final String ARG_SCREEN_NAME = "screen_name";
    private TwitterClient mClient;
    private String                      mScreenName;


    public void setScreenName(String screenName) {mScreenName = screenName;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenName = getArguments().getString(ARG_SCREEN_NAME);
        mClient = TwitterApplication.getRestClient();
//        mResponseHandler = new TimelineResponseHandler();
    }


    @Override
    protected void populateTweetList() {
        mClient.getUserTimeline(mScreenName, getEarliestTweetID(), getResponseHandler());
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SCREEN_NAME, screenName);
        fragment.setArguments(args);
        return fragment;
    }
}
