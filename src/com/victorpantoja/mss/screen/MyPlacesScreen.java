/**
 * 
 */
package com.victorpantoja.mss.screen;

import com.victorpantoja.mss.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author victor.pantoja
 *
 */
public class MyPlacesScreen extends Activity {
	
	private String server_name = "http://192.168.0.255:9080";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.places);
        
		WebView mWebView = (WebView) findViewById(R.id.contextMap);
	    mWebView.loadUrl(server_name+"/webview/");
    }
}
