package com.mitsugaru.Tapdex;

import com.example.android.apis.graphics.CameraPreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TapdexActivity extends Activity {
	//Class variables
	private final Activity activity = this;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Now, inflate our submenu.
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.camera:
				//TODO put this elsewhere, sorta
				// show dialog of download
				Intent intent = new Intent(findViewById(android.R.id.content)
						.getContext(), CameraPreview.class);
				startActivity(intent);
				return true;
			case R.id.settings:
				//TODO show settings view
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Writen by Vincent Deloso")
						.setCancelable(true)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id)
									{
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			default:
				// INFO non-menuitem? IDEK
				return false;
		}
	}
	
	/**
	 * Show the confirm exit dialog when the back button is pressed
	 */
	public void onBackPressed()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id)
							{
								activity.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}