package com.shalvahadebayo.digitable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;


public class SetupReaderActivity extends AppCompatActivity
	{

		private static final String PREFERENCES_FILE = "pref_file";
		public static String PREF_NO_OF_INTERESTS = "no_of_interests";
		public static String PREF_INTEREST = "interest_";
		public static String PREF_READER_FIRST_TIME = "reader_first_time";
		private int noOfInterests = 1;

		public static void saveSharedSetting(Context ctx, String settingName, String settingValue)
		{
			SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context
					.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(settingName, settingValue);
			editor.apply();
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_setup_reader);

			ActionBar bar = getSupportActionBar();
			if (bar != null)
				bar.hide();
		}

		public void readerSetupDone(View v)
		{
			int[] ids = new int[]{R.id.checkbox_agric, R.id.checkbox_fluid,
					R.id.checkbox_metmat, R.id.checkbox_ml, R.id.checkbox_phil,
					R.id.checkbox_puzzles, R.id.checkbox_software_dev,
					R.id.checkbox_thermo};
			String[] values = new String[]{"agric", "fluid mechanics",
					"materials", "machine learning", "philosophy",
					"logic puzzles", "software development",
					"thermodynamics"};
			for (int i = 0; i < ids.length; i++)
			{
				if (chosen(ids[i]))
				{
					saveSharedSetting(getBaseContext(), PREF_INTEREST + noOfInterests,
							values[i]);
					noOfInterests++;
				}

			}
			saveSharedSetting(getBaseContext(), PREF_READER_FIRST_TIME,
					"false");
			saveSharedSetting(getBaseContext(), PREF_NO_OF_INTERESTS,
					"" + noOfInterests);

			Intent i = new Intent(this, StoryListActivity.class);
			startActivity(i);
			finish();
		}

		private boolean chosen(int i)
		{
			CheckBox cB = (CheckBox) this.findViewById(i);
			return cB.isChecked();
		}

		@Override
		public void onBackPressed()
		{
			NavUtils.navigateUpFromSameTask(this);
		}
	}
