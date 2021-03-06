/**
 * 
 */
package com.victorpantoja.mss.screen;

import com.mobilesocialshare.mss.MSSApi;
import com.victorpantoja.mss.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class LoginScreen  extends Activity
{
	protected static final String TAG = "LoginScreen";
	private EditText textNome, textPass;
	private MSSApi mss;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		SharedPreferences pref = getSharedPreferences("MOBILESOCIALSHARE", MODE_PRIVATE);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.login);
        
        Button button = (Button) findViewById(R.id.btnLogin);
		textNome = (EditText) findViewById(R.id.campoLogin);
		textPass = (EditText) findViewById(R.id.campoSenha);
		
		String login = pref.getString("login", "not_found");
		String pass = pref.getString("pass", "not_found");

		if (!login.equals("not_found")) {
			textNome.setText(login);
			textPass.setText(pass);
			textPass.requestFocus();
		}

		button.setOnClickListener(loginListener);
		
		Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
		btnCreateAccount.setOnClickListener(createAccountListener);
	}
	
	private OnClickListener loginListener = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	mss = new MSSApi("http://192.168.0.191:9080");
	    	String auth = mss.Login(textNome.getText().toString(), textPass.getText().toString());
	    	
			SharedPreferences pref = getSharedPreferences("MOBILESOCIALSHARE", MODE_PRIVATE);

			SharedPreferences.Editor editor = pref.edit();
			editor.putString("login", textNome.getText().toString());
			editor.putString("pass", textPass.getText().toString());
			editor.commit();
			
			if(auth.equals("")){
				Toast.makeText(getApplicationContext(), "Wrong User or Password", Toast.LENGTH_SHORT).show();
				editor.putString("pass", "");
				editor.commit();
				textPass.setText("");
			}
			else{
				Intent mainScreen = new Intent(getApplicationContext(),MainScreen.class);
				mainScreen.putExtra("auth", auth.split(";")[0]);
				mainScreen.putExtra("invites", Integer.parseInt(auth.split(";")[1]));
				startActivity(mainScreen);
			}
		}
	};
	
	private OnClickListener createAccountListener = new OnClickListener() {
	    public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(), CreateAccountScreen.class));
		}
	};
}
