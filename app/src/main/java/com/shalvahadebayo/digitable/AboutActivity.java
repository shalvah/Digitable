package com.shalvahadebayo.digitable;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about); //// TODO: 05/06/2016 work on the layout
		setTitle("Help and Feedback");
		setupActionBar();
	}

	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	public void callOrMail(View v)
	{
		switch (v.getId())
		{
			case R.id.call_btn:
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData(Uri.parse("tel:080000000"));
				startActivity(callIntent);
				break;
			case R.id.email_btn:
				Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
				mailIntent.setData(Uri.parse("mailto:me@me,com"));
				startActivity(mailIntent);
				break;
		}
	}

	public void launchHelp(View view)
	{
		Intent intent = new Intent(getBaseContext(), StoryDetailActivity.class);
		intent.putExtra(StoryDetailFragment.ARG_STORY_ID, "1");
		startActivity(intent);
	}
}
