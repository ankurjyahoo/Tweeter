package com.yahoo.learn.android.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/7/2015.
 */
public class TwitterUser {
    private String      name;
    private long        uid;
    private String      screenName;
    private String      imageUrl;


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

    public TwitterUser(JSONObject userObject) throws JSONException {
        name = userObject.getString("name");
        screenName = userObject.getString("screen_name");
        imageUrl = userObject.getString("profile_image_url");
        uid = userObject.getLong("id");


    }

}
