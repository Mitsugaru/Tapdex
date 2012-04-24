package com.mitsugaru.Tapdex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.commonsware.cwac.merge.MergeAdapter;
import com.mitsugaru.Tapdex.DatabaseHandler.FieldData;
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
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EntryListActivity extends ListActivity {
    private final Activity activity = this;
    private String formName, entryName;
    private DatabaseHandler db;
    private ArrayList<FieldEntry> entries = new ArrayList<FieldEntry>();
    private FieldEntryAdapter entryAdapter = null;
    private MergeAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.entrylist);
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    formName = extras.getString("formName");
	    if (formName == null) {
		Log.e(TapdexActivity.TAG, "Null for form name");
		this.finish();
	    }
	    entryName = extras.getString("entryName");
	    if (entryName == null) {
		Log.e(TapdexActivity.TAG, "Null for entry name");
		this.finish();
	    }
	} else {
	    Log.e(TapdexActivity.TAG, "Null extras");
	    this.finish();
	}
	Log.i(TapdexActivity.TAG, "on create: " + formName + "/" + entryName);
	// Set header
	TextView header = (TextView) findViewById(R.id.entryHeader);
	header.setText(entryName);
	// Database
	db = DatabaseHandler.getInstance(this);
	// Adapter
	adapter = new MergeAdapter();
	entryAdapter = new FieldEntryAdapter(activity,
		android.R.layout.simple_list_item_1, entries);
	adapter.addAdapter(entryAdapter);
	setListAdapter(adapter);
	fillEntries();
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

    @Override
    public void onResume() {

	super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    // Save changes
	    saveChanges();
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStop() {
	// Save changes
	saveChanges();
	super.onStop();
    }
    
    private void saveChanges()
    {
	db.clearData(formName, entryName);
	// Add values for entries
	for (FieldEntry field : entries) {
	    db.addData(formName, entryName, field.getData());
	}
    }

    private void fillEntries() {
	// Get data and set it to fields
	final String format = db.getFormat(formName);
	final List<FieldData> data = db.getData(formName, entryName);
	final String[] split = format.split("&");
	int index = 0;
	for (int i = 0; i < split.length; i += 3) {
	    try {
		final String title = split[i];
		// int id = Integer.parseInt(split[i + 1]);
		String type = split[i + 2];
		if (type.equalsIgnoreCase("text")) {
		    TextFieldEntry t = new TextFieldEntry(title, activity,
			    entryAdapter);
		    entryAdapter.add(t);
		    t.setEntry(data.get(index).getValue());
		} else if (type.equalsIgnoreCase("rating")) {
		    RatingFieldEntry r = new RatingFieldEntry(title, activity,
			    entryAdapter);
		    entryAdapter.add(r);
		    r.setRating(Float.parseFloat(data.get(index).getValue()));
		} else if (type.equalsIgnoreCase("check")) {
		    CheckFieldEntry c = new CheckFieldEntry(title, activity,
			    entryAdapter);
		    entryAdapter.add(c);
		    c.setChecked(Boolean.parseBoolean(data.get(index)
			    .getValue()));
		} else if (type.equalsIgnoreCase("spinner")) {
		    final String list = data.get(index).getData();
		    final ArrayList<String> spinnerList = new ArrayList<String>(
			    Arrays.asList(list.split("&")));
		    SpinnerFieldEntry n = new SpinnerFieldEntry(title,
			    activity, entryAdapter, spinnerList);
		    entryAdapter.add(n);
		    n.setPosition(Integer.parseInt(data.get(index).getValue()));
		}
	    } catch (IndexOutOfBoundsException a) {
		// IGNORE
		Log.e(TapdexActivity.TAG,
			"IndexOutOfBoundsException for entry " + entryName
				+ " for form " + formName, a);
	    } catch (NumberFormatException n) {
		// Ignore
		Log.e(TapdexActivity.TAG, "NumberFormatException for entry "
			+ entryName + " for form " + formName, n);
	    }
	    index++;
	}
    }
}
