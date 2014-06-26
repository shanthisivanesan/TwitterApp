package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TimelineActivity extends Activity {

	private static final int COMPOSE_ACTIVITY_REQUEST_CODE = 100;
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private SwipeRefreshLayout swipeLayout;
	private ListView lvTweets;
	//long max_id_pointer = 0;
	//long since_id_pointer = 0;
	//int count = 30;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupViews();
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {		
			@Override
			public void onRefresh() {
				fetchNewTweets();
			}
		});

	    swipeLayout.setColorScheme(android.R.color.holo_blue_dark,
	            android.R.color.holo_green_dark,
	            android.R.color.holo_orange_dark,
	            android.R.color.holo_red_dark);

		client = TwitterApplication.getRestClient();
		
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this,tweets);
		lvTweets.setAdapter(aTweets);
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				String max_id = String.valueOf(tweets.get(tweets.size()-1).getUid());
				fetchOldTweets(max_id);
			}
		});
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long rowid) {
				Intent i = new Intent(TimelineActivity.this, DetailActivity.class);
				i.putExtra("tweet",  aTweets.getItem(position));
				startActivity(i);
			}
			
		});

		populateTimeline();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline_menu, menu);
		return true;
	}

	void setupViews() {
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
	}
	
	public void onComposeAction(MenuItem mi) {
		 Intent intent = new Intent(this,ComposeActivity.class);
		 if(intent!=null)		 {
			 startActivityForResult(intent, COMPOSE_ACTIVITY_REQUEST_CODE);
		 }
	  }
	

	void populateTimeline() {
		fetchOldTweets(null);
	}
	void fetchNewTweets() {
		swipeLayout.setRefreshing(true);
		String since_id = String.valueOf(tweets.get(0).getUid());
		client.getHomeTimeline(since_id, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray tweets) {
				TimelineActivity.this.tweets.addAll(0, Tweet.fromJSONArray(tweets));
				TimelineActivity.this.aTweets.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}

			@Override
			public void onFinish() {
				swipeLayout.setRefreshing(false);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPOSE_ACTIVITY_REQUEST_CODE &&
				resultCode == RESULT_OK) {
			String status = data.getStringExtra("status").trim();
			if (status.length() > 0) {
				client.postUpdate(status, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						TimelineActivity.this.fetchNewTweets();
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						Log.d("debug", s);

						// TODO: localize error message...
						Toast.makeText(TimelineActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
	
	void fetchOldTweets(String max_id) {
		swipeLayout.setRefreshing(true);
		client.getHomeTimeline(null,max_id,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json){
				aTweets.addAll(Tweet.fromJSONArray(json));
				//Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFailure(Throwable e, String s){
				Log.d("debug",e.toString());
				Log.d("debug",s);
			}
			@Override
			public void onFinish(){
				swipeLayout.setRefreshing(false);
			}
		});
	}

}
