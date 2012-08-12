package com.facebook.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * A transient activity which handles fbgraphex: URIs and passes those to the
 * GraphExplorer class. This is used to linkify the Object IDs in the graph api
 * response.
 */
public class IntentUriHandler extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Obtém informações sobre a intent chamadora
		Intent incomingIntent = getIntent();
		
		if (incomingIntent != null) {
		
			// intent não é nula
			
			// obtem os dados da intent (no caso é uma Uri)
			Uri intentUri = incomingIntent.getData();
	
			
			if (intentUri != null) {
				
				// Uri não é nula
				
				// atualiza o objeto com o nome do host
				Utility.objectID = intentUri.getHost();
				
				// cria uma nova intent para execução do GraphExplorer
				Intent graphIntent = new Intent(getApplicationContext(), GraphExplorer.class);
				
				// atualiza os flags da intent
				graphIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				// lança a activity
				startActivity(graphIntent);
				
			}
			
			// finaliza a activity correnre
			finish();
			
		}
		
	}
	
}
