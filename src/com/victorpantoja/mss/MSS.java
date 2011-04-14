/**
 * 
 */
package com.victorpantoja.mss;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.victorpantoja.mss.screen.LoginScreen;

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
		
		Log.d(TAG, "running");

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
