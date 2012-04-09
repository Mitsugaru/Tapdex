package com.mitsugaru.Tapdex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * @author Tokume
 *
 */
//http://developer.android.com/guide/topics/data/data-storage.html#db
public class DatabaseHandler extends SQLiteOpenHelper
{
	//Class variables
	public static final String DATABASE_NAME = "tapdex";
	public static final int DATABASE_VERSION = 1;
	
	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub
		
	}
	
	public boolean tableExists(String tableName)
	{
		return this.tableExists(tableName, true);
	}
	
	/**
	 * Source: http://stackoverflow.com/questions/3058909/how-does-one-check-if-a-table-exists-in-an-android-sqlite-database
	 * 
	 * @param Table name to search for
	 * @param attempt to open database
	 * @return true if table exists, else false
	 */
	public boolean tableExists(String tableName, boolean openDb) {
		SQLiteDatabase db = null;
	    if(openDb) {
	        if(db == null || !db.isOpen()) {
	        	db = getReadableDatabase();
	        }

	        if(!db.isReadOnly()) {
	        	db.close();
	        	db = getReadableDatabase();
	        }
	    }

	    Cursor cursor = db.rawQuery("select DISTINCT * from " + DATABASE_NAME +" where tbl_name = '"+tableName+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	        	cursor.close();
	        	db.close();
	            return true;
	        }
	    }
	    db.close();
	    return false;
	}
}
