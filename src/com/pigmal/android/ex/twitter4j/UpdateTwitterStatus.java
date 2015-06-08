/*package com.pigmal.android.ex.twitter4j;

import android.app.ProgressDialog;
import android.os.AsyncTask;

*//**
 * Function to update status
 * *//*
class UpdateTwitterStatus extends AsyncTask<String, String, String> {
 
    *//**
     * Before starting background thread Show Progress Dialog
     * *//*
	public UpdateTwitterStatus() {
	
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Updating to twitter...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
 
    *//**
     * getting Places JSON
     * *//*
    protected String doInBackground(String... args) {
        Log.d("Tweet Text", "> " + args[0]);
        String status = args[0];
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
             
            // Access Token 
            String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            // Access Token Secret
            String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
             
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
             
            // Update status
            twitter4j.Status response = twitter.updateStatus(status);
             
            Log.d("Status", "> " + response.getText());
        } catch (TwitterException e) {
            // Error in updating status
            Log.d("Twitter Update Error", e.getMessage());
        }
        return null;
    }
 
    *//**
     * After completing background task Dismiss the progress dialog and show
     * the data in UI Always use runOnUiThread(new Runnable()) to update UI
     * from background thread, otherwise you will get error
     * **//*
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after getting all products
        pDialog.dismiss();
        // updating UI from Background Thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        "Status tweeted successfully", Toast.LENGTH_SHORT)
                        .show();
                // Clearing EditText field
                txtUpdate.setText("");
            }
        });
    }
 
}*/