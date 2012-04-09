package com.mitsugaru.Tapdex;

import android.content.Context;
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
	
}
