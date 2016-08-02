package com.shalvahadebayo.digitable;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;


public class AlarmReceiver extends WakefulBroadcastReceiver
	{
		String assDesc;


		@Override
		public void onReceive(Context context, Intent intent)
		{
			String course;
			String classStartTime;
			String type;
			String title;
			String content;
			type = intent.getStringExtra(ReminderActivity.TYPE_ASSIGNMENT_CLASS);
			Intent resultIntent;
			NotificationCompat.Builder nb;

			switch (type)
			{
				case "class":
					course = intent.getStringExtra(ReminderActivity.CLASS_COURSE);
					classStartTime = intent.getStringExtra(ReminderActivity.CLASS_START_TIME);
					title = "Class Reminder";
					content = course + " by " + classStartTime;

					resultIntent = new Intent(context, TimetableActivity.class);

					nb = new NotificationCompat.Builder(context)
							.setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
							.setContentTitle(title)
							.setContentText(content)
							.setAutoCancel(true)
							.setVibrate(new long[]{800, 1000, 800});

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
					{
						// The stack builder object will contain an artificial back stack for the
						// started Activity.
						// This ensures that navigating backward from the Activity leads out of
						// your application to the Home screen.
						TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
						// Adds the back stack for the Intent (but not the Intent itself)
						stackBuilder.addParentStack(HomeActivity.class);
						// Adds the Intent that starts the Activity to the top of the stack
						stackBuilder.addNextIntent(resultIntent);

						PendingIntent resultPendingIntent =
								stackBuilder.getPendingIntent(
										0,
										PendingIntent.FLAG_UPDATE_CURRENT
								);

						int mId = 0;
						nb.setContentIntent(resultPendingIntent);

						NotificationManager mNotificationManager =
								(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						//  mId allows you to update the notification later on.
						mNotificationManager.notify(mId, nb.build());
					}

					break;
				case "assignment":
					assDesc = intent.getStringExtra(ReminderActivity.ASSIGNMENT_DESC);
					content = intent.getStringExtra(ReminderActivity.ASSIGNMENT_TITLE);
					title = "Assignment Reminder";


					resultIntent = new Intent(context, ReminderActivity.class);
					resultIntent.putExtra(ReminderActivity.ASSIGNMENT_DESC, assDesc);


					nb = new NotificationCompat.Builder(context)
							.setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
							.setContentTitle(title)
							.setContentText(content)
							.setAutoCancel(true)
							.setVibrate(new long[]{800, 1000, 800});


					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
					{
						// The stack builder object will contain an artificial back stack for the
						// started Activity.
						// This ensures that navigating backward from the Activity leads out of
						// your application to the Home screen.
						TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
						// Adds the back stack for the Intent (but not the Intent itself)
						stackBuilder.addParentStack(AssignmentListActivity.class);
						// Adds the Intent that starts the Activity to the top of the stack

						stackBuilder.addNextIntent(resultIntent);

						PendingIntent resultPendingIntent =
								stackBuilder.getPendingIntent(
										0,
										PendingIntent.FLAG_UPDATE_CURRENT
								);

						int mId = 0;
						nb.setContentIntent(resultPendingIntent);

						NotificationManager mNotificationManager =
								(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						//  mId allows you to update the notification later on.
						mNotificationManager.notify(mId, nb.build());
					}
					break;
			}





		}

		public class SampleBootReceiver extends BroadcastReceiver
			{

				@Override
				public void onReceive(Context context, Intent intent)
				{
					/*
					if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
					{
						// Set the alarm here.
					}*/
				}
			}
	}
