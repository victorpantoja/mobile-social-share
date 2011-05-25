/**
 * 
 */
package com.victorpantoja.mss.screen;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

/**
 * @author victor.pantoja
 *
 */
public class CreateAccountScreen extends Activity implements OnClickListener{
	
	protected static final String TAG = "mss";
	private EditText textLastName, textFirstName, textUsername;
	private ProgressDialog mProgressDlg;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        
        Button button = (Button) findViewById(R.id.btnCreateAccount);
		textLastName = (EditText) findViewById(R.id.lastName);
		textFirstName = (EditText) findViewById(R.id.firstName);
		textUsername = (EditText) findViewById(R.id.campoLogin);
		
		button.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		mProgressDlg = ProgressDialog.show(CreateAccountScreen.this, "", "Loading. Please wait...", true);
		mProgressDlg.show();
				
		String url = Util.url_create_acount+"?username="+textUsername.getText()+"&firstName="+textFirstName.getText()+"&lastName="+textLastName.getText();
		
		String result = Util.queryRESTurl(url);
		
		if(result.equals(""))
		{
			mProgressDlg.dismiss();
			Toast.makeText(getApplicationContext(), "Error Creating Your Account.", Toast.LENGTH_SHORT).show();
			
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		
		try{
			JSONObject json = new JSONObject(result);
			
			mProgressDlg.dismiss();
						
			if (json.getString("status").equals("ok"))
			{
				Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
				finishFromChild(getParent());
			}
			else{
				Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
			}
		}  
		catch (JSONException e) {  
			Log.e("JSON", "There was an error parsing the JSON", e);  
		}
	}
}
