package com.pigmal.android.ex.twitter4j;

import java.util.ArrayList;
import java.util.Random;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TweetingService extends Service {
   
	private static SharedPreferences mSharedPreferences;
	boolean isTrue = true;
	
	@Override
   public IBinder onBind(Intent arg0) {
      return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      // Let it continue running until it is stopped.
      Toast.makeText(this, "Bot Tweeting", Toast.LENGTH_LONG).show();
      return START_STICKY;
   }
   @Override
   public void onDestroy() {
      super.onDestroy();
      isTrue = false;
      Toast.makeText(this, "Bot Stopped", Toast.LENGTH_LONG).show();
   }
   
   @Override
	public void onCreate() {
		super.onCreate();
		mSharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME,
				MODE_PRIVATE);
		new GetRandomTime().execute();
   }
   
   private void updateStatus1(String tweet) throws TwitterException {

		if (tweet != null) {

			String hashtag = mSharedPreferences.getString(
					Const.PREF_KEY_HASH_TAG, "");
			final String status =  tweet.trim() + "\n" + hashtag.trim();
			if (isConnected()) {

				
				  ConfigurationBuilder builder = new ConfigurationBuilder();
				  builder.setOAuthConsumerKey(Const.CONSUMER_KEY);
				  builder.setOAuthConsumerSecret(Const.CONSUMER_SECRET);
				  
				  // Access Token String 
				  String access_token =
				  mSharedPreferences.getString( Const.PREF_KEY_TOKEN, ""); //
				  //Access Token Secret 
				  String access_token_secret =mSharedPreferences.getString( Const.PREF_KEY_SECRET, "");
				  
				  AccessToken accessToken = new AccessToken(access_token,
				  access_token_secret); 
				  Twitter twitter = new
				  TwitterFactory(builder.build()) .getInstance(accessToken);
				  
				  // Update status final twitter4j.Status response =
				  twitter.updateStatus(status);
					final twitter4j.Status response = twitter
							.updateStatus(status);
				  Log.d("Status", "> " + response.getText());
				 
				/*tweetText.post(new Runnable() {
					@Override
					public void run() {
						tweetText.append("Tweeted: " + status);
					}
				});*/
				
				
				Log.e("Tweeting", status);
				Editor e = mSharedPreferences.edit();
				e.putString(Const.PREF_KEY_LAST_TWEET, "Tweeted: " + response.getText());
				e.commit();

				
			}
		} else {

			/*tweetText.post(new Runnable() {
				@Override
				public void run() {
					tweetText.append("Tweeted: " + "No tweets to send");
					
				}
			});
			*/
			Log.e("Tweeting", "No Tweets to send");
			Editor e = mSharedPreferences.edit();
			e.putString(Const.PREF_KEY_LAST_TWEET, "No tweets to send");
			e.commit();

		}

	}
   
   private boolean isConnected() {
		return mSharedPreferences.getString(Const.PREF_KEY_TOKEN, null) != null;
	}
   

	private String getTweet() {
		ArrayList<String> listOfWords = new ArrayList<String>();
		String[] savedSentences = null;
		String sentences = mSharedPreferences.getString(
				Const.PREF_KEY_SENTENCES, "");
		if (!sentences.equalsIgnoreCase("")) {
			savedSentences = sentences.split(",");
			if (savedSentences.length > 0) {
				for (int i = 0; i < savedSentences.length; i++) {
					listOfWords.add(savedSentences[i].trim());
				}
			}
		}
		String tweet = null;
		String convertedWords = "";
		if (listOfWords.size() > 0) {
			tweet = listOfWords.get(0);
			listOfWords.remove(0);
			if (listOfWords.size() > 0) {
				convertedWords = listOfWords.toString();
				convertedWords = convertedWords.substring(1,
						convertedWords.length() - 1);
			}
			Editor e = mSharedPreferences.edit();
			e.putString(Const.PREF_KEY_SENTENCES, convertedWords);
			e.commit();
		}
		return tweet;
	}

	
	private class GetRandomTime extends AsyncTask<Void, Void, Void> {
		Random rand = new Random();
		@Override
		protected Void doInBackground(Void... params) {
			
			while(isTrue){
				int random = rand.nextInt(5)+1;
				random = (random * 60 ) *1000;
				try {
					Thread.sleep(random);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					updateStatus1(getTweet());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
	}
	
	
   
}