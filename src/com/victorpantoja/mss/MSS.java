/**
 * 
 */
package com.victorpantoja.mss;

import java.util.Observable;
import java.util.Observer;

import br.rio.puc.inf.lac.mobilis.cms.ContextConsumer;

import com.victorpantoja.mss.screen.LoginScreen;
import com.victorpantoja.mss.screen.MainScreen;
import com.victorpantoja.mss.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class MSS extends Activity implements Observer {

	protected static final String TAG = "mss";

	/** a CMS context consumer, used to receive CMS/SDM data */
	private MSSContextConsumer myConsumer;

	SharedPreferences.Editor editor;
	SharedPreferences pref;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		//TODO - Mudar para tela de loading
		Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();

		this.pref = getSharedPreferences("MOBILESOCIALSHARE", MODE_PRIVATE);
		String login = pref.getString("login", "not_found");
		String pass = pref.getString("pass", "not_found");
		this.editor = this.pref.edit();

		if(login.equals("not_found") || pass.equals("not_found")){
			startActivity(new Intent(this,LoginScreen.class));
		}
		else{
			String auth = Util.tryAuthenticate(login, pass);
			if(auth.equals("")){
				Toast.makeText(this, "Wrong User or Password", Toast.LENGTH_SHORT).show();
				this.editor.putString("pass", "");
				this.editor.commit();
				startActivity(new Intent(this,LoginScreen.class));
			}
			else{
				startTests();
				//TODO - obter as redes disponiveis
				Intent mainScreen = new Intent(this,MainScreen.class);
				mainScreen.putExtra("auth", auth.split(";")[0]);
				mainScreen.putExtra("invites", Integer.parseInt(auth.split(";")[1]));
				startActivity(mainScreen);
			}
		}

	}

	@Override
	public void update(Observable observable, Object data) {
		boolean active = (Boolean) data;
		if (active) {
			Log.d(TAG,"os servicos estao disponiveis :)");
			try {
				myConsumer.addContextInformationInterest("this.battery.isOk");
				Log.d(TAG,"adicionado consumer para this.battery.isOk");

			}
			catch (Exception e) {
				Log.e(TAG, "error while trying to add consumer - " + e.getMessage(), e);
			}

		}
		else {
			Log.d(TAG,"os servicos estao indisponiveis :(");
		}
	}

	protected void startTests() {
		ContextConsumer.startUp(getApplicationContext());
		try {
			myConsumer = new MSSContextConsumer(getApplicationContext(), this.editor);
			myConsumer.addObserver(this);
			Log.d(TAG,"observer added");
			update(null, new Boolean(ContextConsumer.isActive()));
		}
		catch (Exception e) {
			Log.d(TAG,"erro ao tentar criar consumer");
			Log.e(TAG, "error while trying to create consumer - " + e.getMessage(), e);
		}
	}
}
