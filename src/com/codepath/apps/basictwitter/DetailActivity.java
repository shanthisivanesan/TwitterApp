package com.codepath.apps.basictwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends Activity {

	ImageView ivProfileImg;
	TextView tvScreenName;
	TextView tvName;
	TextView tvBody;
	TextView tvTimestamp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		setupViews();
		Intent i = getIntent();
		Tweet t = (Tweet) i.getSerializableExtra("tweet");
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(t.getUser().getImageProfileUrl(), ivProfileImg);
	    tvScreenName.setText(t.getUser().getScreenName());
	    tvName.setText(t.getUser().getName());
		tvBody.setText(t.getBody());
		tvTimestamp.setText(t.getCreatedAt());
		
	}
	
	public void setupViews() {
		ivProfileImg = (ImageView) findViewById(R.id.ivProfileImg);
		ivProfileImg.setImageResource(android.R.color.transparent);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		tvName = (TextView) findViewById(R.id.tvName);
		tvBody = (TextView) findViewById(R.id.tvBody);
		tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
	}
}

