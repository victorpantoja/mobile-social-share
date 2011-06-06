/**
 * 
 */
package com.victorpantoja.mss.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

/**
 * @author victor.pantoja
 *
 */
public class FriendsScreen extends ListActivity {
	
	private String auth = "";
	
    @Override
    public void onResume() {
        super.onResume();
        
/*        setContentView(R.layout.friends);
        
        TextView textFriends = (TextView) findViewById(R.id.textFriend);*/
        
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
				JSONObject json = new JSONObject(result);

				for (int i=0;i<json.getJSONArray("friend").length();i++){ 
					friends.add(json.getJSONArray("friend").get(i).toString());
				}
			}  
			catch (JSONException e) {
				Log.e("JSON", "There was an error parsing the JSON", e);  
			}
		}
		
		String[] listItems = {"item 1", "item 2"}; 
		
		for(int i=0;i<friends.size();i++){
			Log.i("FriendsScreen", friends.get(i));
		}
		
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems));
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int posicao, long id)
	{
		Toast.makeText(getApplicationContext(), "posicao: "+posicao+" id: "+id, Toast.LENGTH_SHORT).show();

		//Intent event = new Intent(this,FriendInformationScreen.class);
		//event.putExtra("posicao", posicao);
		//startActivity(event);
	}
}
