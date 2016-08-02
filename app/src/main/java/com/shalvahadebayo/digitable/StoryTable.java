package com.shalvahadebayo.digitable;

/**
 * Created by Shalvah on 08/06/2016.
 * Holds constants for the 'STORIES' table in the SQLite db
 */
public class StoryTable
	{
		//table name
		public static final String TABLE_STORIES = "stories";

		//fields
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_LINK = "link";
		public static final String COLUMN_CONTENT = "content";

		/**
		 * sql db creation statement
		 */
		public static final String CREATE_TABLE_STORIES =
				"CREATE TABLE " + TABLE_STORIES + "("
						+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ COLUMN_TITLE + " TEXT NOT NULL, "
						+ COLUMN_LINK + " TEXT NOT NULL, "
						+ COLUMN_CONTENT + " TEXT"
						+ ");";


	}
