package com.mitsugaru.Tapdex;

import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;
import com.mitsugaru.Tapdex.fields.FieldEntry;
import com.mitsugaru.Tapdex.fields.FieldEntryAdapter;
import com.mitsugaru.Tapdex.fields.TextFieldEntry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewFormActivity extends ListActivity
{
	private static final CharSequence[] fields = new CharSequence[]{"Text"};
	private final Activity activity = this;
	private ArrayList<FieldEntry> entries = new ArrayList<FieldEntry>();
	private FieldEntryAdapter entryAdapter = null;
	private MergeAdapter adapter=null;
	private int count = 0;
	
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
			public void onClick(View v)
			{
				showAddFieldDialog();
			}
		});
        adapter=new MergeAdapter();
        entryAdapter = new FieldEntryAdapter(activity, android.R.layout.simple_list_item_1, entries);
        adapter.addAdapter(entryAdapter);
        setListAdapter(adapter);
    }
    
    private void showAddFieldDialog()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Fields");
		builder.setItems(fields, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item)
			{
				//TODO add fields
				String field = fields[item].toString();
				if(field.equalsIgnoreCase("text"))
				{
					TextFieldEntry t = new TextFieldEntry("Title" + count, activity);
					entryAdapter.add(t);
				}
				count++;
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
    }
}
