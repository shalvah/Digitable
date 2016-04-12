package com.shalvahadebayo.digitable;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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
		.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener {

	ListView lv;
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private SimpleCursorAdapter sca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());


		if (findViewById(R.id.assignment_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
		lv = (ListView) findViewById(android.R.id.list);
		fillData();

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setCheckedItem(R.id.nav_projects);

	}

	/*@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent mIntent = new Intent(getBaseContext(), AssignmentDetailActivity.class);
				startActivity(mIntent);
			}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu_asignment_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if (id == R.id.clear_all) {
			getContentResolver().delete(AssignmentProvider.CONTENT_URI, null, null);
		} else if (id == R.id.new_assignment) {
			Intent intent = new Intent(this, AddAssignmentActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private void fillData() {
		getLoaderManager().initLoader(0, null, this);

		sca = new SimpleCursorAdapter(this, R.layout.assignment_list_item, null,
				new String[]{MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_DESCRIPTION}, new int[]
				{R.id.titleText, R.id.descText}, 0);
		lv.setAdapter(sca);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper
				.COLUMN_DESCRIPTION};
		CursorLoader cl = new CursorLoader(this, AssignmentProvider.CONTENT_URI, projection, null, null,
				null);
		return cl;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		sca.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		sca.swapCursor(null);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}


	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_timetable) {
			// Handle the camera action
		} else if (id == R.id.nav_home) {
			Intent alIntent = new Intent(this, HomeActivity.class);
			startActivity(alIntent);

		} else if (id == R.id.nav_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);

		} else if (id == R.id.nav_profile) {
			Intent alIntent = new Intent(this, AssignmentListActivity.class);
			startActivity(alIntent);

		} else if (id == R.id.nav_about) {
			Intent boutIntent = new Intent(this, AboutActivity.class);
			startActivity(boutIntent);

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void delete(View view) {

	}
}
