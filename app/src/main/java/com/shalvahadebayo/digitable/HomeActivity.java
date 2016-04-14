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

		//set up action bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		setTitle("Home");

		//set up fab
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view) {
				Intent addAssignmentIntent = new Intent(getBaseContext(), AddAssignmentActivity.class);
				startActivity(addAssignmentIntent);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
			}
		});

		//set up nav drawer
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setCheckedItem(R.id.nav_home);

	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

		if (id == R.id.nav_timetable)
		{
			//launch timetable here
		} else if (id == R.id.nav_projects)
		{
			Intent alIntent = new Intent(this, AssignmentListActivity.class);
			startActivity(alIntent);

		} else if (id == R.id.nav_settings)
		{
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);

		} else if (id == R.id.nav_profile)
		{
			//launch profile here
		} else if (id == R.id.nav_about)
		{
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);

		} else if (id == R.id.nav_share)
		{

		} else if (id == R.id.nav_send)
		{

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
