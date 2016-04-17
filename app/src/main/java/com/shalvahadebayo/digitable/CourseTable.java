package com.shalvahadebayo.digitable;

public class CourseTable
	{
		//table name
		public static final String TABLE_COURSES = "courses";

		//fields
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_COURSE_CODE = "code";
		public static final String COLUMN_COURSE_TITLE = "title";
		public static final String COLUMN_UNITS = "units";

		/**
		 * sql db creation statement
		 */
		public static final String CREATE_TABLE_COURSES =
				"CREATE TABLE " + TABLE_COURSES + "("
						+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ COLUMN_COURSE_CODE + " TEXT, "
						+ COLUMN_COURSE_TITLE + " TEXT NOT NULL, "
						+ COLUMN_UNITS + " INTEGER);";


	}

