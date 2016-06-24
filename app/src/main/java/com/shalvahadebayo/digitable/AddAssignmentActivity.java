package com.shalvahadebayo.digitable;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddAssignmentActivity extends AppCompatActivity
	{

		LinearLayout mLinearLayout;
		TextView noDeadline;
		TextView timeText;
		TextView dateText;
		Switch remSwitch;
		int[] date = new int[3]; //dd mm yyyy
		int[] time = new int[2]; //hh mm
		EditText titleET;
		EditText descET;

		long priority = 1;
		int reminderSet = 0;

		String idString = "";
		String title;
		String desc;
		String deadDate;
		String deadTime;
		boolean editing;
		private boolean reminderDateTime;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_assignment);
			setTitle("New Assignment");
			editing = (getIntent().getExtras() != null) ? true : false;

			setupActionBar();
			remSwitch = (Switch) findViewById(R.id.reminderSwitch);

			titleET = (EditText) findViewById(R.id.assignmentTitleET);
			descET = (EditText) findViewById(R.id.assignmentDescET);
			descET.addTextChangedListener(new TextWatcher()
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
						Button doneButton = (Button) findViewById(R.id.assignmentDoneBtn);
						if (!descET.getText().toString().equals("") && !titleET.getText().toString().equals(""))
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
			titleET.addTextChangedListener(new TextWatcher()
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
						Button doneButton = (Button) findViewById(R.id.assignmentDoneBtn);
						if (!descET.getText().toString().equals("") && !titleET.getText().toString().equals(""))
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

			mLinearLayout = (LinearLayout) findViewById(R.id.deadlineField);
			noDeadline = (TextView) findViewById(R.id.deadlineNull);
			noDeadline.setOnClickListener(new View.OnClickListener()
				{
					/*if the user taps on the "no deadline" field, it is replaced with date, time, and cancel
			* options
			*/
					@Override
					public void onClick(View view)
					{
						noDeadline.setVisibility(View.GONE);

						dateText = (TextView) findViewById(R.id.dateText);
						dateText.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									DialogFragment dFrag = new DatePickerFragment();
									dFrag.show(getFragmentManager(), "datePicker");
								}
							});
						dateText.setVisibility(View.VISIBLE);
						dateText.addTextChangedListener(new TextWatcher()
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
									if ((!dateText.getText().toString().equals("Date")) && (!timeText.getText().toString().equals("Time")))
										remSwitch.setEnabled(true);
								}
							});

						timeText = (TextView) findViewById(R.id.timeText);
						timeText.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									DialogFragment tFrag = new TimePickerFragment();
									tFrag.show(getFragmentManager(), "timePicker");

								}
							});
						timeText.addTextChangedListener(new TextWatcher()
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
									if ((!dateText.getText().toString().equals("Date")) && (!timeText.getText().toString().equals("Time")))
										remSwitch.setEnabled(true);
								}
							});
						timeText.setVisibility(View.VISIBLE);

						ImageButton cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);
						cancelBtn.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									timeText.setText("Time");
									dateText.setText("Date");
									timeText.setVisibility(View.GONE);
									dateText.setVisibility(View.GONE);
									v.setVisibility(View.GONE);
									noDeadline.setVisibility(View.VISIBLE);
									remSwitch.setEnabled(false);
								}
							});
						cancelBtn.setVisibility(View.VISIBLE);
					}
				});

			//setup spinner for user to set assignment priority
			Spinner spinner = (Spinner) findViewById(R.id.priority_spinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
					R.array.priority_values, R.layout.spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setSelection((int) priority);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						priority = parent.getItemIdAtPosition(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});


			if (editing)
			{
				setTitle("Edit Assignment");
				idString = getIntent().getStringExtra(AssignmentDetailFragment.ARG_ITEM_ID);
				Uri uri = DataProvider.CONTENT_URI;
				Cursor cursor = getContentResolver()
						.query(
								uri,
								new String[]{AssignmentTable.COLUMN_ID, AssignmentTable.COLUMN_TITLE, AssignmentTable
										.COLUMN_DESCRIPTION, AssignmentTable.COLUMN_DEADLINE_DATE, AssignmentTable
										.COLUMN_DEADLINE_TIME, AssignmentTable.COLUMN_PRIORITY, AssignmentTable.COLUMN_REMINDER_SET},
								AssignmentTable.COLUMN_ID + "=?",
								new String[]{idString},
								null);
				cursor.moveToFirst();


				title = cursor.getString(cursor.getColumnIndex(AssignmentTable.COLUMN_TITLE));
				desc = cursor.getString(cursor.getColumnIndex(AssignmentTable.COLUMN_DESCRIPTION));
				deadDate = cursor.getString(cursor.getColumnIndex(AssignmentTable
						.COLUMN_DEADLINE_DATE));
				deadTime = cursor.getString(cursor.getColumnIndex(AssignmentTable
						.COLUMN_DEADLINE_TIME));
				priority = cursor.getInt(cursor.getColumnIndex(AssignmentTable.COLUMN_PRIORITY));
				reminderSet = cursor.getInt(cursor.getColumnIndex(AssignmentTable.COLUMN_REMINDER_SET));

				titleET.setText(title);
				descET.setText(desc);
				Button doneButton = (Button) findViewById(R.id.assignmentDoneBtn);

				doneButton.setEnabled(true);
				doneButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));


				remSwitch.setChecked((reminderSet != 0));
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

		public void assignmentDone(View v)
		{
			title = titleET.getText().toString();
			desc = descET.getText().toString();

			if (title.length() == 0 || desc.length() == 0)
			{
				Toast.makeText(getBaseContext(), "Please add assignment details!", Toast.LENGTH_SHORT).show();
				return;
			}

			ContentValues values = new ContentValues();
			try
			{
				values.put(AssignmentTable.COLUMN_TITLE, title);
				values.put(AssignmentTable.COLUMN_DESCRIPTION, desc);
				values.put(AssignmentTable.COLUMN_PRIORITY, priority);
				values.put(AssignmentTable.COLUMN_REMINDER_SET, reminderSet);
				if ((!dateText.getText().toString().equals("Date")) && (!timeText.getText().toString().equals("Time")))
				{
					values.put(AssignmentTable.COLUMN_DEADLINE_DATE, dateText.getText().toString());
					values.put(AssignmentTable.COLUMN_DEADLINE_TIME, timeText.getText().toString());
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			/*setup alarm if reminder was switched on
			 */
			if (reminderSet == 1)
				setAlarm();

			if (editing)
				getContentResolver().delete(Uri.parse(DataProvider.CONTENT_URI + "/" + idString), null,
						null);
			getContentResolver().insert(DataProvider.CONTENT_URI, values);
			Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_SHORT).show();

			Intent alIntent = new Intent(this, AssignmentListActivity.class);
			startActivity(alIntent);

		}

		public void setAlarm()
		{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, time[0]);
			cal.set(Calendar.MINUTE, time[1]);
			cal.set(Calendar.DAY_OF_MONTH, date[0] - 1);
			cal.set(Calendar.MONTH, date[1]);
			cal.set(Calendar.YEAR, date[2]);

			Intent alIntent = new Intent(this, AlarmReceiver.class);
			alIntent.putExtra(ReminderActivity.ASSIGNMENT_DESC, desc);
			alIntent.putExtra(ReminderActivity.ASSIGNMENT_TITLE, title);
			alIntent.putExtra(ReminderActivity.TYPE_ASSIGNMENT_CLASS, "assignment");
			PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, alIntent, 0);

			AlarmManager aMan = (AlarmManager) this.getSystemService(ALARM_SERVICE);
			aMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
		}

		public void assignmentReset(View v)
		{
			titleET = (EditText) findViewById(R.id.assignmentTitleET);
			descET = (EditText) findViewById(R.id.assignmentDescET);
			titleET.setText("");
			descET.setText("");
			timeText.setText("Time");
			dateText.setText("Date");

		}

		public void onToggleClicked(View view)
		{
			//Is the toggle on?
			boolean on = ((Switch) view).isChecked();

			if (on)
			{
				reminderDateTime = true;

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
					time[0] = hourOfDay;
					time[1] = minute;
					if (DateFormat.is24HourFormat(getActivity()))
					{
						timeText.setText(DateFormat.format("HH:mm", cal));
					} else
					{
						timeText.setText(DateFormat.format("KK:mm aa", cal));
					}
				}
			}


		@SuppressLint("ValidFragment")
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

					return new DatePickerDialog(getActivity(), this, year, month, day);
				}

				public void onDateSet(DatePicker view, int year, int month, int day)
				{
					if (reminderDateTime)
					{
						Calendar cal = Calendar.getInstance();
						cal.set(year, month, day);
						date[0] = day;
						date[1] = month;
						date[2] = year;
						//dateText.setText(DateFormat.format("dd MMM yyyy", calStart));
					} else
					{
						Calendar cal = Calendar.getInstance();
						cal.set(year, month, day);
						date[0] = day;
						date[1] = month;
						date[2] = year;
						dateText.setText(DateFormat.format("dd MMM yyyy", cal));
					}
				}
			}
	}

