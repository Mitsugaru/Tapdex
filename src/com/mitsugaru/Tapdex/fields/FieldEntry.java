package com.mitsugaru.Tapdex.fields;

import java.util.Map;

import com.mitsugaru.Tapdex.TapdexActivity;

import android.content.Context;
import android.util.Log;
import android.view.View;

public abstract class FieldEntry {
    protected final Type type;
    protected String name;
    protected final Context context;
    protected final FieldEntryAdapter adapter;

    public FieldEntry() {
	this.type = Type.FIXED_ENTRY;
	this.name = "NONE";
	this.context = null;
	this.adapter = null;
    }

    public FieldEntry(Type type, String title, Context context,
	    FieldEntryAdapter adapter) {
	if (type == null) {
	    Log.e(TapdexActivity.TAG, "Null type for: " + title);
	    this.type = Type.FIXED_ENTRY;
	} else {
	    this.type = type;
	}
	if (context == null) {
	    Log.e(TapdexActivity.TAG, "Null context for: " + title);
	    this.context = null;
	} else {
	    this.context = context;
	}
	if (adapter == null) {
	    Log.e(TapdexActivity.TAG, "Null adapter for: " + title);
	    this.adapter = null;
	} else {
	    this.adapter = adapter;
	}
	this.name = title;
    }

    public enum Type {
	/**
	 * TEXT
	 */
	TEXT(TextFieldEntry.class),
	/**
	 * RATING
	 */
	RATING(RatingFieldEntry.class),
	/**
	 * CHECK
	 */
	CHECK(CheckFieldEntry.class),
	/**
	 * NOTE
	 */
	SPINNER(SpinnerFieldEntry.class),
	/**
	 * Miscellaneous Field
	 */
	FIXED_ENTRY(FieldEntry.class);

	private final Class<? extends FieldEntry> origin;

	private Type(Class<? extends FieldEntry> origin) {
	    this.origin = origin;
	}

	public Class<? extends FieldEntry> getFieldEntryClass() {
	    return origin;
	}
    }

    public String getName() {
	return name;
    }

    public Type getType() {
	return type;
    }

    protected void setName(String name) {
	this.name = name;
    }

    public FieldEntryAdapter getAdapter() {
	return adapter;
    }

    public abstract View expandView();
    
    public abstract Map<String, Object> getData();
}
