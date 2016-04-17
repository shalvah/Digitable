package com.shalvahadebayo.digitable;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * An activity representing a list of Assignments. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AssignmentDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AssignmentListActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener
	{

		private ListView lv;
		private SimpleCursorAdapter sca;
		/**
		 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
		 * device.
		 */
		private boolean mTwoPane;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_assignment_list);

			setupActionBarNavDrawerFab();

			if (findViewById(R.id.assignment_detail_container) != null)
			{
				// The detail container view will be present only in the
				// large-screen layouts (res/values-w900dp).
				// If this view is present, then the
				// activity should be in two-pane mode.
				mTwoPane = true;
			}

			//populate the list
			lv = (ListView) findViewById(android.R.id.list);
			fillData();


			lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					if (mTwoPane)
					{
						Bundle arguments = new Bundle();
						arguments.putString(AssignmentDetailFragment.ARG_ITEM_ID, "" + (id));
						AssignmentDetailFragment fragment = new AssignmentDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.assignment_detail_container, fragment)
								.commit();
					} else
					{

						Context context = view.getContext();
						Intent intent = new Intent(context, AssignmentDetailActivity.class);
						intent.putExtra(AssignmentDetailFragment.ARG_ITEM_ID, "" + (id));

						context.startActivity(intent);
					}
				}
			});

		}

		private void setupActionBarNavDrawerFab()
		{
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			assert toolbar != null;
			toolbar.setTitle("Assignments");

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
			navigationView.setCheckedItem(R.id.nav_projects);

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
		}


		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{

			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.menu_asignment_list, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			int id = item.getItemId();
			switch (id)
			{
				case android.R.id.home:
					// This ID represents the Home or Up button. In the case of this
					// activity, the Up button is shown. Use NavUtils to allow users
					// to navigate up one level in the application structure. For
					// more details, see the Navigation pattern on Android Design:
					//
					// http://developer.android.com/design/patterns/navigation.html#up-vs-back
					//
					NavUtils.navigateUpFromSameTask(this);
					return true;
				case R.id.clear_all:
					getContentResolver().delete(AssignmentProvider.CONTENT_URI, null, null);
					break;
				case R.id.new_assignment:
					Intent intent = new Intent(this, AddAssignmentActivity.class);
					startActivity(intent);
					break;
			}
			return super.onOptionsItemSelected(item);
		}

		private void fillData()
		{
			getLoaderManager().initLoader(0, null, this);

			sca = new SimpleCursorAdapter(this, R.layout.assignment_list_item, null,
					new String[]{AssignmentTable.COLUMN_TITLE}, new int[]
					{R.id.titleText}, 0);
			lv.setAdapter(sca);

		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection = {AssignmentTable.COLUMN_ID, AssignmentTable.COLUMN_TITLE,
					AssignmentTable.COLUMN_DESCRIPTION, AssignmentTable.COLUMN_DEADLINE_DATE,
					AssignmentTable.COLUMN_DEADLINE_TIME, AssignmentTable.COLUMN_PRIORITY, AssignmentTable.COLUMN_REMINDER_SET};

			return new CursorLoader(this, AssignmentProvider.CONTENT_URI, projection, null, null,
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


		@SuppressWarnings("StatementWithEmptyBody")
		@Override
		public boolean onNavigationItemSelected(MenuItem item)
		{
			// Handle navigation view item clicks here.
			int id = item.getItemId();

			if (id == R.id.nav_timetable)
			{
				// Handle the camera action
			} else if (id == R.id.nav_home)
			{
				Intent alIntent = new Intent(this, HomeActivity.class);
				startActivity(alIntent);

			} else if (id == R.id.nav_courses)
			{
				Intent alIntent = new Intent(this, CourseListActivity.class);
				startActivity(alIntent);

			} else if (id == R.id.nav_settings)
			{
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);

			} else if (id == R.id.nav_profile)
			{
				Intent alIntent = new Intent(this, AssignmentListActivity.class);
				startActivity(alIntent);

			} else if (id == R.id.nav_about)
			{
				Intent boutIntent = new Intent(this, AboutActivity.class);
				startActivity(boutIntent);

			} else if (id == R.id.nav_share)
			{

			} else if (id == R.id.nav_send)
			{

			}

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}


	}
