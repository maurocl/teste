package br.com.mltech.kmcontrol.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.mltech.kmcontrol.R;

public class CombustivelActivity extends Activity {

	public static String TAG = "Abastecimento";

	double precoGasolina;
	double precoEtanol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tipo_combustivel);

		final EditText editGasolina = (EditText) findViewById(R.id.editGasolina);
		final EditText editEtanol = (EditText) findViewById(R.id.editEtanol);
		final EditText editEscolha = (EditText) findViewById(R.id.editEscolha);

		final EditText editComentario = (EditText) findViewById(R.id.editComentario);

		Button btnCalcular = (Button) findViewById(R.id.button1);

		btnCalcular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					precoGasolina = Double.valueOf(editGasolina.getText()
							.toString());
					precoEtanol = Double.valueOf(editEtanol.getText()
							.toString());

					double razao = precoEtanol / precoGasolina * 100;

					String sRazao = String.format("%.2f", razao);

					if (precoEtanol <= (0.7 * precoGasolina)) {
						Log.d(TAG, "Etanol");

						editEscolha.setText("Etanol");

						editComentario
								.setText("Abasteça com etanol, pois o preço do etanol representa "
										+ sRazao + "% do valor da gasolina");

					} else {
						Log.d(TAG, "Gasolina");
						editEscolha.setText("Gasolina");

						editComentario
								.setText("Abasteça com gasolina, pois o preço do etanol representa "
										+ sRazao + "% do valor da gasolina");

					}

				} catch (NumberFormatException nfe) {
					Log.w(TAG, "Erro na conversão de valores");
				}

			}

		});

		Button btnSair = (Button) findViewById(R.id.button2);
		btnSair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editGasolina.setText("");
				editEtanol.setText("");
				editEscolha.setText("");
				editComentario.setText("");

				editGasolina.requestFocus();
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.abastecimento, menu);
		return true;
	}

}
