/**
 * 
 */
package com.victorpantoja.mss.screen;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;

/**
 * @author victor.pantoja
 *
 */
public class MainScreen extends TabActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		SharedPreferences pref = getSharedPreferences("MOBILESOCIALSHARE", 0);
		String auth = pref.getString("auth", "not_found");
		
		Intent status = new Intent(this,StatusUpdateScreen.class);
		status.putExtra("auth", auth);
		
		Intent places = new Intent(this,MyPlacesScreen.class);
		Intent friends = new Intent(this,FriendsScreen.class);
		friends.putExtra("auth", auth);
		
		Intent me = new Intent(this,MyInformationScreen.class);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
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
	    menu.add(0, 0, 0, "Settings");
	    menu.add(0, 1, 0, "Quit");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case 0:
	        //newGame();
	        return true;
	    case 1:
	        //quit();
	        return true;
	    }
	    return false;
	}
}