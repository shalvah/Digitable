package com.shalvahadebayo.digitable;

/**
 * Created by ACER pc on 16/04/2016.
 */
public class ClassTable
	{
		//table name
		public static final String TABLE_CLASSES = "classes";

		//fields
		public static final String COLUMN_ID = "classid";
		public static final String COLUMN_WEEKDAY = "weekday";
		public static final String COLUMN_TIME_START = "start";
		public static final String COLUMN_TIME_END = "end";
		public static final String COLUMN_COURSE = "course";

		/**
		 * sql db creation statement
		 */
		public static final String CREATE_TABLE_CLASSES =
				"CREATE TABLE " + TABLE_CLASSES + "("
						+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ COLUMN_WEEKDAY + " TEXT NOT NULL, "
						+ COLUMN_TIME_START + " TEXT NOT NULL, "
						+ COLUMN_TIME_END + " TEXT NOT NULL, "
						+ COLUMN_COURSE + " INTEGER NOT NULL, "
						+ "FOREIGN KEY(" + COLUMN_COURSE + ") REFERENCES "
						+ CourseTable.TABLE_COURSES + "(" + CourseTable.COLUMN_ID + "));";


	}
