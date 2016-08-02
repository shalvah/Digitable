package com.shalvahadebayo.digitable;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Shalvah on 17/06/2016.
 * A custom cursor adapter for a ListView
 */
class MySimpleCursorAdapter extends SimpleCursorAdapter
	{

		public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
		{
			super(context, layout, c, from, to, flags);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			return super.getView(position, convertView, parent);
		}
	}
