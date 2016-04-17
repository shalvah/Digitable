package com.shalvahadebayo.digitable;

import android.content.Intent;
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

public class HomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener
	{

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_home);
			setupActionBaNavDrawerFab();
		}

		private void setupActionBaNavDrawerFab()
		{
			//set up action bar
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			setTitle("Home");

			//set up fab
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
				case R.id.nav_timetable:
					//launch timetable here
					break;
				case R.id.nav_courses:
					Intent clIntent = new Intent(this, CourseListActivity.class);
					startActivity(clIntent);
					break;
				case R.id.nav_projects:
					Intent alIntent = new Intent(this, AssignmentListActivity.class);
					startActivity(alIntent);
					break;
				case R.id.nav_settings:
					Intent settingsIntent = new Intent(this, SettingsActivity.class);
					startActivity(settingsIntent);
					break;
				case R.id.nav_profile:
					//launch profile here
					break;
				case R.id.nav_about:
					Intent aboutIntent = new Intent(this, AboutActivity.class);
					startActivity(aboutIntent);
					break;
				case R.id.nav_share:
					break;
				case R.id.nav_send:
					break;
			}

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}
	}
