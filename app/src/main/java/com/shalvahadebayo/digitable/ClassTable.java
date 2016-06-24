package com.shalvahadebayo.digitable;

public class ClassTable
	{
		//table name
		public static final String TABLE_CLASSES = "classes";

		//fields
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_WEEKDAY = "weekday";
		public static final String COLUMN_TIME_START = "start";
		public static final String COLUMN_TIME_END = "end";
		public static final String COLUMN_CLASS_REMINDER_SET = "class_reminder_set";
		public static final String COLUMN_COURSE = "course_name";

		/**
		 * sql db creation statement
		 */
		public static final String CREATE_TABLE_CLASSES =
				"CREATE TABLE " + TABLE_CLASSES + "("
						+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ COLUMN_WEEKDAY + " TEXT NOT NULL, "
						+ COLUMN_TIME_START + " TEXT NOT NULL, "
						+ COLUMN_TIME_END + " TEXT NOT NULL, "
						+ COLUMN_CLASS_REMINDER_SET + " INTEGER NOT NULL, "
						+ COLUMN_COURSE + " TEXT NOT NULL, "
						+ "FOREIGN KEY(" + COLUMN_COURSE + ") REFERENCES "
						+ CourseTable.TABLE_COURSES + "(" + CourseTable.COLUMN_COURSE_CODE + "));";


	}
