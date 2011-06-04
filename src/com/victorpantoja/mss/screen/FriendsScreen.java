/**
 * 
 */
package com.victorpantoja.mss.screen;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

/**
 * @author victor.pantoja
 *
 */
public class FriendsScreen extends Activity {
	
	private String auth = "";
	
    @Override
    public void onResume() {
        super.onResume();
        
        setContentView(R.layout.friends);
        
        Bundle extras = getIntent().getExtras();
        
        auth = extras.getString("auth");
                
		String url = Util.url_get_friend+"?auth="+auth;
		
		String result = Util.queryRESTurl(url);
		
		if(result.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Internal Error.", Toast.LENGTH_SHORT).show();
		}
		else{
			try{
				JSONObject json = new JSONObject(result);
				Toast.makeText(getApplicationContext(), json.getJSONArray("friend").getString(0), Toast.LENGTH_SHORT).show();

			}  
			catch (JSONException e) {
				Log.e("JSON", "There was an error parsing the JSON", e);  
			}
		}
    }
}
