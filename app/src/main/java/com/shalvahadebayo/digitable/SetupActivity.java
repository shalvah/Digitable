package com.shalvahadebayo.digitable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends AppCompatActivity
	{

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_setup);
			final Button getStrt = (Button) findViewById(R.id.get_started_btn);

			final EditText fieldStudyET = (EditText) findViewById(R.id.field_study);
			assert fieldStudyET != null;
			fieldStudyET.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after)
					{

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count)
					{

					}

					@Override
					public void afterTextChanged(Editable s)
					{
						if (fieldStudyET.getText().toString().equals(""))
						{
							getStrt.setEnabled(false);
							getStrt.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
						} else
						{
							getStrt.setEnabled(true);
							getStrt.setBackgroundColor(getResources().getColor(R.color.colorAccent));

						}
					}
				});
			assert getStrt != null;
			getStrt.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//  update 1st time pref
						HomeActivity.saveSharedSetting(getBaseContext(), HomeActivity.PREF_USER_FIRST_TIME,
								"false");
						HomeActivity.saveSharedSetting(getBaseContext(), HomeActivity.PREF_USER_STUDY_FIELD,
								fieldStudyET.getText().toString());
						HomeActivity.saveSharedSetting(getBaseContext(), StoryListActivity.PREF_NO_OF_STORIES, "1");
						Intent i = new Intent(getApplication(), HomeActivity.class);
						startActivity(i);
						finish();

					}
				});

		}
	}
