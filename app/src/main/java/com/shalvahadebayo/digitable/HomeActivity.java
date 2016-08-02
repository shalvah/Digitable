package com.shalvahadebayo.digitable;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity
		implements LoaderManager
		.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener
	{
		public static final String PREF_USER_FIRST_TIME = "pref_user_first_time";
		public static final String PREFERENCES_FILE = "pref_file";
		public static String PREF_USER_STUDY_FIELD = "pref_user_study_field";
		final int[] storyCardViews = {R.id.reader_card_view_1, R.id.reader_card_view_2};
		final int[] storyTitleViews = {R.id.reader_title_1, R.id.reader_title_2};
		final int[] storyContentViews = {R.id.reader_content_1, R.id.reader_content_2};
		public boolean isUserFirstTime;
		private int id1;
		private int id2;

		public static String readSharedSetting(Context ctx, String settingName, String defaultValue)
		{
			SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
			return sharedPref.getString(settingName, defaultValue);
		}

		public static void saveSharedSetting(Context ctx, String settingName, String settingValue)
		{
			SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(settingName, settingValue);
			editor.apply();
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			getSharedPreferences(PREFERENCES_FILE, 0);
			isUserFirstTime = Boolean.valueOf(readSharedSetting(this, PREF_USER_FIRST_TIME,
					"true"));
			setContentView(R.layout.activity_home);
			setupActionBarNavDrawerFab();

			if (isUserFirstTime)
			{
				Intent introIntent = new Intent(this, WelcomeActivity.class);
				introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);
				startActivity(introIntent);
				finish();
			} else
			{

				getLoaderManager().initLoader(0, null, this);
				try
				{
					fillAssignments();
				} catch (ParseException e)
				{
					e.printStackTrace();
				}
//			fillClasses();

		}

		}

		private void fillAssignments() throws ParseException
		{
			Cursor asstC = getContentResolver().query(DataProvider.CONTENT_URI, new String[]{AssignmentTable
							.COLUMN_ID, AssignmentTable.COLUMN_TITLE,
							AssignmentTable.COLUMN_DESCRIPTION, AssignmentTable.COLUMN_DEADLINE_DATE,
							AssignmentTable.COLUMN_DEADLINE_TIME, AssignmentTable.COLUMN_PRIORITY}, null,
					null,
					null);
			if (asstC.moveToFirst())
			{

				Calendar calCurrent = Calendar.getInstance();
				Calendar calAsst = Calendar.getInstance();
				long curTime = calCurrent.getTimeInMillis();
				long assTime;
				List<Long> diff = new ArrayList<Long>();
				Date asstDate;

				DateFormat df = DateFormat.getDateTimeInstance();

				for (int i = 0; i < asstC.getCount(); i++)
				{

					asstDate = df.parse(String.format("%s %s", asstC.getString(asstC.getColumnIndex
							(AssignmentTable
									.COLUMN_DEADLINE_DATE)), asstC.getString(asstC.getColumnIndex
							(AssignmentTable.COLUMN_DEADLINE_TIME))));

					calAsst.setTime(asstDate);
					assTime = calAsst.getTimeInMillis();
					diff.add(assTime - curTime);


				}

				long min = diff.get(0);
				for (long l : diff)
				{
					min = ((min < l) ? min : l);
				}
				final int id = diff.indexOf(min);

				CardView asst = (CardView) findViewById(R.id.upcomiing_card_view_2);

				assert asst != null;
				asst.setVisibility(View.VISIBLE);
				asst.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Intent intent = new Intent(getBaseContext(), AssignmentDetailActivity.class);
							intent.putExtra(AssignmentDetailFragment.ARG_ITEM_ID, "" + id);
							startActivity(intent);
						}
					});

				TextView asstTitleText = (TextView) findViewById(R.id.upcomiing_title_2);
				assert asstTitleText != null;
				asstTitleText.setText(asstC.getString(asstC.getColumnIndex(AssignmentTable.COLUMN_TITLE)));
				TextView asstContentText = (TextView) findViewById(R.id.upcomiing_content_2);
				assert asstContentText != null;
				asstContentText.setText(asstC.getString(asstC.getColumnIndex(AssignmentTable
						.COLUMN_DESCRIPTION)));
			/*TextView asstDeadlineText = (TextView) findViewById(storyContentViews[i]);
			assert asstContentText != null;
			asstContentText.setText(asstC.getString(asstC.getColumnIndex(AssignmentTable
					.COLUMN_DESCRIPTION)));*/

				TextView upcomingSectionHeader = (TextView) findViewById(R.id.upcomiing_text);
				assert upcomingSectionHeader != null;
				upcomingSectionHeader.setVisibility(View.VISIBLE);

			}
		}

		@Override
		protected void onResume()
		{
			super.onResume();
			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setCheckedItem(R.id.nav_home);

		}

		private void setupActionBarNavDrawerFab()
		{
			//set up action bar
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			setTitle("Home");

			FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
			assert fab != null;
			fab.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Intent addAssignmentIntent = new Intent(getBaseContext(), AddAssignmentActivity.class);
						startActivity(addAssignmentIntent);
					}
				});

			//set up nav drawer
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
					this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
			assert drawer != null;
			//noinspection deprecation,deprecation
			drawer.setDrawerListener(toggle);
			toggle.syncState();
			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setNavigationItemSelectedListener(this);
			navigationView.setCheckedItem(R.id.nav_home);
			if (isUserFirstTime)
			{
				drawer.openDrawer(GravityCompat.START);
			}


		}

		@Override
		public void onBackPressed()
		{
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			if (drawer.isDrawerOpen(GravityCompat.START))
			{
				drawer.closeDrawer(GravityCompat.START);
			} else
			{
				super.onBackPressed();
			}
		}

		@SuppressWarnings("StatementWithEmptyBody")
		@Override
		public boolean onNavigationItemSelected(MenuItem item)
		{
			// Handle navigation view item clicks here.
			int id = item.getItemId();

			switch (id)
			{

				case R.id.nav_courses:
					Intent clIntent = new Intent(this, CourseListActivity.class);
					startActivity(clIntent);
					break;
				case R.id.nav_projects:
					Intent alIntent = new Intent(this, AssignmentListActivity.class);
					startActivity(alIntent);
					break;

				case R.id.nav_reader:
					Intent readIntent = new Intent(this, StoryListActivity.class);
					startActivity(readIntent);
					break;

				case R.id.nav_about:
					Intent aboutIntent = new Intent(this, AboutActivity.class);
					startActivity(aboutIntent);
					break;
				case R.id.nav_timetable:
					Intent i = new Intent(this, TimetableActivity.class);
					startActivity(i);
					break;
			}

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			int noOfStories = Integer.valueOf(HomeActivity.readSharedSetting(getBaseContext(), StoryListActivity
					.PREF_NO_OF_STORIES, "1"));
			String[] projection = {StoryTable.COLUMN_ID, StoryTable.COLUMN_TITLE,
					StoryTable.COLUMN_LINK, StoryTable.COLUMN_CONTENT};


			id1 = ((int) (noOfStories * Math.random())) % noOfStories;

			id2 = ((int) (noOfStories * Math.random())) % noOfStories;

			if (id1 == id2)
				return new CursorLoader(this, DataProvider.CONTENT_URI4, projection, StoryTable.COLUMN_ID +
						"=?",
						new String[]{"" + id1},
						null);
			else
			{

				return new CursorLoader(this, DataProvider.CONTENT_URI4, projection, StoryTable.COLUMN_ID +
						"=?",
						new String[]{"" + id1},
						null);
			}
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			if (data.moveToFirst())
			{
				int i = 0;

				{
					CardView story = (CardView) findViewById(storyCardViews[i]);

					assert story != null;
					story.setVisibility(View.VISIBLE);
					story.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Intent intent = new Intent(getBaseContext(), StoryDetailActivity.class);
								intent.putExtra(StoryDetailFragment.ARG_STORY_ID, "" + id1);
								startActivity(intent);
							}
						});

					TextView storyTitleText = (TextView) findViewById(storyTitleViews[i]);
					assert storyTitleText != null;
					storyTitleText.setText(data.getString(data.getColumnIndex(StoryTable.COLUMN_TITLE)));
					TextView storyContentText = (TextView) findViewById(storyContentViews[i]);
					assert storyContentText != null;
					storyContentText.setText(data.getString(data.getColumnIndex(StoryTable
							.COLUMN_CONTENT)));
				}
			}

			if (id1 != id2)
			{
				Cursor c = getContentResolver().query(DataProvider.CONTENT_URI4, new String[]{StoryTable.COLUMN_ID, StoryTable.COLUMN_TITLE,
								StoryTable.COLUMN_LINK, StoryTable.COLUMN_CONTENT}, StoryTable
								.COLUMN_ID +
								"=?",
						new String[]{"" + id2},
						null);

				c.moveToFirst();
				CardView story = (CardView) findViewById(storyCardViews[1]);

				assert story != null;
				story.setVisibility(View.VISIBLE);
				story.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Intent intent = new Intent(getBaseContext(), StoryDetailActivity.class);
							intent.putExtra(StoryDetailFragment.ARG_STORY_ID, "" + id2);
							startActivity(intent);
						}
					});

				try
				{
					TextView storyTitleText = (TextView) findViewById(storyTitleViews[1]);
					assert storyTitleText != null;
					storyTitleText.setText(c.getString(c.getColumnIndex(StoryTable.COLUMN_TITLE)));
					TextView storyContentText = (TextView) findViewById(storyContentViews[1]);
					assert storyContentText != null;
					storyContentText.setText(c.getString(c.getColumnIndex(StoryTable
							.COLUMN_CONTENT)));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			TextView storySectionHeader = (TextView) findViewById(R.id.reader_text);
			assert storySectionHeader != null;
			storySectionHeader.setVisibility(View.VISIBLE);

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader)
		{

		}
	}
