package com.shalvahadebayo.digitable;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
		public static final String ARG_ITEM_ID = "item_id";

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
				Uri uri = AssignmentProvider.CONTENT_URI2;
				cursor = activity.getContentResolver()
						.query(
								uri,
								new String[]{AssignmentTable.COLUMN_ID, AssignmentTable.COLUMN_TITLE, AssignmentTable
										.COLUMN_DESCRIPTION, AssignmentTable.COLUMN_DEADLINE_DATE, AssignmentTable
										.COLUMN_DEADLINE_TIME, AssignmentTable.COLUMN_PRIORITY, AssignmentTable.COLUMN_REMINDER_SET},
								AssignmentTable.COLUMN_ID + "=?",
								new String[]{getArguments().getString(ARG_ITEM_ID, "1")},
								null);
				cursor.moveToFirst();
				CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
				if (appBarLayout != null)
				{
					appBarLayout.setTitle(cursor.getString(1));
				}
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.course_detail, container, false);

			// Show the dummy content as text in a TextView.
			((TextView) rootView.findViewById(R.id.course_detail)).setText(cursor.getString(1));

			return rootView;
		}
	}
