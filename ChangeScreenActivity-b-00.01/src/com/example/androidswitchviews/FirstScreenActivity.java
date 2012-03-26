package com.example.androidswitchviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstScreenActivity extends Activity {

	EditText inputName;
	EditText inputEmail;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen1);

		inputName = (EditText) findViewById(R.id.name);
		inputEmail = (EditText) findViewById(R.id.email);
		Button btnNextScreen = (Button) findViewById(R.id.btnNextScreen);

		btnNextScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View srg0) {

				// Starting a new intent

				Intent nextScreen = new Intent(getApplicationContext(),
						SecondScreenActivity.class);

				// Sending data to another Activity

				nextScreen.putExtra("name", inputName.getText().toString());
				nextScreen.putExtra("email", inputEmail.getText().toString());
				
				Log.e("n",inputName.getText()+"."+inputEmail.getText());
				
				startActivity(nextScreen);
			}
		});

	}
}