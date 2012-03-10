/**
 * 
 */
package com.victorpantoja.mss.screen;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilesocialshare.mss.MSSApi;
import com.victorpantoja.mss.R;

/**
 * @author victor.pantoja
 *
 */
public class SearchableActivity extends Activity {
	private EditText email;
	String auth;
	
	private MSSApi mss;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        Bundle extras = getIntent().getExtras();
        auth = extras.getString("auth");

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          
          doMySearch(query);
        }
        
        email = (EditText)findViewById(R.id.friendEmail);
        Button btnSendEmail = (Button)findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(sendEmailListener);

    }
   
	private OnClickListener sendEmailListener = new OnClickListener() {
	    public void onClick(View v) {

			String result = mss.SendEmailInvite(email.getText().toString(), auth);
			
			if(result.equals(""))
			{
				Toast.makeText(getApplicationContext(), "Internal Error.", Toast.LENGTH_SHORT).show();
			}
			else{
				try{
					JSONObject json = new JSONObject(result);
					
					Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
				}  
				catch (JSONException e) {  
					Log.e("JSON", "There was an error parsing the JSON", e);  
				}
			}
		}
	};

	private void doMySearch(String query) {
		Toast.makeText(getApplicationContext(), "Busca Realizada", Toast.LENGTH_SHORT).show();

	}
}
