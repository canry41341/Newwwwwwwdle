package com.example.newwwdle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private final static int _DBVersion = 1;    // DB version
    private final static String _DBName = "ClassInfo.db";   // DB name
    private final static String _TableName = "MyClass";     // table name

    public DBHelper(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "("
                + "_id TEXT NOT NULL, "
                + "cname TEXT NOT NULL, "
                + "ctime TEXT NOT NULL, "
                + "cpos TEXT NOT NULL, "
                + "PRIMARY KEY (_id)"
                + ")";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL = "DROP TABLE " + _TableName;
        db.execSQL(SQL);
    }
}
