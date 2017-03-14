package com.example.lex.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lex on 3/6/2017.
 */

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    //constructor
    public DBManager(Context c) {context = c;}

    // opening the database
    public DBManager open() throws SQLException {
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    // closing the database
    public void close() {dbHelper.close();}

    // insert a catagorie in the database
    public void insertcatagorie(String name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.MAINSUBJECT, 1);
        contentValue.put(DatabaseHelper.TODOITEM, "no item");
        contentValue.put(DatabaseHelper.DONE, 0);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        fetch();
    }

    // insert a to do in the database
    public void inserttodo(String parent, String name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, parent);
        contentValue.put(DatabaseHelper.MAINSUBJECT, 0);
        contentValue.put(DatabaseHelper.TODOITEM, name);
        contentValue.put(DatabaseHelper.DONE, 0);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        fetch();
    }

    // get all items
    public Cursor fetch() {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.MAINSUBJECT, DatabaseHelper.TODOITEM, DatabaseHelper.DONE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Log.d("cursor", Integer.toString(cursor.getCount()));
        return cursor;
    }

    // get all categories
    public Cursor fetchcategorie() {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.MAINSUBJECT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, "mainlist = 1", null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Log.d("cursor", Integer.toString(cursor.getCount()));
        return cursor;
    }

    // get al not done to do's
    public Cursor fetchtodo(String parent) {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.MAINSUBJECT, DatabaseHelper.TODOITEM, DatabaseHelper.DONE};
        String getcolumns = "mainlist = 0 AND done = 0 AND list = \"" + parent + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, getcolumns, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Log.d("cursor", Integer.toString(cursor.getCount()));
        return cursor;
    }

    // get all done to do's
    public Cursor fetchdone(String parent) {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.MAINSUBJECT, DatabaseHelper.TODOITEM, DatabaseHelper.DONE};
        String getcolumns = "mainlist = 0 AND done = 1 AND list = \"" + parent + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, getcolumns, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Log.d("cursor", Integer.toString(cursor.getCount()));
        return cursor;
    }

    // delete a categorie
    public void deletecategorie(String listname) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.SUBJECT + "=\"" + listname + "\"", null);
    }

    // delete a to do
    public void deletetodo(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + id, null);
    }

    // check if the categorie already exists
    public Boolean checkcategories(String categorie) {
        String[] columns = new String[] {DatabaseHelper.SUBJECT};
        String getcolumns = "mainlist = 1 AND list = \"" + categorie + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, getcolumns, null, null, null, null);
        if (cursor.getCount() == 0) {
            return true;
        }
        return false;
    }

    // check if the to do already exists
    public Boolean checktodo(String parent, String todo) {
        String[] columns = new String[] {DatabaseHelper.TODOITEM};
        String getcolumns = "mainlist = 0 AND todoitem = \"" + todo + "\"" + " AND list = \"" + parent + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, getcolumns, null, null, null, null);
        if (cursor.getCount() == 0) {
            return true;
        }
        return false;
    }

    // update the database
    public void done(String parent, String todo) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DONE, 1);
        String getcolumns = "mainlist = 0 AND todoitem = \"" + todo + "\"" + " AND list = \"" + parent + "\"";
        database.update(DatabaseHelper.TABLE_NAME, contentValue, getcolumns, null);
    }
}
