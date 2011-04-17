/**
 * 
 */
package com.victorpantoja.mss;

import com.victorpantoja.mss.screen.LoginScreen;
import com.victorpantoja.mss.screen.MainScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class MSS extends Activity implements Runnable {
	
	protected static final String TAG = "mss";
	
	private activeHandler handler;
	public final static int DELAY = 100, ASKLOGIN = 1001, TRYAUTH = 1002, RUN = 1003, WAITCONTEXT = 1004;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		handler = new activeHandler(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
		
		handler.postDelayed(this, DELAY);
	}

	@Override
	public void run() {
		SharedPreferences pref = getSharedPreferences("MOBILESOCIALSHARE", 0);
		SharedPreferences.Editor editor = pref.edit();

		editor.putString("pass", "");
		editor.commit();
		
		handler.sendMessageDelayed(Message.obtain(handler, ASKLOGIN, this), DELAY);
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) 
	{
		handler.sendMessage(Message.obtain(handler, TRYAUTH, this));
	}
	
	class activeHandler extends Handler{
		private MSS parent;

		activeHandler (MSS obj) {
			parent = obj;
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		
			switch (msg.what) {
				case ASKLOGIN:
					startActivityForResult(new Intent(getApplicationContext(), LoginScreen.class), 1);
					break;
					
				case TRYAUTH:
					SharedPreferences pref = getSharedPreferences("MOBILESOCIALSHARE", 0);
					String login = pref.getString("login", "not_found");
					String pass = pref.getString("pass", "not_found");
	
					if (tryAuthenticate(login, pass))
					{
						Toast.makeText(getApplicationContext(), "Autenticado", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(getApplicationContext(),MainScreen.class));
	
					}
					else{
						Toast.makeText(getApplicationContext(), "Falhou!!!", Toast.LENGTH_SHORT).show();
						handler.sendMessageDelayed(Message.obtain(handler, ASKLOGIN, msg.obj), DELAY);
					}
					
				default:
					finish();
					break;
			}
		}
	}
	
	private boolean tryAuthenticate (String login, String pass)
	{
		try {
			return true;
		}

		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}

		return false;
	}
}
