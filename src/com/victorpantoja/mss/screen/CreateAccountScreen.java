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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobilesocialshare.mss.MSSApi;
import com.victorpantoja.mss.R;

/**
 * @author victor.pantoja
 *
 */
public class CreateAccountScreen extends Activity implements OnClickListener{
	
	protected static final String TAG = "mss";
	private EditText textLastName, textFirstName, textUsername, email;
	private ProgressDialog mProgressDlg;
	private Spinner s;
	private MSSApi mss;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        
        Button button = (Button) findViewById(R.id.btnCreateAccount);
		textLastName = (EditText) findViewById(R.id.lastName);
		textFirstName = (EditText) findViewById(R.id.firstName);
		textUsername = (EditText) findViewById(R.id.campoLogin);
		email = (EditText) findViewById(R.id.campoEmail);
		s = (Spinner) findViewById(R.id.genderSpinner);
	    ArrayAdapter adapter = ArrayAdapter.createFromResource(
	            this, R.array.gender, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s.setAdapter(adapter);
	    
		button.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		mProgressDlg = ProgressDialog.show(CreateAccountScreen.this, "", "Loading. Please wait...", true);
		mProgressDlg.show();
		
		String[] genders = new String[3];
		genders[0] = "M";
		genders[1] = "F";
		genders[2] = "O";
		
		int position = s.getSelectedItemPosition();
						
		mss = new MSSApi("http://192.168.0.191:9080");
		String result = mss.CreateUser(email.getText().toString(), textLastName.getText().toString(), textFirstName.getText().toString(), textUsername.getText().toString(), genders[position]);
		
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
