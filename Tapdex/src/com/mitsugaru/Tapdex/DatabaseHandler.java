package com.mitsugaru.Tapdex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Tokume
 * 
 */
// http://developer.android.com/guide/topics/data/data-storage.html#db
public class DatabaseHandler extends SQLiteOpenHelper {
    // Class variables
    private static final String DATABASE_NAME = "tapdex.db";
    private static final String DATABASE_PATH = "/data/data/com.mitsugaru.Tapdex/databases/";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase db;
    private static DatabaseHandler database = null;
    public final Context context;

    public DatabaseHandler(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	this.context = context;
	try {
	    createDatabase();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	checkTables();
    }

    public static DatabaseHandler getInstance(Context context) {
	if (database == null) {
	    database = new DatabaseHandler(context);
	    database.openDatabase();

	    if (db == null) {
		try {
		    db = database.getWritableDatabase();
		} catch (Exception e) {
		    Log.i(TapdexActivity.TAG, "Error in database creation");
		}

		database.openDatabase();
	    }
	}
	return database;
    }

    /**
     * http://www.itsalif.info/content/check-if-database-exist-android-
     * sqlite3openv2-failed
     * 
     * @return true if database file exists
     */
    public boolean databaseExist() {
	File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
	return dbFile.exists();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void createDatabase() throws IOException {

	boolean dbExist = databaseExist();

	if (dbExist) {
	    // do nothing - database already exist
	} else {

	    // By calling this method and empty database will be created into
	    // the default system path
	    // of your application so we are gonna be able to overwrite that
	    // database with our database.
	    this.getReadableDatabase();
	}

    }

    public boolean openDatabase() throws SQLException {

	String path = DATABASE_PATH + DATABASE_NAME;
	try {

	    db = SQLiteDatabase.openDatabase(path, null,
		    SQLiteDatabase.OPEN_READWRITE);
	} catch (SQLiteException e) {
	    Log.i(TapdexActivity.TAG, "Error in openDatabase method");
	    // TODO: handle exception
	}
	return db != null ? true : false;
    }

    @Override
    public synchronized void close() {

	if (db != null)
	    db.close();

	super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub

    }

    public boolean tableExists(String tableName) {
	return this.tableExists(tableName, true);
    }

    /**
     * Source:
     * http://stackoverflow.com/questions/3058909/how-does-one-check-if-a
     * -table-exists-in-an-android-sqlite-database
     * 
     * @param Table
     *            name to search for
     * @param attempt
     *            to open database
     * @return true if table exists, else false
     */
    public boolean tableExists(String tableName, boolean openDb) {
	if (openDb) {
	    if (db == null || !db.isOpen()) {
		db = getReadableDatabase();
	    }

	    if (!db.isReadOnly()) {
		close();
		db = getReadableDatabase();
	    }
	}

	try {
	    Cursor cursor = db.rawQuery("select * from " + tableName, null);
	    cursor.close();
	    return true;
	} catch (SQLiteException e) {

	}
	return false;
    }

    public void checkTables() {
	// Grab database
	if (db == null || !db.isOpen()) {
	    db = getWritableDatabase();
	}
	if (db.isReadOnly()) {
	    db.close();
	    db = getWritableDatabase();
	}
	// Check forms database
	if (!tableExists("forms", false)) {
	    db.execSQL("CREATE TABLE forms (id INTEGER PRIMARY KEY, name TEXT NOT NULL, format TEXT NOT NULL, UNIQUE(name));");
	}
	if (!tableExists("entry")) {
	    db.execSQL("CREATE TABLE entry (id INTEGER PRIMARY KEY, formid INTEGER NOT NULL);");
	}
	if (!tableExists("fields", false)) {
	    db.execSQL("CREATE TABLE fields (row INTEGER PRIMARY KEY, entryid INTEGER NOT NULL, fieldKey TEXT, fieldValue TEXT);");
	}
	close();
    }

    public boolean formNameExists(String name) {
	boolean found = false;
	if (db == null || !db.isOpen()) {
	    db = getReadableDatabase();
	}

	if (!db.isReadOnly()) {
	    close();
	    db = getReadableDatabase();
	}
	try {
	    Cursor cursor = db.rawQuery("SELECT * FROM forms", null);
	    if (cursor.moveToFirst()) {
		do {
		    if (name.equals(cursor.getString(cursor
			    .getColumnIndex("name")))) {
			found = true;
		    }
		} while (cursor.moveToNext());
	    }
	    cursor.close();
	} catch (SQLiteException e) {

	}
	close();
	return found;
    }

    public List<String> getFormNames() {
	List<String> list = new ArrayList<String>();
	if (db == null || !db.isOpen()) {
	    db = getReadableDatabase();
	}

	if (!db.isReadOnly()) {
	    close();
	    db = getReadableDatabase();
	}
	try {
	    Cursor cursor = db.rawQuery("SELECT * FROM forms", null);
	    /*
	     * http://stackoverflow.com/questions/4920528/iterate-through-rows-from
	     * -sqlite-query
	     */
	    if (cursor.moveToFirst()) {
		do {
		    list.add(cursor.getString(cursor.getColumnIndex("name")));
		} while (cursor.moveToNext());
	    }
	    cursor.close();
	} catch (SQLiteException e) {

	}
	close();
	return list;
    }

    public enum Field {

	FORMS_ID(Table.FORMS, "id", Type.INTEGER), FORMS_NAME(Table.FORMS,
		"name", Type.STRING), FORMS_FORMAT(Table.FORMS, "format",
		Type.STRING), ENTRY_ID(Table.ENTRY, "id", Type.INTEGER), ENTRY_FORM_ID(
		Table.ENTRY, "formid", Type.INTEGER), FIELDS_ENTRY_ID(
		Table.FIELDS, "entryid", Type.INTEGER), FIELDS_KEY(
		Table.FIELDS, "fieldKey", Type.STRING), FIELDS_VALUE(
		Table.FIELDS, "fieldValue", Type.STRING);

	private final Table table;
	private final String columnname;
	private final Type type;

	private Field(Table table, String columnname, Type type) {
	    this.table = table;
	    this.columnname = columnname;
	    this.type = type;
	}

	public Table getTable() {
	    return table;
	}

	public String getColumnName() {
	    return columnname;
	}

	public Type getType() {
	    return type;
	}
    }

    public enum Type {
	STRING, INTEGER;
    }

    public enum Table {
	FORMS("forms"), ENTRY("entry"), FIELDS("fields");
	private final String table;

	private Table(String table) {
	    this.table = table;
	}

	public String getName() {
	    return table;
	}

	@Override
	public String toString() {
	    return table;
	}
    }
}
