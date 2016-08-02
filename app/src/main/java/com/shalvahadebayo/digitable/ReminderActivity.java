package com.shalvahadebayo.digitable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity
	{
		public static final String ASSIGNMENT_TITLE = "assignment_title";
		public static final String TYPE_ASSIGNMENT_CLASS = "assignment_1_class_2";
		public static String ASSIGNMENT_DESC = "assignment_desc";
		public static String CLASS_COURSE = "class_course";
		public static String CLASS_START_TIME = "class_start_time";
		int time[] = new int[2];
		int date[] = new int[3];

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_reminder);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				actionBar.hide();
			}

			Uri uri = DataProvider.CONTENT_URI;
			Cursor cursor = getContentResolver()
					.query(
							uri,
							new String[]{AssignmentTable.COLUMN_ID, AssignmentTable.COLUMN_TITLE, AssignmentTable
									.COLUMN_DESCRIPTION, AssignmentTable.COLUMN_DEADLINE_DATE, AssignmentTable
									.COLUMN_DEADLINE_TIME, AssignmentTable.COLUMN_PRIORITY, AssignmentTable.COLUMN_REMINDER_SET},
							AssignmentTable.COLUMN_DESCRIPTION + "=?",
							new String[]{getIntent().getStringExtra(ASSIGNMENT_DESC)},
							null);
			if (cursor.moveToFirst())
			{
				((TextView) findViewById(R.id.ass_title)).setText(cursor.getString(cursor.getColumnIndex
						(AssignmentTable.COLUMN_TITLE)));
				((TextView) findViewById(R.id.ass_desc)).setText(cursor.getString(cursor.getColumnIndex
						(AssignmentTable.COLUMN_DESCRIPTION)));
				((TextView) findViewById(R.id.deadline)).setText(cursor.getString(cursor.getColumnIndex
						(AssignmentTable.COLUMN_DEADLINE_DATE)) + " at " + cursor.getString(cursor.getColumnIndex
						(AssignmentTable.COLUMN_DEADLINE_TIME)));
			}
			Button snoozeBtn;
			snoozeBtn = (Button) findViewById(R.id.snooze);
			Button dismissBtn;
			dismissBtn = (Button) findViewById(R.id.dismiss);

			assert snoozeBtn != null;
			snoozeBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(System.currentTimeMillis());
						time[0] = cal.get(Calendar.HOUR_OF_DAY);
						time[1] = cal.get(Calendar.MINUTE) + 5;
						date[0] = cal.get(Calendar.DAY_OF_MONTH);
						date[1] = cal.get(Calendar.MONTH);
						date[2] = cal.get(Calendar.YEAR);
						setAlarm(date, time);
						Toast.makeText(getBaseContext(), "Alarm snoozed for 5 minutes!", Toast.LENGTH_SHORT)
								.show();
						finish();
					}
				});
			assert dismissBtn != null;
			dismissBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Toast.makeText(getBaseContext(), "Alarm Dismissed!", Toast.LENGTH_SHORT).show();
						finish();
					}
				});
		}

		public void setAlarm(int[] date, int[] time)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, time[0]);
			cal.set(Calendar.MINUTE, time[1]);
			cal.set(Calendar.DAY_OF_MONTH, date[0]);
			cal.set(Calendar.MONTH, date[1]);
			cal.set(Calendar.YEAR, date[2]);

			Intent alIntent = new Intent(this, AlarmReceiver.class);
			alIntent.putExtra(ReminderActivity.ASSIGNMENT_DESC, ((TextView) findViewById(R.id.ass_desc)).getText());
			PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, alIntent, 0);

			AlarmManager aMan = (AlarmManager) this.getSystemService(ALARM_SERVICE);
			aMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
		}

	}

