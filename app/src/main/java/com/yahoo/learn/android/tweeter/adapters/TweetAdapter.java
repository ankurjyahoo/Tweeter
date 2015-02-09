package com.yahoo.learn.android.tweeter.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ankurj on 2/7/2015.
 *
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {
    public TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Data
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        // Recycled View
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            TextView tvMeta = (TextView) convertView.findViewById(R.id.tvMeta);
            TextView tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
            ImageView ivUserIcon = (ImageView) convertView.findViewById(R.id.ivUserIcon);
            TextView tvTime = (TextView) convertView.findViewById(R.id.tvTimeElapsed);

            viewHolder = new ViewHolder(tvMeta, tvTime, tvTweet, ivUserIcon);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate fields
        viewHolder.tvMeta.setText(Html.fromHtml(tweet.getMetadata()));
        viewHolder.tvTime.setText(tweet.getCreatedAt());
        viewHolder.tvTweet.setText(tweet.getBody());
        viewHolder.ivUserIcon.setImageResource(0);

        // External lib call to load the photo (and manage caching)
        Picasso.with(getContext()).load(tweet.getUser().getImageUrl()).into(viewHolder.ivUserIcon);


        // Return
        return convertView;

    }



    private static class ViewHolder {
        TextView tvTime;
        TextView tvMeta;
        TextView tvTweet;
        ImageView ivUserIcon;

        public ViewHolder(TextView tvMeta, TextView tvTime, TextView tvTweet, ImageView ivIcon) {
            this.tvMeta = tvMeta;
            this.tvTweet = tvTweet;
            this.tvTime = tvTime;
            this.ivUserIcon = ivIcon;
        }


    }


}
