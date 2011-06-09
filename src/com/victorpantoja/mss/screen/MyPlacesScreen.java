/**
 * 
 */
package com.victorpantoja.mss.screen;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author victor.pantoja
 *
 */
public class MyPlacesScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.places);
        
		WebView mWebView = (WebView) findViewById(R.id.contextMap);
	    mWebView.loadUrl(Util.server_name+"/webview/");
    }
}
