package com.shalvahadebayo.digitable;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddClassActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>
	{

		String startTime;
		int reminderSet = 0;
		Spinner daySpinner;
		Spinner courseSpinner;
		TextView startTimeTV;
		TextView endTimeTV;
		int timeType; //startTime is 1; endTime is 2
		SimpleCursorAdapter sca;
		String weekday;
		Calendar calStart;
		Calendar calEnd;
		private long day;
		private long courseId;
		private boolean editing;
		private String idString;
		private String courseName;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_class);
			setTitle("New Class");
			startTimeTV = (TextView) findViewById(R.id.startTimeTV);
			endTimeTV = (TextView) findViewById(R.id.endTimeTV);

			editing = (getIntent().getExtras() != null);
			setupActionBar();


			getLoaderManager().initLoader(0, null, this);

			sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
					new String[]{CourseTable.COLUMN_COURSE_CODE}, new int[]
					{android.R.id.text1}, 0);
			sca.setDropDownViewResource(R.layout.spinner_item);
			courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
			courseSpinner.setAdapter(sca);
			courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						courseId = parent.getItemIdAtPosition(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});

			daySpinner = (Spinner) findViewById(R.id.daySpinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
					R.array.weekday_values, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(R.layout.spinner_item);
			daySpinner.setAdapter(adapter);
			daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						day = parent.getItemIdAtPosition(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});

			if (editing)
			{
				setTitle("Edit Class");
				//			idString = getIntent().getStringExtra(ClassDetailFragment.ARG_ITEM_ID);
				Uri uri = DataProvider.CONTENT_URI2;
				Cursor cursor = getContentResolver()
						.query(
								uri,
								new String[]{ClassTable.COLUMN_ID, ClassTable.COLUMN_WEEKDAY, ClassTable.COLUMN_TIME_START,
										ClassTable.COLUMN_TIME_START, ClassTable.COLUMN_CLASS_REMINDER_SET, ClassTable
										.COLUMN_COURSE},
								ClassTable.COLUMN_ID + "=?",
								new String[]{idString},
								null);
				cursor.moveToFirst();


				weekday = cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_WEEKDAY));
				String endTime = cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_TIME_END));
				String startTime = cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_TIME_START));
				courseName = cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_COURSE));
				reminderSet = cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_CLASS_REMINDER_SET));

				day = getDay(weekday);
				daySpinner.setSelection((int) day);
				courseSpinner.setSelection((int) courseId);
				startTimeTV.setText(startTime);
				endTimeTV.setText(endTime);
				Button doneButton = (Button) findViewById(R.id.classDoneBtn);

				doneButton.setEnabled(true);
				doneButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
			}


			startTimeTV.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						timeType = 1;
						DialogFragment dFrag = new TimePickerFragment();
						dFrag.show(getFragmentManager(), "timePicker");
					}
				});

			endTimeTV.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						timeType = 2;
						DialogFragment dFrag = new TimePickerFragment();
						dFrag.show(getFragmentManager(), "timePicker");

					}
				});
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

		public void classDone(View v)
		{

			startTime = startTimeTV.getText().toString();
			String endTime = endTimeTV.getText().toString();

			weekday = getWeekday(day);

			Uri mUri = DataProvider.CONTENT_URI2;
			Cursor c = getContentResolver().query(
					mUri,
					new String[]{CourseTable.COLUMN_ID, CourseTable.COLUMN_COURSE_CODE},
					CourseTable.COLUMN_ID + "=?",
					new String[]{"" + (courseId)},
					null);
			c.moveToFirst();

			courseName = c.getString(c.getColumnIndex(CourseTable.COLUMN_COURSE_CODE));


			ContentValues values = new ContentValues();

			if (!startTime.equals("From..."))
				values.put(ClassTable.COLUMN_TIME_START,
						startTime);
			if (!endTime.equals("To..."))
				values.put(ClassTable.COLUMN_TIME_END, endTime);
			values.put(ClassTable.COLUMN_COURSE, courseName);
			values.put(ClassTable.COLUMN_WEEKDAY, weekday);
			values.put(ClassTable.COLUMN_CLASS_REMINDER_SET, "" + reminderSet);

			if (reminderSet == 1)
			{
				setAlarm();
			}

			if (editing)
				getContentResolver().update(Uri.parse(DataProvider.CONTENT_URI3 + "/" + idString),
						values, null,
						null);
			else
				getContentResolver().insert(DataProvider.CONTENT_URI3, values);
			Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_SHORT).show();

			Intent tIntent = new Intent(this, TimetableActivity.class);
			startActivity(tIntent);

		}

		@NonNull
		private String getWeekday(long day)
		{

			switch ((int) day)
			{
				case 0:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					return "Monday";
				case 1:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
					return "Tuesday";
				case 2:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
					return "Wednesday";
				case 3:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					return "Thursday";
				case 4:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
					return "Friday";
				case 5:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					return "Saturday";
				case 6:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					return "Sunday";
				default:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					return "Monday";
			}
		}

		@NonNull
		private long getDay(String mWeekday)
		{

			switch (mWeekday)
			{
				case "Monday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					return 0;
				case "Tuesday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
					return 1;
				case "Wednesday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
					return 2;
				case "Thursday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					return 3;
				case "Friday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
					return 4;
				case "Saturday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					return 5;
				case "Sunday":
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					return 6;
				default:
					calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					return 0;
			}
		}

		public void classReset(View v)
		{
			startTimeTV = (TextView) findViewById(R.id.startTimeTV);
			endTimeTV = (TextView) findViewById(R.id.endTimeTV);
			startTimeTV.setText("From...");
			endTimeTV.setText("to...");
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection = {CourseTable.COLUMN_ID,
					CourseTable.COLUMN_COURSE_CODE};

			return new CursorLoader(this, DataProvider.CONTENT_URI2, projection, null, null,
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			sca.swapCursor(data);

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader)
		{

			sca.swapCursor(null);
		}


		public void setAlarm()
		{

			Intent alIntent = new Intent(this, AlarmReceiver.class);
			alIntent.putExtra(ReminderActivity.CLASS_COURSE, courseName);
			alIntent.putExtra(ReminderActivity.CLASS_START_TIME, startTime);
			alIntent.putExtra(ReminderActivity.TYPE_ASSIGNMENT_CLASS, "class");
			PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, alIntent, 0);

			AlarmManager aMan = (AlarmManager) this.getSystemService(ALARM_SERVICE);
			aMan.setRepeating(AlarmManager.RTC_WAKEUP, calStart.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000,
					alarmIntent);
		}

		public void onReminderClicked(View view)
		{
			//Is the toggle on?
			boolean on = ((Switch) view).isChecked();

			if (on)
			{
				reminderSet = 1;
			} else
			{
				reminderSet = 0;
			}
		}

		@SuppressLint("ValidFragment")
		private class TimePickerFragment extends DialogFragment
				implements TimePickerDialog.OnTimeSetListener
			{

				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState)
				{
					int hour = 8;
					int minute = 0;

					// Create a new instance of TimePickerDialog and return it
					return new TimePickerDialog(getActivity(), this, hour, minute,
							DateFormat.is24HourFormat(getActivity()));
				}

				public void onTimeSet(TimePicker view, int hourOfDay, int minute)
				{


					switch (timeType)
					{
						case 1:
							calStart = Calendar.getInstance();
							calStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
							calStart.set(Calendar.MINUTE, minute);
							if (DateFormat.is24HourFormat(getActivity()))
							{
								startTimeTV.setText(DateFormat.format("HH:mm", calStart));
							} else
							{
								startTimeTV.setText(DateFormat.format("hh:mm aa", calStart));
							}
							break;
						case 2:
							calEnd = Calendar.getInstance();
							calEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
							calEnd.set(Calendar.MINUTE, minute);
							if (DateFormat.is24HourFormat(getActivity()))
							{
								endTimeTV.setText(DateFormat.format("HH:mm", calEnd));
							} else
							{
								endTimeTV.setText(DateFormat.format("hh:mm aa", calEnd));
							}
							break;
						default:
							break;
					}

					timeType = 0;
				}

			}
	}
