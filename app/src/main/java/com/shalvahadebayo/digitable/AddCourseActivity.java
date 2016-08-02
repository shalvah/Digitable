package com.shalvahadebayo.digitable;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("UnusedParameters")
public class AddCourseActivity extends AppCompatActivity
	{

		boolean editing;
		EditText codeET;
		EditText unitsET;
		EditText titleET;

		String idString = "";
		String title;
		String code;
		int units;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_course);
			editing = (getIntent().getExtras() != null) ? true : false;

			setTitle("New Course");
			setupActionBar();

			codeET = (EditText) findViewById(R.id.courseCodeET);
			unitsET = (EditText) findViewById(R.id.courseUnitsET);
			titleET = (EditText) findViewById(R.id.courseTitleET);
			codeET.addTextChangedListener(new TextWatcher()
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
						Button doneButton = (Button) findViewById(R.id.courseDoneBtn);
						if (!codeET.getText().toString().equals("") && !unitsET.getText().toString().equals(""))
						{
							doneButton.setEnabled(true);
							doneButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
						} else
						{
							doneButton.setEnabled(false);
							doneButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
						}
					}
				});
			unitsET.addTextChangedListener(new TextWatcher()
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
						Button doneButton = (Button) findViewById(R.id.courseDoneBtn);
						if (!codeET.getText().toString().equals("") && !unitsET.getText().toString().equals(""))
						{
							doneButton.setEnabled(true);
							doneButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
						} else
						{
							doneButton.setEnabled(false);
							doneButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
						}
					}
				});

			if (editing)
			{
				setTitle("Edit Course");
				idString = getIntent().getStringExtra(CourseDetailFragment.ARG_ITEM_ID);
				Uri uri = DataProvider.CONTENT_URI2;
				Cursor cursor = getContentResolver()
						.query(
								uri,
								new String[]{CourseTable.COLUMN_ID, CourseTable.COLUMN_COURSE_CODE, CourseTable
										.COLUMN_COURSE_TITLE, CourseTable.COLUMN_UNITS},
								CourseTable.COLUMN_ID + "=?",
								new String[]{idString},
								null);
				cursor.moveToFirst();


				code = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_CODE));
				title = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_TITLE));
				units = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_UNITS));


				titleET.setText(title);
				codeET.setText(code);
				unitsET.setText("" + units);
				Button doneButton = (Button) findViewById(R.id.courseDoneBtn);

				doneButton.setEnabled(true);
				doneButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

			}

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
				if (!title.equals(""))
					values.put(CourseTable.COLUMN_COURSE_TITLE, title);
				values.put(CourseTable.COLUMN_UNITS, units);
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			if (editing)
				getContentResolver().delete(Uri.parse(DataProvider.CONTENT_URI2 + "/" + idString), null,
						null);
			getContentResolver().insert(DataProvider.CONTENT_URI2, values);
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

		@Override
		public void onBackPressed()
		{
			finish();
		}
	}
