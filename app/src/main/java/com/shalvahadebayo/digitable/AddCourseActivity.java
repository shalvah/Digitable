package com.shalvahadebayo.digitable;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("UnusedParameters")
public class AddCourseActivity extends AppCompatActivity
	{

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_clourse);

			setTitle("New Course");
			setupActionBar();

		}

		private void setupActionBar()
		{
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				// Show the Up button in the action bar.
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}

		public void courseDone(View v)
		{
			EditText codeET = (EditText) findViewById(R.id.courseCodeET);
			EditText titleET = (EditText) findViewById(R.id.courseTitleET);
			EditText unitsET = (EditText) findViewById(R.id.courseUnitsET);
			String code = codeET.getText().toString();
			String title = titleET.getText().toString();
			String units = unitsET.getText().toString();

			if (code.length() == 0 || title.length() == 0)
			{
				Toast.makeText(getBaseContext(), "Please add course details!", Toast.LENGTH_SHORT).show();
				return;
			}

			ContentValues values = new ContentValues();
			try
			{
				values.put(CourseTable.COLUMN_COURSE_CODE, code);
				values.put(CourseTable.COLUMN_COURSE_TITLE, title);
				values.put(CourseTable.COLUMN_UNITS, units);
			} catch (Exception e)
			{
				e.printStackTrace();
			}


			Uri uri = getContentResolver().insert(AssignmentProvider.CONTENT_URI2, values);
			Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_SHORT).show();

			Intent alIntent = new Intent(this, CourseListActivity.class);
			startActivity(alIntent);

		}

		public void courseReset(View v)
		{
			EditText codeET = (EditText) findViewById(R.id.courseCodeET);
			EditText titleET = (EditText) findViewById(R.id.courseTitleET);
			EditText unitsET = (EditText) findViewById(R.id.courseUnitsET);
			codeET.setText("");
			titleET.setText("");
			unitsET.setText("");
		}


	}
