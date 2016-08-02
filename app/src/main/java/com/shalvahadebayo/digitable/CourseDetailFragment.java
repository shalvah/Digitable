package com.shalvahadebayo.digitable;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Course detail screen.
 * This fragment is either contained in a {@link CourseListActivity}
 * in two-pane mode (on tablets) or a {@link CourseDetailActivity}
 * on handsets.
 */
public class CourseDetailFragment extends Fragment
	{
		/**
		 * The fragment argument representing the item ID that this fragment
		 * represents.
		 */
		public static final String ARG_ITEM_ID = "course_id";

		/**
		 * Mandatory empty constructor for the fragment manager to instantiate the
		 * fragment (e.g. upon screen orientation changes).
		 */
		public Cursor cursor;

		public CourseDetailFragment()
		{
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);


			if (getArguments().containsKey(ARG_ITEM_ID))
			{
				// Load the dummy content specified by the fragment
				// arguments. In a real-world scenario, use a Loader
				// to load content from a content provider.


				Activity activity = this.getActivity();
				Uri uri = DataProvider.CONTENT_URI2;
				cursor = activity.getContentResolver()
						.query(
								uri,
								new String[]{CourseTable.COLUMN_ID, CourseTable.COLUMN_COURSE_CODE, CourseTable
										.COLUMN_COURSE_TITLE, CourseTable.COLUMN_UNITS},
								CourseTable.COLUMN_ID + "=?",
								new String[]{getArguments().getString(ARG_ITEM_ID, "1")},
								null);
				cursor.moveToFirst();
				CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
				if (appBarLayout != null)
				{
					appBarLayout.setTitle(cursor.getString(cursor
							.getColumnIndex(CourseTable.COLUMN_COURSE_CODE)));
					Toolbar t = (Toolbar) activity.findViewById(R.id.detail_toolbar);
					t.setSubtitle(cursor.getString(cursor
							.getColumnIndex(CourseTable.COLUMN_COURSE_TITLE)));
				}
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.course_detail, container, false);

			((TextView) rootView.findViewById(R.id.course_title)).setText(cursor.getString(cursor
					.getColumnIndex(CourseTable.COLUMN_COURSE_TITLE)));
			((TextView) rootView.findViewById(R.id.course_units)).setText("" + cursor.getInt(cursor
					.getColumnIndex(CourseTable.COLUMN_UNITS)));

			return rootView;
		}

		public AlertDialog confirmDelete(final Uri uri, final String where, final String[]
				selectionArgs)
		{
			AlertDialog deleteConfirmation = new AlertDialog.Builder(getContext())
					.setMessage("Are you sure you want to delete this course?")
					.setIcon(android.R.drawable.ic_menu_delete)
					.setTitle(cursor.getString(1))
					.setPositiveButton("Delete", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								getActivity().getContentResolver().delete(uri, where, selectionArgs);
								Intent i2 = new Intent(getContext(), CourseListActivity.class);
								startActivity(i2);
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
					NavUtils.navigateUpTo(getActivity(), new Intent(getContext(), AssignmentListActivity.class));
					return true;
				case R.id.edit_assignment:
					Intent i = new Intent(getContext(), AddCourseActivity.class);
					i.putExtra(CourseDetailFragment.ARG_ITEM_ID, getArguments().getString(ARG_ITEM_ID, "1"));
					startActivity(i);
					break;
				case R.id.delete_assignment:
					AlertDialog dialog = confirmDelete(Uri.parse(DataProvider.CONTENT_URI2 + "/" +
									getArguments().getString(CourseDetailFragment.ARG_ITEM_ID, "1")), null,
							null);
					dialog.show();

					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
		{
			inflater.inflate(R.menu.menu_asignment_detail, menu);
		}

	}
