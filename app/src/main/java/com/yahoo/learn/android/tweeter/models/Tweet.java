package com.yahoo.learn.android.tweeter.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ankurj on 2/7/2015.
 */
public class Tweet {
    private static SimpleDateFormat     mDateParser = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

    private String          body;
    private long            tweetUniqueID;
    private long            createdMillis;
    private int             retweets;
    private TwitterUser     user;

    public Tweet(JSONObject tweetJSON) throws JSONException, ParseException {
        body = tweetJSON.getString("text");
        String createdAt = tweetJSON.getString("created_at");
        createdMillis = mDateParser.parse(createdAt).getTime();
        tweetUniqueID = tweetJSON.getLong("id");
        retweets = tweetJSON.getInt("retweet_count");

        user = new TwitterUser(tweetJSON.getJSONObject("user"));
    }

    public String getBody() {
        return body;
    }

    public long getTweetUniqueID() {
        return tweetUniqueID;
    }

    public String getCreatedAtOld() {
        return DateUtils.getRelativeTimeSpanString(createdMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }

    public String getCreatedAt()
    {
//        return internalCreatedAt() + ":" + getCreatedAtOld();
        return internalCreatedAt();
    }

    public String internalCreatedAt() {
        long diffMillis = System.currentTimeMillis() - createdMillis;
        long diffSec = diffMillis / 1000;
        if (diffSec < 0)
            diffSec = 0;
        long diffMin = diffSec / 60;
        long diffHour = diffMin / 60;
        long diffDays = diffHour / 24;

        if (diffDays > 0)
            return "" + diffDays + "d";
        if (diffHour > 0)
            return "" + diffHour + "h";
        if (diffMin > 0)
            return "" + diffMin + "m";
        return "" + diffSec + "s";
    }




    public int getRetweets() {
        return retweets;
    }

    public TwitterUser getUser() {
        return user;
    }

    public String toString() {
        return getUser().getScreenName() + "\t" + getCreatedAt() + "\n" + body;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray response) throws JSONException, ParseException {
        ArrayList<Tweet> results = new ArrayList<Tweet>();
        for (int i=0, len=response.length(); i<len; i++) {
            results.add(new Tweet(response.getJSONObject(i)));
        }

        return results;
    }

    public String getMetadata() {
        return "<b>" + user.getName() + "</b> <i>@" + user.getScreenName() + "</i>";
    }
}
