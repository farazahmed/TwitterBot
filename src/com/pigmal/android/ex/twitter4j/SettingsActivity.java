package com.pigmal.android.ex.twitter4j;

import java.util.ArrayList;

import org.w3c.dom.ls.LSInput;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity implements OnClickListener{
	
	EditText edt_hash_tag;
	Button btnsavehashtag;
	EditText edt_sentence;
	Button btnaddtolist;
	
	private static SharedPreferences mSharedPreferences;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		edt_hash_tag = (EditText)findViewById(R.id.edt_hash_tag);
		btnsavehashtag = (Button)findViewById(R.id.btnsavehashtag);
		edt_sentence = (EditText)findViewById(R.id.edt_sentence);
		btnaddtolist = (Button)findViewById(R.id.btnaddtolist);
		btnsavehashtag.setOnClickListener(this);
		btnaddtolist.setOnClickListener(this);
		
		mSharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME,
				MODE_PRIVATE);
		
		
		
		
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.edt_hash_tag:
			
			break;
		case R.id.btnsavehashtag:
			if(validateHashTag()){
				putHashTag(edt_hash_tag.getText().toString());
				edt_hash_tag.setText("");
			}
			break;
		case R.id.edt_sentence:
			
			break;
		case R.id.btnaddtolist:
			if(validateEditText()){
				putSentence(edt_sentence.getText().toString());
				edt_sentence.setText("");
			}
			break;

			
		default:
			break;
		}
	}


	private boolean validateEditText() {
		if(edt_sentence.getText().toString().trim().length() < 1){
			edt_sentence.setError("empty field");
			return false;
		}
		
		return true;
	}


	private boolean validateHashTag() {
		if(edt_hash_tag.getText().toString().trim().length() < 0 ){
			edt_hash_tag.setText("");
			edt_hash_tag.setError("Empty Field");
			return false;
		}
		else if(edt_hash_tag.getText().toString().trim().length() <= 1){
			edt_hash_tag.setError("Invalid Hashtag");
			return false;
		}
		else if(!edt_hash_tag.getText().toString().trim().startsWith("#")){
			edt_hash_tag.setError("Invalid Hashtag");
			return false;
		}
			
		return true;
	}
	
	
	
	private void putHashTag(String hashtag){
		
		Editor e = mSharedPreferences.edit();
		e.putString(Const.PREF_KEY_HASH_TAG, hashtag);
		e.putString(Const.PREF_KEY_SENTENCES, "");
		e.commit();
		
	}
	
	private void putSentence(String sentence){
		ArrayList<String> listOfWords = new ArrayList<String>();
		String[] savedSentences = null;
		String sentences = mSharedPreferences.getString(Const.PREF_KEY_SENTENCES, "");
		if(!sentences.equalsIgnoreCase("")){
			savedSentences = sentences.split(",");
			if(savedSentences.length > 0){
				for(int i=0;i<savedSentences.length;i++){
					listOfWords.add(savedSentences[i].trim());
				}
			}
		}
		listOfWords.add(sentence);
		String convertedWords = listOfWords.toString();
		convertedWords = convertedWords.substring(1, convertedWords.length()-1);
		Editor e = mSharedPreferences.edit();
		e.putString(Const.PREF_KEY_SENTENCES, convertedWords);
		e.commit();
	}
	
	
}
