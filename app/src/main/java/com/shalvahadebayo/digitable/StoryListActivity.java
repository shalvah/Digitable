package com.shalvahadebayo.digitable;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * An activity representing a list of Stories. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StoryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StoryListActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener
	{

		private static final String PREF_USER_STUDY_FIELD = "pref_user_study_field";
		private static final String PREFERENCES_FILE = "pref_file";
		private static final String PREF_READER_FIRST_TIME_AFTER_SETUP =
				"reader_first_time_after_setup";
		public static String PREF_NO_OF_INTERESTS = "no_of_interests";
		public static String PREF_NO_OF_STORIES = "no_of_stories";
		public static String PREF_INTEREST = "interest_";
		public static String PREF_READER_FIRST_TIME = "reader_first_time";
		public int noOfStories;
		public boolean isReaderFirstTime;
		String[] url;
		String KEY = "AIzaSyDOYVNdHkK3DpBb43hYC21oFhZEE48vYYQ";
		List<String> qList = new ArrayList<>();
		String[] queryCompanion = {
				"", "%20history", "%20introduction", "%20interesting%20articles", "%20overview"
		};
		List<String> urlList = new ArrayList<>();
		ConnectivityManager connMgr;
		NetworkInfo networkInfo;
		CoordinatorLayout clView;
		private SListMSCA sca;
		/**
		 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
		 * device.
		 */
		private boolean mTwoPane;
		private boolean isReaderFirstTimeAfterSetup;
		private ListView lv;
		private TextView loadMoreTV;

		public static String readSharedSetting(Context ctx, String settingName, String defaultValue)
		{
			SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context
					.MODE_PRIVATE);
			return sharedPref.getString(settingName, defaultValue);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			checkUserFirstTime();

			setContentView(R.layout.activity_story_list);

			setupActionBarNavDrawer();
			if (findViewById(R.id.story_detail_container) != null)
			{
				// The detail container view will be present only in the
				// large-screen layouts (res/values-w900dp).
				// If this view is present, then the
				// activity should be in two-pane mode.
				mTwoPane = true;
			}

			//populate the list
			lv = (ListView) findViewById(android.R.id.list);
			loadMoreTV = (TextView) findViewById(R.id.load_more);
			clView = (CoordinatorLayout) findViewById(R.id.clview);
			setupLV();

			if (this.isReaderFirstTimeAfterSetup)
			{
				setupQueries();
				url = getUrlStrings();
				fetchStories();
			}


		}

		public void updateStoryList(View v)
		{
			setupQueries();
			url = getUrlStrings();
			fetchStories();
		}

		private void setupQueries()
		{
			qList.add(String.valueOf(readSharedSetting(getBaseContext(),
					HomeActivity.PREF_USER_STUDY_FIELD,
					"medicine")));

			int interests = Integer.valueOf(HomeActivity.readSharedSetting(getBaseContext(),
					PREF_NO_OF_INTERESTS,
					"1"));
			for (int i = 0; i < interests - 1; i++)
			{
				qList.add(String.valueOf(HomeActivity.readSharedSetting(getBaseContext(),
						StoryListActivity.PREF_INTEREST + i,
						"engineering")));
			}
		}

		private void checkUserFirstTime()
		{
			//setup if user first time
			getSharedPreferences(PREFERENCES_FILE, 0);
			this.isReaderFirstTime = Boolean.valueOf(readSharedSetting(getBaseContext(),
					PREF_READER_FIRST_TIME,
					"true"));
			this.isReaderFirstTimeAfterSetup = Boolean.valueOf(readSharedSetting
					(getBaseContext(), PREF_READER_FIRST_TIME_AFTER_SETUP,
							"true"));
			if (this.isReaderFirstTime)
			{
				Intent setupIntent = new Intent(this, SetupReaderActivity.class);
				setupIntent.putExtra(PREF_READER_FIRST_TIME, isReaderFirstTime);
				startActivity(setupIntent);
				finish();
			}
		}

		@NonNull
		private String[] getUrlStrings()
		{
			for (int i = 0; i < qList.size(); i++)
			{
				urlList.add("https://www.googleapis.com/customsearch/v1?key="
						+ KEY + "&cx=014732760366713101305:-sm18gfpks4&q=" + qList.get(i) + getRandomObject
						(queryCompanion) + "&alt=json");
			}
			String[] url = new String[(urlList.size())];
			urlList.toArray(url);
			urlList.clear();
			return url;
		}

		private void setupActionBarNavDrawer()
		{
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			setTitle("Reader");


			// Show the Up button in the action bar.
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				actionBar.setDisplayHomeAsUpEnabled(true);
			}

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
			navigationView.setCheckedItem(R.id.nav_reader);


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
					deleteStories(this);
					break;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		protected void onResume()
		{
			super.onResume();
			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setCheckedItem(R.id.nav_reader);

		}

		@Override
		public boolean onNavigationItemSelected(MenuItem item)
		{
			// Handle navigation view item clicks here.
			int id = item.getItemId();

			switch (id)
			{

				case R.id.nav_home:
					Intent hIntent = new Intent(this, HomeActivity.class);
					startActivity(hIntent);
					break;
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

		public boolean deleteStories(Context context)
		{
			context.getContentResolver().delete(DataProvider.CONTENT_URI4,
					null,
					null);
			HomeActivity.saveSharedSetting(getBaseContext(), StoryListActivity
					.PREF_NO_OF_STORIES, "1");
			/*((Integer.valueOf(HomeActivity.readSharedSetting
					(getBaseContext(),
							StoryListActivity.PREF_NO_OF_STORIES, "0"))) -
					ids.length));*/
			return true;
		}

		public Object getRandomObject(Object[] objects)
		{
			return objects[(int) (objects.length * Math.random())];
		}

		public int getRandomObject(int[] objects)
		{
			return objects[(int) (objects.length * Math.random())];
		}


		public boolean fetchStories()
		{

			loadMoreTV.setText("Loading...");
			connMgr = (ConnectivityManager)
					getSystemService(Context.CONNECTIVITY_SERVICE);
			networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected())
			{
				new UpdateStories().execute(url);
				return true;
			} else
			{
				Snackbar.make(clView, "Connect to the Internet for more interesting content", Snackbar
						.LENGTH_LONG)
						.setAction("SETTINGS", new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Intent in = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
									startActivity(in);
								}
							}).show();
				return false;
			}
		}

		@Override
		protected void onStop()
		{
			HomeActivity.saveSharedSetting(getBaseContext(), StoryListActivity
					.PREF_READER_FIRST_TIME_AFTER_SETUP, "false");
			super.onStop();
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection = {StoryTable.COLUMN_ID, StoryTable.COLUMN_TITLE,
					StoryTable.COLUMN_LINK, StoryTable.COLUMN_CONTENT};

			return new CursorLoader(this, DataProvider.CONTENT_URI4, projection, null, null,
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			loadMoreTV.setText("Tap to load more articles from the internet");
			sca.swapCursor(data);

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader)
		{
			sca.swapCursor(null);
			loadMoreTV.setText("Tap to load more articles from the internet");

		}

		public AlertDialog confirmDelete(final Uri uri, final String where, final String[]
				selectionArgs)
		{
			return new AlertDialog.Builder(getBaseContext())
					.setMessage("Are you sure you want to delete all stories?")
					.setTitle("All Stories")
					.setIcon(android.R.drawable.ic_menu_delete)
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
		}

		private void setupLV()
		{
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id)
					{
						if (mTwoPane)
						{
							Bundle arguments = new Bundle();
							arguments.putString(StoryDetailFragment.ARG_STORY_ID, "" + id);
							StoryDetailFragment fragment = new StoryDetailFragment();
							fragment.setArguments(arguments);
							getSupportFragmentManager().beginTransaction()
									.replace(R.id.story_detail_container, fragment)
									.commit();
						} else
						{
							Intent intent = new Intent(getApplication(), StoryDetailActivity.class);
							intent.putExtra(StoryDetailFragment.ARG_STORY_ID, "" + id);
							startActivity(intent);
						}
					}
				});

			getLoaderManager().initLoader(0, null, this);

			sca = new SListMSCA(this, null,
					new String[]{StoryTable.COLUMN_TITLE, StoryTable
							.COLUMN_CONTENT}, new int[]
					{R.id.story_title, R.id.story_content}, 0);
			lv.setAdapter(sca);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{

			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.menu_asignment_list, menu);
			return true;
		}

		private class UpdateStories extends AsyncTask<String, Void, String>
			{

				protected String doInBackground(String... urls)
				{

					// params comes from the execute() call: params[0] is the url.
					try
					{
						for (int i = 0; i < qList.size(); i++)
							downloadUrl(urls[i]);

						return null;
					} catch (IOException | JSONException e)
					{
						e.printStackTrace();
					}
					return "Unable to retrieve web page. URL may be invalid.";

				}

				private void downloadUrl(String url) throws IOException, JSONException
				{
					InputStream is = null;

					try
					{
						URL mUrl = new URL(url);
						HttpURLConnection conn;
						conn = (HttpURLConnection) mUrl.openConnection();
						conn.setReadTimeout(20000 /* milliseconds */);
						conn.setConnectTimeout(30000 /* milliseconds */);
						conn.setRequestMethod("GET");
						conn.setRequestProperty("Accept", "application/json");

						// Starts the query
						conn.connect();
						is = conn.getInputStream();

						// Convert the InputStream into a string
						String contentAsString = readIt(is);

						String link;
						String title;
						String snippet;

						JSONObject obj = new JSONObject(contentAsString);
						JSONArray output = obj.getJSONArray("items");
						for (int i = 1; i < 4; i++)
						{
							JSONObject entry = output.getJSONObject(i);
							link = entry.getString("link");
							title = entry.getString("title");
							snippet = entry.getString("snippet");


							ContentValues contentValues = new ContentValues();
							contentValues.put(StoryTable.COLUMN_CONTENT, snippet);
							contentValues.put(StoryTable.COLUMN_TITLE, title);
							contentValues.put(StoryTable.COLUMN_LINK, link);
							getContentResolver().insert(DataProvider.CONTENT_URI4, contentValues);
							noOfStories++;
						}
						HomeActivity.saveSharedSetting(getBaseContext(), StoryListActivity
								.PREF_NO_OF_STORIES, "" + ((Integer.valueOf(HomeActivity.readSharedSetting
								(getBaseContext(),
										StoryListActivity.PREF_NO_OF_STORIES, "1"))) +
								noOfStories));
						conn.disconnect();
					} catch (IOException e)
					{
						e.printStackTrace();
					} finally
					{
						if (is != null)
						{
							is.close();
						}

					}
				}

				// Reads an InputStream and converts it to a String.
				public String readIt(InputStream stream) throws
						IOException
				{
					Scanner s = new Scanner(stream, "UTF-8").useDelimiter("\\A");
					return s.hasNext() ? s.next() : "";
				}
			}

		class SListMSCA extends MySimpleCursorAdapter
			{
				public SListMSCA(Context context, Cursor c, String[] from, int[] to, int flags)
				{
					super(context, R.layout.story_list_content, c, from, to, flags);
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent)
				{
					View v = super.getView(position, convertView, parent);

					int[] imgs = new int[]{R.drawable.circle_blue, R.drawable.circle_black, R.drawable
							.circle_red, R.drawable
							.circle_green, R.drawable
							.circle_grey};

					ImageView storyImg = (ImageView) v.findViewById(R.id.story_img);
					storyImg.setImageDrawable(getResources().getDrawable(getRandomObject(imgs)));
					return v;
				}
			}
	}

