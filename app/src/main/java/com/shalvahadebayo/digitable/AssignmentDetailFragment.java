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
			Uri uri = DataProvider.CONTENT_URI;
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
			if (appBarLayout != null) {
				appBarLayout.setTitle(cursor.getString(1));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.assignment_detail, container, false);

		((TextView) rootView.findViewById(R.id.assignment_description)).setText(cursor.getString(2));

		if (!cursor.getString(3).equals("") && cursor.getString(3) != null)
			((TextView) rootView.findViewById(R.id.assignment_deadline)).setText
					(cursor.getString(3) + " at " + cursor.getString(4));
		TextView priorityText = ((TextView) rootView.findViewById(R.id.assignment_priority));
		switch (cursor.getInt(5))
		{

			case 2:
				priorityText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
				priorityText.setText(getResources().getString(R.string.priority_high));
				break;
			case 0:
				priorityText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
				priorityText.setText(getResources().getString(R.string.priority_low));
				break;
			default:
				priorityText.setTextColor(getResources().getColor(android.R.color.primary_text_light));
				priorityText.setText(getResources().getString(R.string.priority_normal));
				break;
		}

		return rootView;
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
				Intent i = new Intent(getContext(), AddAssignmentActivity.class);
				i.putExtra(AssignmentDetailFragment.ARG_ITEM_ID, getArguments().getString(ARG_ITEM_ID, "1"));
				startActivity(i);
				break;
			case R.id.delete_assignment:
				AlertDialog dialog = confirmDelete(Uri.parse(DataProvider.CONTENT_URI + "/" +
								getArguments().getString(ARG_ITEM_ID, "1")), null,
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


	public AlertDialog confirmDelete(final Uri uri, final String where, final String[]
			selectionArgs)
	{
		AlertDialog deleteConfirmation = new AlertDialog.Builder(getContext())
				.setMessage("Are you sure you want to delete this assignment?")
				.setIcon(android.R.drawable.ic_menu_delete)
				.setTitle(cursor.getString(1))
				.setPositiveButton("Delete", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							getActivity().getContentResolver().delete(uri, where, selectionArgs);
							Intent i2 = new Intent(getContext(), AssignmentListActivity.class);
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
}
