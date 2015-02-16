package com.yahoo.learn.android.tweeter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.learn.android.tweeter.R;
import com.yahoo.learn.android.tweeter.activities.HomeActivity;
import com.yahoo.learn.android.tweeter.models.TwitterUser;

import org.w3c.dom.Text;

/**
 * Created by ankurj on 2/15/2015.
 */
public class UserHeaderFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mUser = (TwitterUser) getArguments().getSerializable("user");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_header2 , container, false);

        populateHeader(view);

        return view;
    }

    private void populateHeader(View view) {
        TwitterUser user = (TwitterUser) getArguments().getSerializable("user");

        ImageView ivViewUser = (ImageView) view.findViewById(R.id.ivViewUser);
        TextView tvViewUserDetails = (TextView) view.findViewById(R.id.tvViewUserDetails);

        TextView tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);


        ivViewUser.setImageResource(0);
        Picasso.with(getActivity()).load(user.getImageUrl()).into(ivViewUser);

        tvViewUserDetails.setText(Html.fromHtml(user.getUserDetails()));
        tvFollowers.setText(user.getNumFollowers() + " followers");
        tvFollowing.setText(user.getNumFollowing() + " following");
    }


    public static UserHeaderFragment newInstance(TwitterUser user) {
        UserHeaderFragment fragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

}
