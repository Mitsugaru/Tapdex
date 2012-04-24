package com.mitsugaru.Tapdex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
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
	if (!tableExists(Table.FORMS.getName(), false)) {
	    db.execSQL("CREATE TABLE "
		    + Table.FORMS.getName()
		    + " (id INTEGER PRIMARY KEY, name TEXT NOT NULL, format TEXT NOT NULL, UNIQUE(name));");
	}
	if (!tableExists(Table.ENTRY.getName())) {
	    db.execSQL("CREATE TABLE "
		    + Table.ENTRY.getName()
		    + " (id INTEGER PRIMARY KEY, formid INTEGER NOT NULL, name TEXT NOT NULL);");
	}
	if (!tableExists(Table.FIELDS.getName(), false)) {
	    db.execSQL("CREATE TABLE "
		    + Table.FIELDS.getName()
		    + " (row INTEGER PRIMARY KEY, entryid INTEGER NOT NULL, fieldKey TEXT NOT NULL, fieldValue TEXT NOT NULL, fieldData TEXT);");
	}
	close();
    }

    public void createForm(String name, String format) {
	if (db == null || !db.isOpen()) {
	    db = getWritableDatabase();
	}
	if (db.isReadOnly()) {
	    db.close();
	    db = getWritableDatabase();
	}
	try {
	    SQLiteStatement statement = db.compileStatement("INSERT INTO "
		    + Table.FORMS.getName() + " (name, format) VALUES(?,?);");
	    statement.bindString(1, name);
	    statement.bindString(2, format);
	    long row = statement.executeInsert();
	    if (row == -1) {
		Log.e(TapdexActivity.TAG, "Could not insert form '" + name
			+ "' of format '" + format + "'");
	    }
	    statement.close();
	    close();
	} catch (SQLException e) {
	    Log.e(TapdexActivity.TAG, "Error on creating form '" + name + "'",
		    e);
	}
    }

    public int getFormId(String form) {
	int id = -1;
	if (db == null || !db.isOpen()) {
	    db = getReadableDatabase();
	}

	if (!db.isReadOnly()) {
	    close();
	    db = getReadableDatabase();
	}
	try {
	    Cursor cursor = db.rawQuery(
		    "SELECT * FROM " + Table.FORMS.getName(), null);
	    if (cursor.moveToFirst()) {
		do {
		    if (form.equals(cursor.getString(cursor
			    .getColumnIndex(Field.FORMS_NAME.getColumnName())))) {
			id = cursor.getInt(cursor.getColumnIndex(Field.FORMS_ID
				.getColumnName()));
		    }
		} while (cursor.moveToNext());
	    }
	    cursor.close();
	} catch (SQLiteException e) {

	}
	close();
	return id;
    }

    public void createEntry(String form, String entryName) {
	int id = getFormId(form);
	if (id == -1) {
	    Log.e(TapdexActivity.TAG, "Missing form '" + form + "'");
	} else {
	    try {
		if (db == null || !db.isOpen()) {
		    db = getWritableDatabase();
		}
		if (db.isReadOnly()) {
		    db.close();
		    db = getWritableDatabase();
		}
		SQLiteStatement statement = db.compileStatement("INSERT INTO "
			+ Table.ENTRY.getName()
			+ " (formid, name) VALUES(?,?);");
		statement.bindLong(1, id);
		statement.bindString(2, entryName);
		long row = statement.executeInsert();
		if (row == -1) {
		    Log.e(TapdexActivity.TAG, "Could not insert entry '"
			    + entryName + "' for form '" + form + "'");
		}
		statement.close();
		close();
	    } catch (SQLException e) {
		Log.e(TapdexActivity.TAG, "Error on creating entry '"
			+ entryName + "'", e);
	    }
	}
    }

    public boolean entryNameExistsForForm(String form, String entry) {
	int id = getFormId(form);
	if (id == -1) {
	    Log.e(TapdexActivity.TAG, "Missing form '" + form + "'");
	} else {
	    int entryId = getEntryId(form, entry);
	    if (entryId != -1) {
		return true;
	    }
	}
	return false;
    }

    public int getEntryId(String form, String entry) {
	int id = -1;
	int formId = getFormId(form);
	if (formId == -1) {
	    return id;
	}
	if (db == null || !db.isOpen()) {
	    db = getReadableDatabase();
	}

	if (!db.isReadOnly()) {
	    close();
	    db = getReadableDatabase();
	}
	try {
	    Cursor cursor = db.rawQuery(
		    "SELECT * FROM " + Table.ENTRY.getName()
			    + " WHERE formid='" + formId + "';", null);
	    if (cursor.moveToFirst()) {
		do {
		    if (entry.equals(cursor.getString(cursor
			    .getColumnIndex(Field.ENTRY_NAME.getColumnName())))) {
			id = cursor.getInt(cursor.getColumnIndex(Field.ENTRY_ID
				.getColumnName()));
		    }
		} while (cursor.moveToNext());
	    }
	    cursor.close();
	} catch (SQLiteException e) {
	    Log.e(TapdexActivity.TAG, "Error on getting entry id of '" + entry
		    + "'", e);
	}
	close();
	return id;
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
	    Cursor cursor = db.rawQuery(
		    "SELECT * FROM " + Table.FORMS.getName() + ";", null);
	    if (cursor.moveToFirst()) {
		do {
		    if (name.equals(cursor.getString(cursor
			    .getColumnIndex(Field.FORMS_NAME.getColumnName())))) {
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

    public void addData(String form, String entry, Map<String, Object> data) {
	int id = getEntryId(form, entry);
	if (id != -1) {
	    try {
		if (db == null || !db.isOpen()) {
		    db = getWritableDatabase();
		}
		if (db.isReadOnly()) {
		    db.close();
		    db = getWritableDatabase();
		}
		SQLiteStatement statement = db
			.compileStatement("INSERT INTO "
				+ Table.FIELDS.getName()
				+ " (entryid, fieldKey, fieldValue, fieldData) VALUES(?,?,?,?);");
		statement.bindLong(1, id);
		String type = (String) data.get("type");
		statement.bindString(2, type);
		if (type.equals("TEXT")) {
		    statement.bindString(3, (String) data.get("text"));
		    statement.bindNull(4);
		} else if (type.equals("RATING")) {
		    final String floatString = ""
			    + ((Float) data.get("value")).floatValue();
		    statement.bindString(3, floatString);
		    statement.bindNull(4);
		} else if (type.equals("CHECK")) {
		    final String booleanString = ""
			    + ((Boolean) data.get("checked")).booleanValue();
		    statement.bindString(3, booleanString);
		    statement.bindNull(4);
		} else if (type.equals("SPINNER")) {
		    final String selection = ""
			    + ((Integer) data.get("position")).intValue();
		    statement.bindString(3, selection);
		    final String spinner = (String) data.get("list");
		    statement.bindString(4, spinner);
		}
		long row = statement.executeInsert();
		if (row == -1) {
		    Log.e(TapdexActivity.TAG, "Could not insert entry '"
			    + entry + "' for form '" + form + "'");
		}
		statement.close();
		close();
	    } catch (SQLException e) {
		Log.e(TapdexActivity.TAG, "Error on adding data for entry '"
			+ entry + "'", e);
	    }
	}
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
	    Cursor cursor = db.rawQuery(
		    "SELECT * FROM " + Table.FORMS.getName() + ";", null);
	    /*
	     * http://stackoverflow.com/questions/4920528/iterate-through-rows-from
	     * -sqlite-query
	     */
	    if (cursor.moveToFirst()) {
		do {
		    list.add(cursor.getString(cursor
			    .getColumnIndex(Field.FORMS_NAME.getColumnName())));
		} while (cursor.moveToNext());
	    }
	    cursor.close();
	} catch (SQLiteException e) {
	    Log.e(TapdexActivity.TAG, "Error on getting form names", e);
	}
	close();
	return list;
    }

    public List<String> getEntryNames(String formName) {
	List<String> list = new ArrayList<String>();
	int formId = getFormId(formName);
	if (formId != -1) {
	    if (db == null || !db.isOpen()) {
		db = getReadableDatabase();
	    }

	    if (!db.isReadOnly()) {
		close();
		db = getReadableDatabase();
	    }
	    try {
		Cursor cursor = db.rawQuery(
			"SELECT * FROM " + Table.ENTRY.getName() + " WHERE formid='" + formId +"';", null);
		if (cursor.moveToFirst()) {
		    do {
			list.add(cursor.getString(cursor
				.getColumnIndex(Field.ENTRY_NAME
					.getColumnName())));
		    } while (cursor.moveToNext());
		}
		cursor.close();
	    } catch (SQLiteException e) {
		Log.e(TapdexActivity.TAG, "Error on getting format for form '"
			+ formName + "'", e);
	    }
	    close();
	}
	return list;
    }
    
    public String getFormat(String form)
    {
	String format = "";
	int formId = getFormId(form);
	if (formId != -1) {
	    try {
		Cursor cursor = db.rawQuery(
			"SELECT * FROM " + Table.FORMS.getName() + " WHERE formid='" + formId +"';", null);
		if (cursor.moveToFirst()) {
		    do {
			format = cursor.getString(cursor
				.getColumnIndex(Field.FORMS_FORMAT
					.getColumnName()));
		    } while (cursor.moveToNext());
		}
		cursor.close();
	    } catch (SQLiteException e) {

	    }
	    close();
	}
	return format;
    }

    public enum Field {

	FORMS_ID(Table.FORMS, "id", Type.INTEGER), FORMS_NAME(Table.FORMS,
		"name", Type.STRING), FORMS_FORMAT(Table.FORMS, "format",
		Type.STRING), ENTRY_ID(Table.ENTRY, "id", Type.INTEGER), ENTRY_FORM_ID(
		Table.ENTRY, "formid", Type.INTEGER), ENTRY_NAME(Table.ENTRY,
		"name", Type.STRING), FIELDS_ENTRY_ID(Table.FIELDS, "entryid",
		Type.INTEGER), FIELDS_KEY(Table.FIELDS, "fieldKey", Type.STRING), FIELDS_VALUE(
		Table.FIELDS, "fieldValue", Type.STRING), FIELDS_DATA(
		Table.FIELDS, "fieldData", Type.STRING);

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
