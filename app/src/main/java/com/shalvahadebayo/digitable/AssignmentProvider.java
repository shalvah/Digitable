package com.shalvahadebayo.digitable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class AssignmentProvider extends ContentProvider {
	public static final String PROVIDER_NAME = "com.shalvahadebayo.provider.assignment";
	public static final String BASE_PATH = "assignments";
	public static final String URL = "content://" + PROVIDER_NAME + "/" + BASE_PATH;
	public static final Uri CONTENT_URI = Uri.parse(URL);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/assignments";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
			"/assignment";
	/**
	 * for UriMatcher
	 */
	public static final int ASSIGNMENTS = 1;
	public static final int ASSIGNMENT_ID = 2;
	private static final UriMatcher aUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static SQLiteDatabase db;

	static {
		aUriMatcher.addURI(PROVIDER_NAME, BASE_PATH, ASSIGNMENTS);
		aUriMatcher.addURI(PROVIDER_NAME, BASE_PATH + "/#", ASSIGNMENT_ID);
	}

	private MySQLiteHelper mySQLiteHelper;

	public AssignmentProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		int uriType = aUriMatcher.match(uri);
		SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
		int rowsDeleted;

		switch (uriType)

		{
			case ASSIGNMENTS:
				rowsDeleted = db.delete(MySQLiteHelper.TABLE_ASSIGNMENTS, selection, selectionArgs);
				break;
			case ASSIGNMENT_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(MySQLiteHelper.TABLE_ASSIGNMENTS, MySQLiteHelper.COLUMN_ID + "="
							+ id, null);
				} else {
					rowsDeleted = db.delete(MySQLiteHelper.TABLE_ASSIGNMENTS, MySQLiteHelper.COLUMN_ID + "="
							+ id + " and " + selection, null);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = aUriMatcher.match(uri);
		SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
		long id;

		switch (uriType) {
			case ASSIGNMENTS:
				id = db.insert(MySQLiteHelper.TABLE_ASSIGNMENTS, null, values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		mySQLiteHelper = new MySQLiteHelper(getContext());
		db = mySQLiteHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	                    String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qBuild = new SQLiteQueryBuilder();

		checkColumns(projection);

		qBuild.setTables(MySQLiteHelper.TABLE_ASSIGNMENTS);

		int uriType = aUriMatcher.match(uri);


		switch (uriType) {
			case ASSIGNMENTS:
				break;
			case ASSIGNMENT_ID:
				qBuild.appendWhere(MySQLiteHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		/*if (sortOrder==null || sortOrder=="")
		{
			sortOrder=MySQLiteHelper.COLUMN_ID;
		}*/

		Cursor cursor = qBuild.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
	                  String[] selectionArgs) {
		int uriType = aUriMatcher.match(uri);
		SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
		int rowsUpdated = 0;

		switch (uriType) {
			case ASSIGNMENTS:
				rowsUpdated = db.update(MySQLiteHelper.TABLE_ASSIGNMENTS, values, selection, selectionArgs);
				break;
			case ASSIGNMENT_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(MySQLiteHelper.TABLE_ASSIGNMENTS, values, MySQLiteHelper
							.COLUMN_ID + "="
							+ id, null);
				} else {
					rowsUpdated = db.update(MySQLiteHelper.TABLE_ASSIGNMENTS, values, MySQLiteHelper
							.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper
				.COLUMN_DESCRIPTION, MySQLiteHelper.COLUMN_DEADLINE_DATE, MySQLiteHelper.COLUMN_DEADLINE_TIME};
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}

		}

	}


}
