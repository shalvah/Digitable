package com.shalvahadebayo.digitable;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimetableActivity extends AppCompatActivity implements NavigationView
		.OnNavigationItemSelectedListener
	{
		ExpandableListView elv;
		List<String> listDataHeader;
		HashMap<String, List<String>> listDataChild;
		MyExpandableListAdapter listAdapter;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_timetable);
			setupActionBarNavDrawerFab();

			elv = (ExpandableListView) findViewById(android.R.id.list);
			fillData();
			elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
				{
					@Override
					public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
					{
						parent.expandGroup(groupPosition);
						return false;
					}
				});
			listAdapter = new MyExpandableListAdapter(this, listDataHeader,
					listDataChild);
			elv.setAdapter(listAdapter);
		}

		@Override
		protected void onResume()
		{
			super.onResume();
			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setCheckedItem(R.id.nav_timetable);

		}

		private void setupActionBarNavDrawerFab()
		{
			//set up action bar
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			setTitle("Timetable");

			//set up fab
			FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
			assert fab != null;
			fab.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Intent i = new Intent(getApplicationContext(), AddClassActivity.class);
						startActivity(i);
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
			navigationView.setCheckedItem(R.id.nav_timetable);

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
				Intent i = new Intent(this, HomeActivity.class);
				startActivity(i);
			}
		}

		private void fillData()
		{
			//populate the listView

			listDataHeader = new ArrayList<String>(); //string array for weekday names
			listDataChild = new HashMap<String, List<String>>();//mapping each weekday to a string array
			// for classes

			listDataHeader.add("Monday");
			listDataHeader.add("Tuesday");
			listDataHeader.add("Wednesday");
			listDataHeader.add("Thursday");
			listDataHeader.add("Friday");
			listDataHeader.add("Saturday");
			listDataHeader.add("Sunday");

			List<String> courses = new ArrayList<String>();
			for (int i = 0; i < listDataHeader.size(); i++)
			{
				Cursor c;

				//get courses for that day
				Uri mUri = DataProvider.CONTENT_URI3;
				c = getContentResolver().query(
						mUri,
						new String[]{ClassTable.COLUMN_ID, ClassTable
								.COLUMN_TIME_START, ClassTable.COLUMN_TIME_END, ClassTable.COLUMN_COURSE},
						ClassTable.COLUMN_WEEKDAY + "=?",
						new String[]{(listDataHeader
								.get(i))},
						null);

				if (c.moveToFirst())
				{
					courses.clear();
					courses.add(c.getString(c.getColumnIndex(ClassTable.COLUMN_COURSE)));
					courses.add("Hiiio");
				}
				while (c.moveToNext())
				{
					courses.add(c.getString(c.getColumnIndex(ClassTable.COLUMN_COURSE)));
				}
				listDataChild.put(listDataHeader.get(i), courses);
			}

		}

		public boolean onNavigationItemSelected(MenuItem item)
		{
			// Handle navigation view item clicks here.
			int id = item.getItemId();

			switch (id)
			{
				case R.id.nav_timetable:
					Intent i = new Intent(this, TimetableActivity.class);
					startActivity(i);
					break;
				case R.id.nav_projects:
				{
					Intent alIntent = new Intent(this, AssignmentListActivity.class);
					startActivity(alIntent);

					break;
				}
				case R.id.nav_home:
				{
					Intent alIntent = new Intent(this, HomeActivity.class);
					startActivity(alIntent);
					break;
				}

				case R.id.nav_reader:
				{
					Intent alIntent = new Intent(this, StoryListActivity.class);
					startActivity(alIntent);
					break;
				}
				case R.id.nav_courses:
					Intent in = new Intent(this, CourseListActivity.class);
					startActivity(in);
					break;
				case R.id.nav_about:
					Intent boutIntent = new Intent(this, AboutActivity.class);
					startActivity(boutIntent);
					break;
			}

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}
	}
