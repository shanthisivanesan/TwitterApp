package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet implements Serializable {

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	private String body;
	private long uid;
	private String createdAt;
	private User user;
	
	public static Tweet fromJSON(JSONObject jsonObject)
	{
		Tweet tweet = new Tweet();
		//extract values from the JSON to populate the member variables
		try
		{
			tweet.body = jsonObject.getString("text");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.uid = jsonObject.getLong("id");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		}
		catch(JSONException e){
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	//serailization of a collection
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets= new ArrayList<Tweet>(jsonArray.length());
		for(int i=0;i<jsonArray.length();i++)
		{
			JSONObject tweetJson = null;
			try{
				tweetJson =jsonArray.getJSONObject(i);
			}
			catch(Exception e){
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if(tweet !=null){
				tweets.add(tweet);
			}
		}
		return tweets;
	}

	/*@Override
	public String toString() {
		return getBody();
		//+"-"+getUser().getScreenName();
	}*/
}
