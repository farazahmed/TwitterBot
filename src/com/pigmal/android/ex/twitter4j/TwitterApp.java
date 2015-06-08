package com.pigmal.android.ex.twitter4j;

import java.util.ArrayList;
import java.util.Random;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TwitterApp extends Activity implements OnClickListener {
	private static final String TAG = "T4JSample";

	private Button buttonLogin;
	private Button getTweetButton;
	private Button btnstarttweeting;

	private TextView tweetText;
	// private ScrollView scrollView;
	boolean isTrue = true;
	private static Twitter twitter;
	private static RequestToken requestToken;
	private static SharedPreferences mSharedPreferences;
	private static TwitterStream twitterStream;
	private boolean running = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME,
				MODE_PRIVATE);
		// scrollView = (ScrollView)findViewById(R.id.scrollView);
		tweetText = (TextView) findViewById(R.id.tweetText);
		getTweetButton = (Button) findViewById(R.id.getTweet);
		getTweetButton.setOnClickListener(this);
		buttonLogin = (Button) findViewById(R.id.twitterLogin);
		btnstarttweeting = (Button) findViewById(R.id.btnstarttweeting);
		buttonLogin.setOnClickListener(this);
		btnstarttweeting.setOnClickListener(this);
		new Show().execute();
		if(isConnected()){
			btnstarttweeting.setEnabled(true);
			if(isMyServiceRunning(TweetingService.class)){
				btnstarttweeting.setText("Stop Tweeting");
			}else {
				btnstarttweeting.setText("Start Tweeting");
			}
		}else {
			btnstarttweeting.setEnabled(false);
		}
		
		/**
		 * Handle OAuth Callback
		 */
		Uri uri = getIntent().getData();
		if (uri != null && uri.toString().startsWith(Const.CALLBACK_URL)) {
			String verifier = uri
					.getQueryParameter(Const.IEXTRA_OAUTH_VERIFIER);
			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);
				Editor e = mSharedPreferences.edit();
				e.putString(Const.PREF_KEY_TOKEN, accessToken.getToken());
				e.putString(Const.PREF_KEY_SECRET, accessToken.getTokenSecret());
				e.commit();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

	protected void onResume() {
		super.onResume();

		if (isConnected()) {
			String oauthAccessToken = mSharedPreferences.getString(
					Const.PREF_KEY_TOKEN, "");
			String oAuthAccessTokenSecret = mSharedPreferences.getString(
					Const.PREF_KEY_SECRET, "");

			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			Configuration conf = confbuilder
					.setOAuthConsumerKey(Const.CONSUMER_KEY)
					.setOAuthConsumerSecret(Const.CONSUMER_SECRET)
					.setOAuthAccessToken(oauthAccessToken)
					.setOAuthAccessTokenSecret(oAuthAccessTokenSecret).build();
			// twitterStream = new TwitterStreamFactory(conf).getInstance();
			/*try {
				updateStatus1(getTweet());
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			buttonLogin.setText(R.string.label_disconnect);
			getTweetButton.setEnabled(true);
			btnstarttweeting.setEnabled(true);
		} else {
			buttonLogin.setText(R.string.label_connect);
		}
	}

	/**
	 * check if the account is authorized
	 * 
	 * @return
	 */
	private boolean isConnected() {
		return mSharedPreferences.getString(Const.PREF_KEY_TOKEN, null) != null;
	}

	private void askOAuth() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(Const.CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(Const.CONSUMER_SECRET);
		Configuration configuration = configurationBuilder.build();
		twitter = new TwitterFactory(configuration).getInstance();

		try {
			requestToken = twitter.getOAuthRequestToken(Const.CALLBACK_URL);
			Toast.makeText(this, "Please authorize this app!",
					Toast.LENGTH_LONG).show();
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse(requestToken.getAuthenticationURL())));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove Token, Secret from preferences
	 */
	private void disconnectTwitter() {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.remove(Const.PREF_KEY_TOKEN);
		editor.remove(Const.PREF_KEY_SECRET);
		editor.commit();
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.twitterLogin:
			if (isConnected()) {
				disconnectTwitter();
				buttonLogin.setText(R.string.label_connect);
			} else {
				askOAuth();
			}
			break;
		case R.id.getTweet:
			/*
			 * if (running) { //stopStreamingTimeline(); //running = false;
			 * getTweetButton.setText("start streaming"); } else {
			 * startStreamingTimeline(); running = true;
			 * getTweetButton.setText("stop streaming"); }
			 */

			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.btnstarttweeting:
			if(isMyServiceRunning(TweetingService.class)){
				btnstarttweeting.setText("Start Tweeting");
				stopService();
			}else {
				btnstarttweeting.setText("Stop Tweeting");
				startService();
			}
		break;
	}
	
		
		
	}

	private void stopStreamingTimeline() {
		twitterStream.shutdown();
	}

	public void startStreamingTimeline() {
		UserStreamListener listener = new UserStreamListener() {

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				System.out.println("deletionnotice");
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				System.out.println("scrubget");
			}

			@Override
			public void onStatus(Status status) {
				final String tweet = "@" + status.getUser().getScreenName()
						+ " : " + status.getText() + "\n";
				System.out.println(tweet);
				tweetText.post(new Runnable() {
					@Override
					public void run() {
						tweetText.append(tweet);
						// scrollView.fullScroll(View.FOCUS_DOWN);
					}
				});
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				System.out.println("trackLimitation");
			}

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onBlock(User arg0, User arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDeletionNotice(long arg0, long arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDirectMessage(DirectMessage arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFollow(User arg0, User arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFriendList(long[] arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUnblock(User arg0, User arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUnfavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListCreation(User arg0, UserList arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListDeletion(User arg0, UserList arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListMemberAddition(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListMemberDeletion(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListSubscription(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListUnsubscription(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListUpdate(User arg0, UserList arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserProfileUpdate(User arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}
		};
		twitterStream.addListener(listener);
		twitterStream.user();
	}

	private void updateStatus(String tweet) {

		try {
			if (tweet != null) {

				String hashtag = mSharedPreferences.getString(
						Const.PREF_KEY_HASH_TAG, "");
				String status = hashtag + tweet;
				if (isConnected()) {

					ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(Const.CONSUMER_KEY);
					builder.setOAuthConsumerSecret(Const.CONSUMER_SECRET);

					// Access Token
					String access_token = mSharedPreferences.getString(
							Const.PREF_KEY_TOKEN, "");
					// Access Token Secret
					String access_token_secret = mSharedPreferences.getString(
							Const.PREF_KEY_SECRET, "");

					AccessToken accessToken = new AccessToken(access_token,
							access_token_secret);
					Twitter twitter = new TwitterFactory(builder.build())
							.getInstance(accessToken);

					// Update status
					final twitter4j.Status response = twitter
							.updateStatus(status);

					Log.d("Status", "> " + response.getText());

					tweetText.post(new Runnable() {
						@Override
						public void run() {
							tweetText.append("Tweeted: " + response.getText());
						}
					});
				}
			} else {

				tweetText.post(new Runnable() {
					@Override
					public void run() {
						tweetText.append("Tweeted: " + "No tweets to send");
					}
				});

			}
		} catch (TwitterException e) {
			// Error in updating status
			Log.d("Twitter Update Error", e.getMessage());
		}

	}

	private void updateStatus1(String tweet) throws TwitterException {

		if (tweet != null) {

			String hashtag = mSharedPreferences.getString(
					Const.PREF_KEY_HASH_TAG, "");
			final String status = hashtag + tweet;
			if (isConnected()) {

				/*
				 * ConfigurationBuilder builder = new ConfigurationBuilder();
				 * builder.setOAuthConsumerKey(Const.CONSUMER_KEY);
				 * builder.setOAuthConsumerSecret(Const.CONSUMER_SECRET);
				 * 
				 * // Access Token String access_token =
				 * mSharedPreferences.getString( Const.PREF_KEY_TOKEN, ""); //
				 * Access Token Secret String access_token_secret =
				 * mSharedPreferences.getString( Const.PREF_KEY_SECRET, "");
				 * 
				 * AccessToken accessToken = new AccessToken(access_token,
				 * access_token_secret); Twitter twitter = new
				 * TwitterFactory(builder.build()) .getInstance(accessToken);
				 * 
				 * // Update status final twitter4j.Status response =
				 * twitter.updateStatus(status);
				 * 
				 * Log.d("Status", "> " + response.getText());
				 */
				tweetText.post(new Runnable() {
					@Override
					public void run() {
						tweetText.append("Tweeted: " + status);
					}
				});
			}
		} else {

			tweetText.post(new Runnable() {
				@Override
				public void run() {
					tweetText.append("Tweeted: " + "No tweets to send");
				}
			});

		}

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isTrue = false;
		if(isMyServiceRunning(TweetingService.class)){
			stopService();
		}
		//Toast.makeText(this, "Destroy", Toast.LENGTH_LONG).show();
	}

	
	 // Method to start the service
	   public void startService() {
	      startService(new Intent(getBaseContext(), TweetingService.class));
	   }

	   // Method to stop the service
	   public void stopService() {
	      stopService(new Intent(getBaseContext(), TweetingService.class));
	   }
	   
	   
	   private boolean isMyServiceRunning(Class<?> serviceClass) {
		    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (serviceClass.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}
	   
	   
	   private class Show extends AsyncTask<Void, Void, Void> {
			@Override
			protected Void doInBackground(Void... params) {
				while(isTrue){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tweetText.post(new Runnable() {
						@Override
						public void run() {
							tweetText.setText(mSharedPreferences.getString(Const.PREF_KEY_LAST_TWEET, ""));
						}
					});
				}
				
				return null;
			}
			
		}
	   
	
}
