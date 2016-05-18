package com.myapp.suresh.tasktimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class PersonDatabaseHelper {

    private static final String TAG = PersonDatabaseHelper.class.getSimpleName();

    // database configuration
    // if you want the onUpgrade to run then change the database_version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";

    // table configuration
    private static final String TABLE_NAME = "emp_table";         // Table name
    private static final String PERSON_TABLE_COLUMN_ID = "_id";     // a column named "_id" is required for cursor
    public static final String PERSON_TABLE_COLUMN_NAME = "person_name";
    private static final String PERSON_TABLE_COLUMN_PIN = "person_pin";
    private static final String PERSON_TABLE_COLUMN_PASS = "person_pass";
    private static final String FTS_VIRTUAL_TABLE = "Info";

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    // this is a wrapper class. that means, from outside world, anyone will communicate with PersonDatabaseHelper,
    // but under the hood actually DatabaseOpenHelper class will perform database CRUD operations
    public PersonDatabaseHelper(Context aContext) {

        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public void insertData (String aPersonName, String aPersonPin, String aPersonPass) {

        // we are using ContentValues to avoid sql format errors

        ContentValues contentValues = new ContentValues();

        contentValues.put(PERSON_TABLE_COLUMN_NAME, aPersonName);
        contentValues.put(PERSON_TABLE_COLUMN_PIN, aPersonPin);
        contentValues.put(PERSON_TABLE_COLUMN_PASS, aPersonPass);

        database.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData () {

        String buildSQL = "SELECT * FROM " + TABLE_NAME;

        Log.d(TAG, "getAllData SQL: " + buildSQL);

        return database.rawQuery(buildSQL, null);
    }
    // this DatabaseOpenHelper class will actually be used to perform database related operation

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // Create your tables here

            String buildSQL = "CREATE VIRTUAL TABLE " + TABLE_NAME + " USING fts3( " + PERSON_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    PERSON_TABLE_COLUMN_NAME + " TEXT, " + PERSON_TABLE_COLUMN_PIN + " TEXT, " + PERSON_TABLE_COLUMN_PASS + " TEXT, )";

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

    public boolean Login(String empId, String empPass) throws SQLException
    {
        Cursor mCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE person_pin=? AND person_pass=?", new String[]{empId,empPass});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }
}