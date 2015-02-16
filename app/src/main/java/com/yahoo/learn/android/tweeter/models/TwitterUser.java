package com.yahoo.learn.android.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ankurj on 2/7/2015.
 */
public class TwitterUser implements Serializable {
    private String      name;
    private long        uid;
    private String      screenName;
    private String      imageUrl;
    private String      tagline;
    private int         numFollowers;
    private int         numFollowing;


    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getTagline() {
        return tagline;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public TwitterUser(JSONObject userObject) throws JSONException {
        name = userObject.getString("name");
        screenName = userObject.getString("screen_name");
        imageUrl = userObject.getString("profile_image_url");
        uid = userObject.getLong("id");
        tagline = userObject.getString("description");
        numFollowers = userObject.getInt("followers_count");
        numFollowing = userObject.getInt("friends_count");

    }

    public String getUserDetails()
    {
        return "<b>" + name + "</b><br>" + tagline;
    }
}
