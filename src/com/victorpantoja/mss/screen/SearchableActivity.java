/**
 * 
 */
package com.victorpantoja.mss.screen;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.victorpantoja.mss.R;

/**
 * @author victor.pantoja
 *
 */
public class SearchableActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          
          doMySearch(query);
        }

    }

	private void doMySearch(String query) {
		Toast.makeText(getApplicationContext(), "Busca Realizada", Toast.LENGTH_SHORT).show();

	}
}
