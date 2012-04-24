package com.mitsugaru.Tapdex;

import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;
import com.mitsugaru.Tapdex.fields.CheckFieldEntry;
import com.mitsugaru.Tapdex.fields.FieldEntry;
import com.mitsugaru.Tapdex.fields.FieldEntryAdapter;
import com.mitsugaru.Tapdex.fields.RatingFieldEntry;
import com.mitsugaru.Tapdex.fields.SpinnerFieldEntry;
import com.mitsugaru.Tapdex.fields.TextFieldEntry;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewEntryActivity extends ListActivity {
    private final Activity activity = this;
    private String formName;
    private DatabaseHandler db;
    private ArrayList<FieldEntry> entries = new ArrayList<FieldEntry>();
    private FieldEntryAdapter entryAdapter = null;
    private MergeAdapter adapter = null;
    private EditText entryName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.newentry);
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    formName = extras.getString("formName");
	    if (formName == null) {
		Log.e(TapdexActivity.TAG, "Null for form name");
		this.finish();
	    }
	} else {
	    Log.e(TapdexActivity.TAG, "Null extras");
	    this.finish();
	}
	entryName = (EditText) findViewById(R.id.newEntryName);
	// Database
	db = DatabaseHandler.getInstance(this);
	// Adapter
	adapter = new MergeAdapter();
	entryAdapter = new FieldEntryAdapter(activity,
		android.R.layout.simple_list_item_1, entries);
	adapter.addAdapter(entryAdapter);
	setListAdapter(adapter);
	addFields();
	// Animation
	AnimationSet set = new AnimationSet(true);

	Animation animation = new AlphaAnimation(0.0f, 1.0f);
	animation.setDuration(50);
	set.addAnimation(animation);

	animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
		-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
	animation.setDuration(100);
	set.addAnimation(animation);

	LayoutAnimationController controller = new LayoutAnimationController(
		set, 0.5f);
	ListView listView = getListView();
	listView.setLayoutAnimation(controller);
    }
    
    private void addFields() {
	// Get data and set it to fields
	final String format = db.getFormat(formName);
	final String[] split = format.split("&");
	for (int i = 0; i < split.length; i += 3) {
	    try {
		final String title = split[i];
		//int id = Integer.parseInt(split[i + 1]);
		String type = split[i + 2];
		if (type.equalsIgnoreCase("text")) {
		    TextFieldEntry t = new TextFieldEntry(title, activity, entryAdapter);
		    entryAdapter.add(t);
		} else if (type.equalsIgnoreCase("rating")) {
		    RatingFieldEntry r = new RatingFieldEntry(title, activity, entryAdapter);
		    entryAdapter.add(r);
		} else if (type.equalsIgnoreCase("check")) {
		    CheckFieldEntry c = new CheckFieldEntry(title, activity, entryAdapter);
		    entryAdapter.add(c);
		} else if (type.equalsIgnoreCase("spinner")) {
		    final ArrayList<String> spinnerList = new ArrayList<String>();
		    spinnerList.add("item1");
		    SpinnerFieldEntry n = new SpinnerFieldEntry(title, activity, entryAdapter, spinnerList);
		    entryAdapter.add(n);
		}
	    } catch (IndexOutOfBoundsException a) {
		// IGNORE
		Log.e(TapdexActivity.TAG, "IndexOutOfBoundsException for new entry for form " + formName, a);
	    } catch (NumberFormatException n) {
		// Ignore
		Log.e(TapdexActivity.TAG, "NumberFormatException for new entry for form " + formName, n);
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Now, inflate our submenu.
	MenuInflater inflater = new MenuInflater(this);
	inflater.inflate(R.menu.newentrymenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.addNewEntry: {
	    String name = entryName.getEditableText().toString();
	    if (db.entryNameExistsForForm(formName, name)) {
		Toast toast = Toast.makeText(activity, "Entry '" + name
			+ "' already exists!", Toast.LENGTH_SHORT);
		toast.show();
	    } else {
		// Add first entry
		db.createEntry(formName, name);
		// Add values for entry
		for(FieldEntry field: entries)
		{
		    db.addData(formName, name, field.getData());
		}
		activity.finish();
	    }
	    return true;
	}
	default:
	    return false;
	}
    }
}
