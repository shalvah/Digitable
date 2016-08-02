package com.shalvahadebayo.digitable;

/**
 * Created by Shalvah on 16/04/2016
 * Holds constants for the 'ASSIGNMENTS' table in the SQLite db
 */
public class AssignmentTable
	{
		//table name
		public static final String TABLE_ASSIGNMENTS = "assignments";

		//fields
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_DEADLINE_DATE = "deadline_date";
		public static final String COLUMN_DEADLINE_TIME = "deadline_time";
		public static final String COLUMN_PRIORITY = "priority";
		public static final String COLUMN_REMINDER_SET = "reminder";
		public static final String COLUMN_REMINDER_DATE = "reminder_date";
		public static final String COLUMN_REMINDER_TIME = "reminder_time";

		/**
		 * sql db table creation statement
		 */
		public static final String CREATE_TABLE_ASSIGNMENTS =
				"CREATE TABLE " + TABLE_ASSIGNMENTS + "("
						+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ COLUMN_TITLE + " TEXT NOT NULL, "
						+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
						+ COLUMN_DEADLINE_DATE + " TEXT, "
						+ COLUMN_DEADLINE_TIME + " TEXT, "
						+ COLUMN_PRIORITY + " INTEGER NOT NULL, "
						+ COLUMN_REMINDER_SET + " INTEGER NOT NULL);";


	}
