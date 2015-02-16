package com.yahoo.learn.android.tweeter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.activities.ComposeActivity;
import com.yahoo.learn.android.tweeter.adapters.EndlessScrollListener;
import com.yahoo.learn.android.tweeter.adapters.TweetAdapter;
import com.yahoo.learn.android.tweeter.application.TwitterApplication;
import com.yahoo.learn.android.tweeter.application.TwitterClient;
import com.yahoo.learn.android.tweeter.models.Tweet;
import com.yahoo.learn.android.tweeter.models.TwitterUser;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ankurj on 2/14/2015.
 */
public abstract class TweetsListFragment extends Fragment {

    public static final int             REQUEST_CODE_POST = 21;
    private static final int            MAX_RESULTS = 300;
    private TweetAdapter                mTweetAdapter;
    private final ArrayList<Tweet>      mTweets = new ArrayList<Tweet>();
    private ListView                    mListViewTweets;
    private TimelineResponseHandler     mResponseHandler;
    private long                        mEarliestTweetID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResponseHandler = new TimelineResponseHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        mListViewTweets = (ListView) v.findViewById(R.id.lvTweets);
        mTweetAdapter = new TweetAdapter(getActivity(), mTweets);

        mListViewTweets.setAdapter(mTweetAdapter);

        mEarliestTweetID = 0;
        mListViewTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                populateTimeline();
            }
        });

        populateTimeline();
        return v;
    }


    public void populateTimeline()
    {
        if (mTweets.size() >= MAX_RESULTS)
            return;

        populateTweetList();
    }

    protected abstract void populateTweetList();

    public void clearTweets() {
        mTweets.clear();
        mTweetAdapter.notifyDataSetChanged();
        mEarliestTweetID = 0;
    }


    protected void addAll(ArrayList<Tweet> tweets) {
        mTweetAdapter.addAll(tweets);
    }

    protected AsyncHttpResponseHandler getResponseHandler() { return mResponseHandler; }
    protected long getEarliestTweetID() { return mEarliestTweetID; }


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
