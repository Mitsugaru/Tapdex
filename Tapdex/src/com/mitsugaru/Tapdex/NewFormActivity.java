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
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewFormActivity extends ListActivity {
    private static final CharSequence[] fields = new CharSequence[] { "Text",
	    "Rating", "Checkbox", "Spinner" };
    private final Activity activity = this;
    private ArrayList<FieldEntry> entries = new ArrayList<FieldEntry>();
    private FieldEntryAdapter entryAdapter = null;
    private MergeAdapter adapter = null;
    private EditText formName, entryName;
    private int count = 0;
    private DatabaseHandler db;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.newform);
	/**
	 * Set button to show dialog
	 */
	Button button = (Button) findViewById(R.id.newFormAddFieldButton);
	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		showAddFieldDialog();
	    }
	});
	formName = (EditText) findViewById(R.id.formName);
	entryName = (EditText) findViewById(R.id.entryName);
	// Adapters
	adapter = new MergeAdapter();
	entryAdapter = new FieldEntryAdapter(activity,
		android.R.layout.simple_list_item_1, entries);
	adapter.addAdapter(entryAdapter);
	setListAdapter(adapter);
	// Animations
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
	// TODO Database
	db = DatabaseHandler.getInstance(this);
    }

    private void showAddFieldDialog() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Fields");
	builder.setItems(fields, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
		// TODO add fields
		String field = fields[item].toString();
		if (field.equalsIgnoreCase("text")) {
		    TextFieldEntry t = new TextFieldEntry("Title" + count,
			    activity, entryAdapter);
		    entryAdapter.add(t);
		} else if (field.equalsIgnoreCase("rating")) {
		    RatingFieldEntry r = new RatingFieldEntry("Rate" + count,
			    activity, entryAdapter);
		    entryAdapter.add(r);
		} else if (field.equalsIgnoreCase("checkbox")) {
		    CheckFieldEntry c = new CheckFieldEntry("Check" + count,
			    activity, entryAdapter);
		    entryAdapter.add(c);
		} else if (field.equalsIgnoreCase("spinner")) {
		    final ArrayList<String> list = new ArrayList<String>();
		    list.add("item1");
		    SpinnerFieldEntry n = new SpinnerFieldEntry("Spinner"
			    + count, activity, entryAdapter, list);
		    entryAdapter.add(n);
		}
		count++;
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Now, inflate our submenu.
	MenuInflater inflater = new MenuInflater(this);
	inflater.inflate(R.menu.newformmenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.createForm: {
	    String name = formName.getEditableText().toString();
	    if (db.formNameExists(name)) {
		Toast toast = Toast.makeText(activity, "Form '" + name
			+ "' already exists!", Toast.LENGTH_SHORT);
		toast.show();
	    } else {
		int id = 0;
		StringBuilder sb = new StringBuilder();
		for (FieldEntry field : entries) {
		    sb.append(field.getName().replaceAll("&", "") + "&" + id++
			    + "&" + field.getType().name() + "&");
		}
		// Remove trailing ampersand
		sb.deleteCharAt(sb.length() - 1);
		db.createForm(name, sb.toString());
		// Add first entry
		String entry = entryName.getEditableText().toString();
		db.createEntry(name, entry);
		// Add values for entry
		for(FieldEntry field: entries)
		{
		    db.addData(name, entry, field.getData());
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
