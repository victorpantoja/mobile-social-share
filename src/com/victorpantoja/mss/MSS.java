/**
 * 
 */
package com.victorpantoja.mss;

import com.victorpantoja.mss.screen.LoginScreen;
import com.victorpantoja.mss.screen.MainScreen;
import com.victorpantoja.mss.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class MSS extends Activity {

	protected static final String TAG = "mss";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();

		SharedPreferences pref = getSharedPreferences("MOBILESOCIALSHARE", MODE_PRIVATE);
		String login = pref.getString("login", "not_found");
		String pass = pref.getString("pass", "not_found");

		if(login.equals("not_found") || pass.equals("not_found")){
			startActivity(new Intent(this,LoginScreen.class));
		}
		else{
			String auth = Util.tryAuthenticate(login, pass);
			if(auth.equals("")){
				Toast.makeText(this, "Wrong User or Password", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this,LoginScreen.class));
			}
			else{
				Intent mainScreen = new Intent(this,MainScreen.class);
				mainScreen.putExtra("auth", auth);
				startActivity(mainScreen);
			}
		}

	}
}
