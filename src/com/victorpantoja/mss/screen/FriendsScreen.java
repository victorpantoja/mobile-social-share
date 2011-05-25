/**
 * 
 */
package com.victorpantoja.mss.screen;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.victorpantoja.mss.R;

/**
 * @author victor.pantoja
 *
 */
public class FriendsScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        
        Bundle extras = getIntent().getExtras();
                
		Toast.makeText(getApplicationContext(), "Auth: "+extras.getString("auth"), Toast.LENGTH_SHORT).show();
    }
}
