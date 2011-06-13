/**
 * 
 */
package com.victorpantoja.mss.screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

/**
 * @author victor.pantoja
 *
 */
public class MainScreen extends TabActivity{
	
	static final int DIALOG_QUIT_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		String auth = extras.getString("auth");
		
		if(!auth.equals("")){
			//TODO - try authticating user...
		}
		
		Intent status = new Intent(this,StatusUpdateScreen.class);
		status.putExtra("auth", auth);
		
		Intent places = new Intent(this,MyPlacesScreen.class);
		places.putExtra("auth", auth);
		
		Intent friends = new Intent(this,FriendsScreen.class);
		friends.putExtra("auth", auth);
		
		Intent me = new Intent(this,MyInformationScreen.class);
		me.putExtra("auth", auth);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		final TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("Status")
				.setContent(status));

		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("My Places")
				.setContent(places));
		
		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator("Friends")
				.setContent(friends));
		
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator("Me")
				.setContent(me));
	}
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Add Friend");
	    menu.add(0, 1, 0, "Settings");
	    menu.add(0, 2, 0, "Quit");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case 0:
	    	Intent searchScreen = new Intent(getApplicationContext(),SearchableActivity.class);
	    	startActivity(searchScreen);
	        return true;
	    case 1:
	    	//settings
	        return true;
	    case 2:
	    	showDialog(DIALOG_QUIT_ID);
	        return true;
	    }
	    return false;
	}
	
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    AlertDialog.Builder builder;
	    switch(id) {
		    case DIALOG_QUIT_ID:
		    	builder = new AlertDialog.Builder(this);
		    	builder.setTitle("MSS");
		    	builder.setMessage("Are you sure you want to exit?")
		    	       .setCancelable(false)
		    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                MainScreen.this.finish();
		    	           }
		    	       })
		    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dialog.cancel();
		    	           }
		    	       });
		    	dialog = builder.create();
		    	break;
		    default:
		        dialog = null;
	    }
	    return dialog;
	}
}