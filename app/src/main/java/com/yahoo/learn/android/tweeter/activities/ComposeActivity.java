package com.yahoo.learn.android.tweeter.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.application.TwitterApplication;
import com.yahoo.learn.android.tweeter.application.TwitterClient;
import com.yahoo.learn.android.tweeter.models.TwitterUser;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {
    private ImageView       mIvUserIcon;
    private TextView        mTvUserDetails;
    private EditText        mEtTweet;
    private TwitterClient   mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        mIvUserIcon = (ImageView) findViewById(R.id.ivPostUser);
        mTvUserDetails = (TextView) findViewById(R.id.tvPostUserDetails);
        mEtTweet = (EditText) findViewById(R.id.etTweet);
        mEtTweet.setText("");
        mClient = TwitterApplication.getRestClient();
        Button btnSubmit = (Button) findViewById(R.id.btnTweet);
        btnSubmit.setVisibility(View.GONE);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);

        populateScreen();
    }

    private void populateScreen() {
        mIvUserIcon.setImageResource(0);
        TwitterUser user = HomeActivity.mUser;
        Picasso.with(this).load(user.getImageUrl()).into(mIvUserIcon);

        mTvUserDetails.setText("@" + user.getScreenName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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

    public void onCancel(View view) {
        onCancel();
    }

    private void onCancel() {
        mEtTweet.setText("");
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onSubmit(View view) {
        onSubmit();
    }

    private void onSubmit() {
        String tweetText = mEtTweet.getText().toString();
        mClient.postTweet(tweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                mEtTweet.setText("");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    public void onMenuSubmit(MenuItem item) {
        onSubmit();
    }

    public void onMenuCancel(MenuItem item) {
        onCancel();
    }
}
