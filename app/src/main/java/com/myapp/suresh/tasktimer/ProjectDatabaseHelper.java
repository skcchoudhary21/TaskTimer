package com.myapp.suresh.tasktimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class ProjectDatabaseHelper {

    private static final String TAG = PersonDatabaseHelper.class.getSimpleName();

    // database configuration
    // if you want the onUpgrade to run then change the database_version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";

    // table configuration
    private static final String TABLE_NAME = "person_table";         // Table name
    private static final String PERSON_TABLE_COLUMN_ID = "_id";     // a column named "_id" is required for cursor
    public static final String PERSON_TABLE_COLUMN_NAME = "person_name";
    private static final String PERSON_TABLE_COLUMN_PIN = "person_pin";
    private static final String FTS_VIRTUAL_TABLE = "Info";
    private static final String PERSON_TABLE_ADATE ="a_date";
    private static final String PERSON_TABLE_COLUMN_APROJECT ="a_project" ;
    private static final String PERSON_TABLE_COLUMN_ATASK ="a_task" ;
    private static final String PERSON_TABLE_COLUMN_ATIMETAKEN ="a_timetaken" ;
    private static final String PERSON_TABLE_AEMPID = "a_empid";
    private static final String PERSON_TABLE_COLUMN_ADATE = "a_date";

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    // this is a wrapper class. that means, from outside world, anyone will communicate with PersonDatabaseHelper,
    // but under the hood actually DatabaseOpenHelper class will perform database CRUD operations
    public ProjectDatabaseHelper(Context aContext) {

        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public void insertData (String aDate, String aEmpId, String aProject, String aTask, String aTimeTaken) {

        // we are using ContentValues to avoid sql format errors

        ContentValues contentValues = new ContentValues();

        contentValues.put(PERSON_TABLE_AEMPID, aEmpId);
        contentValues.put(PERSON_TABLE_COLUMN_ADATE, aDate);
        contentValues.put(PERSON_TABLE_COLUMN_APROJECT, aProject);
        contentValues.put(PERSON_TABLE_COLUMN_ATASK, aTask);
        contentValues.put(PERSON_TABLE_COLUMN_ATIMETAKEN, aTimeTaken);
       // contentValues.put(PERSON_TABLE_COLUMN_PIN, aTime);


        database.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData () {

        String buildSQL = "SELECT * FROM " + TABLE_NAME;

        Log.d(TAG, "getAllData SQL: " + buildSQL);

        return database.rawQuery(buildSQL, null);
    }

    public boolean deleteAllNames() {
        int doneDelete = database.delete(TABLE_NAME, null, null);
        return doneDelete > 0;
    }
    public Cursor searchByInputText(String inputText) throws SQLException {

        String query = "SELECT docid as _id," +
                PERSON_TABLE_COLUMN_NAME +  " from " + TABLE_NAME +
                " where " + PERSON_TABLE_COLUMN_NAME + " MATCH '" + inputText + "';";

        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    // this DatabaseOpenHelper class will actually be used to perform database related operation

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // Create your tables here

            String buildSQL = "CREATE VIRTUAL TABLE " + TABLE_NAME + " USING fts3( " + PERSON_TABLE_AEMPID+ " INTEGER PRIMARY KEY, " +
                    PERSON_TABLE_COLUMN_ADATE + " TEXT, "+PERSON_TABLE_COLUMN_APROJECT + " TEXT, "+PERSON_TABLE_COLUMN_ATASK + " TEXT, " + PERSON_TABLE_COLUMN_ATIMETAKEN + " TEXT )";

            Log.d(TAG, "onCreate SQL: " + buildSQL);

            sqLiteDatabase.execSQL(buildSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // Database schema upgrade code goes here

            String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

            Log.d(TAG, "onUpgrade SQL: " + buildSQL);

            sqLiteDatabase.execSQL(buildSQL);       // drop previous table

            onCreate(sqLiteDatabase);               // create the table from the beginning
        }
    }
}