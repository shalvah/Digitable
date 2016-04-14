package com.shalvahadebayo.digitable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper
	{

		//table name
		public static final String TABLE_ASSIGNMENTS = "assignments";

		//fields
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_DEADLINE_DATE = "deadline_date";
		public static final String COLUMN_DEADLINE_TIME = "deadline_time";

		private static final String DATABASE_NAME = "assignments.db";
		private static final int DATABASE_VERSION = 3;

		/**
		 * sql db creation statement
		 */
		private static final String DATABASE_CREATE =
				"CREATE TABLE " + TABLE_ASSIGNMENTS + "(" + COLUMN_ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE
						+ " TEXT NOT NULL, " + COLUMN_DESCRIPTION
						+ " TEXT NOT NULL, " + COLUMN_DEADLINE_DATE
						+ " TEXT NOT NULL, " + COLUMN_DEADLINE_TIME + " TEXT);";

		public MySQLiteHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
			onCreate(db);
		}


	}
