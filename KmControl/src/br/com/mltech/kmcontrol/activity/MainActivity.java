package br.com.mltech.kmcontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import br.com.mltech.kmcontrol.R;

public class MainActivity extends Activity {

	Intent intent;

	private static String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate() ...");

		setContentView(R.layout.activity_main);

	}

	public void onClickVeiculos(View v) {
		intent = new Intent(this, VehiclesActivity.class);
		startActivity(intent);
	}

	public void onClickAbastecimentos(View v) {
		intent = new Intent(this, AbastecimentoActivity.class);
		startActivity(intent);
	}

	public void onClickCalculaOpcaoAbastecimento(View v) {
		intent = new Intent(this, CombustivelActivity.class);
		startActivity(intent);
	}
	
	public void onClickCalcMediaActivity(View v) {
		intent = new Intent(this, CalcMediaActivity.class);
		startActivity(intent);
	}

}
