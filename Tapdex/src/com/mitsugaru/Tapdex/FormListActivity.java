package com.mitsugaru.Tapdex;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class FormListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.formlist);
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
    public boolean onCreateOptionsMenu(Menu menu) {
	// Now, inflate our submenu.
	MenuInflater inflater = new MenuInflater(this);
	inflater.inflate(R.menu.formmenu, menu);
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch(item.getItemId())
	{
	case R.id.addEntry:
	{
	    //TODO show activity like new form, but already populated with fields
	    return true;
	}
	default:
	{
	    break;
	}
	}
	return false;
    }
}
