package com.mitsugaru.Tapdex;

import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;
import com.mitsugaru.Tapdex.fields.FieldEntry;
import com.mitsugaru.Tapdex.fields.FieldEntryAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ListView;

public class EntryListActivity extends ListActivity {
    private final Activity activity = this;
    private String formName, entryName;
    private DatabaseHandler database;
    private ArrayList<FieldEntry> entries = new ArrayList<FieldEntry>();
    private FieldEntryAdapter entryAdapter = null;
    private MergeAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.formentrylist);
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
	// Set header
	EditText header = (EditText) findViewById(R.id.headerText);
	header.setText(entryName);
	// Database
	database = DatabaseHandler.getInstance(this);
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
    
    private void fillEntries()
    {
	//TODO get data and set it to fields
    }
}
