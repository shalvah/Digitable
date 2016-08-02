package com.shalvahadebayo.digitable;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * An activity representing a list of Courses. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CourseDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CourseListActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener
	{
		private ListView lv;
		private TextView emptyView;
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
			setContentView(R.layout.activity_course_list);

			setupActionBarNavDrawerFab();
			emptyView = (TextView) findViewById(android.R.id.empty);
			emptyView.setText(R.string.courses_empty);

			//populate the list
			lv = (ListView) findViewById(android.R.id.list);
			fillData();

			if (findViewById(R.id.course_detail_container) != null)
			{
				// The detail container view will be present only in the
				// large-screen layouts (res/values-w900dp).
				// If this view is present, then the
				// activity should be in two-pane mode.
				mTwoPane = true;
			}


			lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					if (mTwoPane)
					{
						Bundle arguments = new Bundle();
						arguments.putString(CourseDetailFragment.ARG_ITEM_ID, "" + (id));
						CourseDetailFragment fragment = new CourseDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.course_detail_container, fragment)
								.commit();
					} else
					{
						Context context = view.getContext();
						Intent intent = new Intent(context, CourseDetailActivity.class);
						intent.putExtra(CourseDetailFragment.ARG_ITEM_ID, "" + (id));
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
			toolbar.setTitle(getTitle());

			FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
			assert fab != null;
			fab.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Intent i = new Intent(getBaseContext(), AddCourseActivity.class);
					startActivity(i);

					/*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
			*/
				}
			});
			// Show the Up button in the action bar.
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
					this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
			assert drawer != null;
			//noinspection deprecation,deprecation,deprecation,deprecation,deprecation
			drawer.setDrawerListener(toggle);
			toggle.syncState();

			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setNavigationItemSelectedListener(this);
			navigationView.setCheckedItem(R.id.nav_courses);
		}

		private void fillData()
		{
			getLoaderManager().initLoader(0, null, this);

			sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
					new String[]{CourseTable.COLUMN_COURSE_CODE, CourseTable.COLUMN_COURSE_TITLE}, new int[]
					{android.R.id.text1, android.R.id.text2}, 0);
			lv.setAdapter(sca);

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
					AlertDialog dialog = confirmDelete(DataProvider.CONTENT_URI, null, null);
					dialog.show();
					return true;

			}
			return super.onOptionsItemSelected(item);
		}


		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection = {CourseTable.COLUMN_ID, CourseTable.COLUMN_COURSE_CODE,
					CourseTable.COLUMN_COURSE_TITLE, CourseTable.COLUMN_UNITS};

			return new CursorLoader(this, DataProvider.CONTENT_URI2, projection, null, null,
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			sca.swapCursor(data);
			if (data.getCount() == 0)
				emptyView.setVisibility(View.VISIBLE); //// TODO: 06/06/2016 make sure this works IMMEDIATELY AFTER DELETION
			else
				emptyView.setVisibility(View.GONE);

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
				NavUtils.navigateUpFromSameTask(this);
			}
		}

		public boolean onCreateOptionsMenu(Menu menu)
		{

			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.menu_asignment_list, menu);
			return true;
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

		@Override
		protected void onResume()
		{
			super.onResume();
			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setCheckedItem(R.id.nav_courses);

		}

		public AlertDialog confirmDelete(final Uri uri, final String where, final String[]
				selectionArgs)
		{
			AlertDialog deleteConfirmation = new AlertDialog.Builder(getBaseContext())
					.setMessage("Are you sure you want to delete all assignments?")
					.setTitle("All Assignments")
					.setPositiveButton("Delete", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								getContentResolver().delete(uri, where, selectionArgs);
							}
						})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						})
					.create();
			return deleteConfirmation;
		}
	}
