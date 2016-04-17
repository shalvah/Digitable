package com.shalvahadebayo.digitable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper
	{

		private static final String DATABASE_NAME = "digitable.db";
		private static final int DATABASE_VERSION = 2;


		public MySQLiteHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(AssignmentTable.CREATE_TABLE_ASSIGNMENTS);
			db.execSQL(CourseTable.CREATE_TABLE_COURSES);
			db.execSQL(ClassTable.CREATE_TABLE_CLASSES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + AssignmentTable.TABLE_ASSIGNMENTS);
			db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_COURSES);
			db.execSQL("DROP TABLE IF EXISTS " + ClassTable.TABLE_CLASSES);
			onCreate(db);
		}


	}
