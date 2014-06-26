package com.codepath.apps.basictwitter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0,tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Tweet tweet = getItem(position);

		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweets_item, parent, false);
		} else {
			v = convertView;
		}

		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivImageProfile);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvScreenName);
		TextView tvTweetBody = (TextView) v.findViewById(R.id.tvTweetBody);
		TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimeStamp);
		ivProfileImage.setImageResource(android.R.color.transparent);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getImageProfileUrl(), ivProfileImage);
		tvUserName.setText(tweet.getUser().getScreenName());
		tvTweetBody.setText(tweet.getBody());
		tvTimestamp.setText(getRelativeTweetTime(tweet.getCreatedAt()));
        // Return the completed view to render on screen
        return v;
	}

	private CharSequence getRelativeTweetTime(String createdAt) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(createdAt).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}
	

}
