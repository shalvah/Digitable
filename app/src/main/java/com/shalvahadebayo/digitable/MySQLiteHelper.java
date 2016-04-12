package com.shalvahadebayo.digitable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shalvah on 29/03/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	//table name
	public static final String TABLE_ASSIGNMENTS = "assignments";

	//fields
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";

	private static final String DATABASE_NAME = "assignments.db";
	private static final int DATABASE_VERSION = 2;

	/**
	 * sql db creation statement
	 */
	private static final String DATABASE_CREATE =
			"CREATE TABLE " + TABLE_ASSIGNMENTS + "(" + COLUMN_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE
					+ " TEXT NOT NULL, " + COLUMN_DESCRIPTION + " TEXT);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
		onCreate(db);
	}
}
