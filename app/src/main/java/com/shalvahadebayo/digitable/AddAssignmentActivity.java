package com.shalvahadebayo.digitable;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddAssignmentActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		setTitle("New Assignment");
		setupActionBar();
	}

	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	public void done(View v) {
		EditText titleET = (EditText) findViewById(R.id.titleET);
		EditText descET = (EditText) findViewById(R.id.descET);
		String title = titleET.getText().toString();
		String desc = descET.getText().toString();

		if (title.length() == 0 && desc.length() == 0) {
			Toast.makeText(getBaseContext(), "Please add assignment details!", Toast.LENGTH_SHORT).show();
			return;
		}

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, title);
		values.put(MySQLiteHelper.COLUMN_DESCRIPTION, desc);

		Uri uri = getContentResolver().insert(AssignmentProvider.CONTENT_URI, values);
		Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_SHORT).show();

		Intent alIntent = new Intent(this, AssignmentListActivity.class);
		startActivity(alIntent);

	}

	public void reset(View v) {
		EditText titleET = (EditText) findViewById(R.id.titleET);
		EditText descET = (EditText) findViewById(R.id.descET);
		titleET.setText("");
		descET.setText("");
	}
}
