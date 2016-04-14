package com.shalvahadebayo.digitable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A fragment representing a single Assignment detail screen.
 * This fragment is either contained in a {@link AssignmentListActivity}
 * in two-pane mode (on tablets) or a {@link AssignmentDetailActivity}
 * on handsets.
 */
public class AssignmentDetailFragment extends Fragment {
	public static final String ARG_ITEM_ID = "item_id";
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public Cursor cursor;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AssignmentDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.


			Activity activity = this.getActivity();
			Uri uri = AssignmentProvider.CONTENT_URI;
			cursor = activity.getContentResolver()
					.query(
							uri,
							new String[]{MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_DESCRIPTION},
							MySQLiteHelper.COLUMN_ID + "=?",
							new String[]{getArguments().getString(ARG_ITEM_ID, "1")},
							null);
			cursor.moveToFirst();
			CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
			if (appBarLayout != null) {
				appBarLayout.setTitle(cursor.getString(1));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.assignment_detail, container, false);

		((TextView) rootView.findViewById(R.id.assignment_detail)).setText(cursor.getString(2));

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.assignment_context_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.delete)
		{
			delete(cursor.getString(0));
		}
		return super.onOptionsItemSelected(item);
	}

	public void delete(String id)
	{
		Uri uri = Uri.parse(AssignmentProvider.CONTENT_URI + "/" + id);
		getActivity().getContentResolver().delete(uri, null, null);
		Intent alIntent = new Intent(getActivity(), AssignmentListActivity.class);
		startActivity(alIntent);

	}
}
