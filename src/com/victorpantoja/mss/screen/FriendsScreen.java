/**
 * 
 */
package com.victorpantoja.mss.screen;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.victorpantoja.mss.util.Util;

/**
 * @author victor.pantoja
 *
 */
public class FriendsScreen extends ListActivity {
	
	private String auth = "";
	
	JSONObject json;
	
    @Override
    public void onResume() {
        super.onResume();
        
        Bundle extras = getIntent().getExtras();
        
        auth = extras.getString("auth");
                
		String url = Util.url_get_friend+"?auth="+auth;
		
		String result = Util.queryRESTurl(url);
		ArrayList<String> friends = new ArrayList<String>();
		
		if(result.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Internal Error.", Toast.LENGTH_SHORT).show();
		}
		else{
			try{
				json = new JSONObject(result);

				for (int i=0;i<json.getJSONArray("friend").length();i++){
					String friend = ((JSONObject)json.getJSONArray("friend").get(i)).getString("first_name")+" "+((JSONObject)json.getJSONArray("friend").get(i)).getString("last_name");
					friends.add(friend);
				}
			}  
			catch (JSONException e) {
				Log.e("JSON", "There was an error parsing the JSON", e);  
			}
		}
		
		List<String> menu = new ArrayList<String>(0);
		
		for(int i=0;i<friends.size();i++){
			menu.add(friends.get(i));
		}
		
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu));
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int posicao, long id)
	{
		String username = "";
		try {
			username = ((JSONObject)json.getJSONArray("friend").get(posicao)).getString("username");

		} catch (JSONException e) {
			Log.e("JSON", "There was an error parsing the JSON", e); 
		}
		

		Intent friendInformation = new Intent(this,FriendInformationScreen.class);
		friendInformation.putExtra("username", username);
		friendInformation.putExtra("auth", auth);
		friendInformation.putExtra("isFriend", true);

		startActivity(friendInformation);
	}
}
