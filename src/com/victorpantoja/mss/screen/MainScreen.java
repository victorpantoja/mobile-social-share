/**
 * 
 */
package com.victorpantoja.mss.screen;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
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
		
		Intent status = new Intent(this,StatusUpdateScreen.class);
		Intent places = new Intent(this,MyPlacesScreen.class);
		Intent friends = new Intent(this,FriendsScreen.class);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		final TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("Status")
				.setContent(status));

		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("My Places")
				.setContent(places));
		
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("Friends")
				.setContent(friends));
	}
}