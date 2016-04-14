package com.shalvahadebayo.digitable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddAssignmentActivity extends AppCompatActivity {

	LinearLayout mLinearLayout;
	TextView noDeadline;
	TextView timeText;
	TextView dateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		setTitle("New Assignment");
		setupActionBar();

		//if the user taps on the "no deadline" field, it is replaced with date, time, and cancel
		// options
		mLinearLayout = (LinearLayout) findViewById(R.id.deadlineField);
		noDeadline = (TextView) findViewById(R.id.deadlineNull);
		noDeadline.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				mLinearLayout.removeView(view);

				dateText = new TextView(getBaseContext());
				dateText.setText("Date");
				dateText.setTextColor(Color.BLACK);
				dateText.setClickable(true);
				dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				dateText.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						DialogFragment dFrag = new DatePickerFragment();
						dFrag.show(getFragmentManager(), "datePicker");
					}
				});
				mLinearLayout.addView(dateText, new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));


				timeText = new TextView(getBaseContext());
				timeText.setText("Time");
				timeText.setTextColor(Color.BLACK);
				timeText.setClickable(true);
				timeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				timeText.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						DialogFragment tFrag = new TimePickerFragment();
						tFrag.show(getFragmentManager(), "timePicker");

					}
				});
				mLinearLayout.addView(timeText,
						new LinearLayout.LayoutParams(0, ViewGroup
								.LayoutParams.WRAP_CONTENT, 1.0f));


				ImageButton cancelBtn = new ImageButton(getBaseContext());
				cancelBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
				cancelBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						mLinearLayout.removeAllViews();
						mLinearLayout.addView(noDeadline);
					}
				});
				mLinearLayout.addView(cancelBtn, new ViewGroup.LayoutParams(ViewGroup.LayoutParams
						.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}
		});
	}

	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	public void done(View v) {
		EditText titleET = (EditText) findViewById(R.id.titleET);
		EditText descET = (EditText) findViewById(R.id.descET);
		String title = titleET.getText().toString();
		String desc = descET.getText().toString();

		if (title.length() == 0 && desc.length() == 0) {
			Toast.makeText(getBaseContext(), "Please add assignment details!", Toast.LENGTH_SHORT).show();
			return;
		}

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, title);
		values.put(MySQLiteHelper.COLUMN_DESCRIPTION, desc);
		values.put(MySQLiteHelper.COLUMN_DEADLINE_DATE, dateText.getText().toString());
		values.put(MySQLiteHelper.COLUMN_DEADLINE_TIME, timeText.getText().toString());

		Uri uri = getContentResolver().insert(AssignmentProvider.CONTENT_URI, values);
		Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_SHORT).show();

		Intent alIntent = new Intent(this, AssignmentListActivity.class);
		startActivity(alIntent);

	}

	public void reset(View v) {
		EditText titleET = (EditText) findViewById(R.id.titleET);
		EditText descET = (EditText) findViewById(R.id.descET);
		titleET.setText("");
		descET.setText("");
	}


	private class TimePickerFragment extends DialogFragment
			implements TimePickerDialog.OnTimeSetListener
		{

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState)
			{
				// Use the current time as the default values for the picker
				final Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);

				// Create a new instance of TimePickerDialog and return it
				return new TimePickerDialog(getActivity(), this, hour, minute,
						DateFormat.is24HourFormat(getActivity()));
			}

			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				if (DateFormat.is24HourFormat(getActivity()))
				{
					timeText.setText(DateFormat.format("hh:mm", cal));
				} else
				{
					timeText.setText(DateFormat.format("KK:mm aa", cal));
				}
			}
		}


	private class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener
		{

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState)
			{
				// Use the current date as the default date in the picker
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);

				// Create a new instance of DatePickerDialog and return it
				return new DatePickerDialog(getActivity(), this, year, month, day);
			}

			public void onDateSet(DatePicker view, int year, int month, int day)
			{
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day);
				dateText.setText(DateFormat.format("dd MMM yyyy", cal));

			}
		}
}

