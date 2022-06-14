package com.example.spredicts;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "predicts.db";
    private static final String TABLE_PREDICT = "tblpredicts";
    private static final int DATABASEVERSION = 1;
    // ?
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HOME_PREDICT = "Home_Predict";
    private static final String COLUMN_AWAY_PREDICT = "Away_predict";

    private static final String[] allColumns = {COLUMN_ID, COLUMN_HOME_PREDICT, COLUMN_AWAY_PREDICT};

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_PREDICT + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_HOME_PREDICT + " INTEGER," +
            COLUMN_AWAY_PREDICT + " INTEGER );";

    private SQLiteDatabase database; // access to table

    public DBHelper(@Nullable Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        Log.d(TAG,CREATE_TABLE_USER);
    }


    // creating the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
            sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    // in case of version upgrade -> new schema
    // database version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PREDICT);
        onCreate(sqLiteDatabase);
    }


    // get the user back with the id
    // also possible to return only the id
    public Predict insert(Predict user)
    {
        database = getWritableDatabase(); // get access to write the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_HOME_PREDICT, user.getHomePredict());
        values.put(COLUMN_AWAY_PREDICT, user.getAwayPredict());

        if (getById((int) user.getId()) == null) {
            database.execSQL("INSERT INTO " + TABLE_PREDICT + " (_id, Home_Predict, Away_predict) VALUES (" + user.getId() + "," + user.getHomePredict() + "," + user.getAwayPredict() + ");");
        }
//        long id = database.insert(TABLE_PREDICT, null, values);
        database.close();
        getById((int) user.getId());
        return user;
    }

    // remove a specific user from the table
    public void deleteUser(com.example.spredicts.Predict user)
    {

    }

    public void deleteById(long id)
    {
        database = getWritableDatabase(); // get access to write e data
        database.delete(TABLE_PREDICT, COLUMN_ID + " = " + id, null);
        database.close(); // close the database

    }


    // update a specific user
    public void update(Predict user)
    {
        try {
            database = getWritableDatabase();
            System.out.println("UPDATE " + TABLE_PREDICT + " SET Home_Predict = " + user.getHomePredict() + ", Away_predict = " + user.getAwayPredict() + " WHERE _id = " + user.getId() + ";");
            database.execSQL("UPDATE " + TABLE_PREDICT + " SET Home_Predict = " + user.getHomePredict() + ", Away_predict = " + user.getAwayPredict() + " WHERE _id = " + user.getId() + ";");
            database.close();
        } catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    // return all rows in table
    public ArrayList<Predict> selectAll()
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<Predict> users = new ArrayList<>();
        String sortOrder = COLUMN_ID + " DESC"; // sorting by id
        Cursor cursor = database.query(TABLE_PREDICT, allColumns, null, null, null, null, sortOrder); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") int home = cursor.getInt(cursor.getColumnIndex(COLUMN_HOME_PREDICT));
                @SuppressLint("Range") int away = cursor.getInt(cursor.getColumnIndex(COLUMN_AWAY_PREDICT));
                Predict user= new Predict(id, home, away);
                users.add(user);
            }
        }
        database.close();
        return users;
    }
    //check if the database file is exist
    public boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return true;
//        return dbFile.exists();
    }

    public Cursor getData(){
        SQLiteDatabase sq = this.getWritableDatabase();
        Cursor cursor = sq.query(TABLE_PREDICT,allColumns,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Predict getById(int id) {
        SQLiteDatabase sq = getReadableDatabase();
        Cursor c = sq.rawQuery("SELECT * FROM tblpredicts WHERE _id = " + id + ";", null);
        c.moveToNext();
        try {
            return new Predict(c.getInt(0), c.getInt(1), c.getInt(2));
        } catch (Exception ignored) {
        }
        return null;
    }

    //
    // I prefer using this one...
    //


}