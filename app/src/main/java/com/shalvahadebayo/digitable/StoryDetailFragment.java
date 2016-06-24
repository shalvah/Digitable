package com.shalvahadebayo.digitable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Story detail screen.
 * This fragment is either contained in a {@link StoryListActivity}
 * in two-pane mode (on tablets) or a {@link StoryDetailActivity}
 * on handsets.
 */
public class StoryDetailFragment extends Fragment
	{
		public static final String ARG_STORY_ID = "story_id";
		/**
		 * The fragment argument representing the item ID that this fragment
		 * represents.
		 */
		Cursor cursor;

		/**
		 * The dummy content this fragment is presenting.
		 */
		/**
		 * Mandatory empty constructor for the fragment manager to instantiate the
		 * fragment (e.g. upon screen orientation changes).
		 */
		public StoryDetailFragment()
		{
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(ARG_STORY_ID))
			{
				Activity activity = this.getActivity();

				String idString = getArguments().getString(ARG_STORY_ID, "1");
				Log.d("MERTY", idString);
				cursor = activity.getContentResolver().query(DataProvider.CONTENT_URI4, new
								String[]{StoryTable.COLUMN_ID, StoryTable.COLUMN_TITLE, StoryTable
								.COLUMN_LINK, StoryTable.COLUMN_CONTENT},
						StoryTable.COLUMN_ID + "=?",
						new String[]{idString},
						null);
				cursor.moveToFirst();

				if (!cursor.getString(cursor.getColumnIndex(StoryTable.COLUMN_LINK)).equals("local"))
				{
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(cursor.getString(cursor.getColumnIndex(StoryTable.COLUMN_LINK))));
					startActivity(i);
					activity.finish();
				}


				CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
				if (appBarLayout != null)
				{
					appBarLayout.setTitle(cursor.getString(cursor.getColumnIndex(StoryTable.COLUMN_TITLE)));
				}
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.story_detail, container, false);


			((TextView) rootView.findViewById(R.id.story_detail)).setText(cursor.getString(cursor
					.getColumnIndex(StoryTable.COLUMN_CONTENT)));


			return rootView;
		}
	}
