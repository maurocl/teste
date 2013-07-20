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


public class CalcMediaActivity extends Activity {

	public static String TAG = "Abastecimento";

	private double media;
	private double valorPorLitro;
	private double litros;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calc_media);
	
		final EditText editKmPercorrida = (EditText) findViewById(R.id.editKmPercorrida);
		final EditText editLitros = (EditText) findViewById(R.id.editLitros);
		final EditText editValor = (EditText) findViewById(R.id.editValor);
		final EditText editMedia = (EditText) findViewById(R.id.editEscolha);
		final EditText editValorPorLitro = (EditText) findViewById(R.id.editValorPorLitro);

		Button btnCalcular = (Button) findViewById(R.id.button1);

		btnCalcular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					if ((editKmPercorrida != null) && (editLitros != null)) {
						
						media = (Double.valueOf(editKmPercorrida.getText()
								.toString()) / Double.valueOf(editLitros
								.getText().toString()));

						Log.i(TAG, "M�dia=" + media);

						editMedia.setText(String.valueOf(media) + " Km/l");

					}

					if ((editValor != null) && (editLitros != null)) {
						
						valorPorLitro = (Double.valueOf(editValor.getText()
								.toString()) / Double.valueOf(editLitros
								.getText().toString()));

						Log.i(TAG, "Valor por litro=" + valorPorLitro);

						editValorPorLitro.setText("R$ "+String.valueOf(valorPorLitro)
								+ " por litro");

					}

					if ((editValor != null) && (editValorPorLitro != null)) {
						
						litros = (Double
								.valueOf(editValor.getText().toString()) / Double
								.valueOf(editValorPorLitro.getText().toString()));

						Log.i(TAG, "N� de litros = " + String.valueOf(litros));

					}

				} catch (NumberFormatException nfe) {
					Log.w(TAG, "Erro na convers�o de valores");
				}

				/*
				if (editLitros != null) {

				}

				if (editValor != null) {

				}
				*/

			}

		});

		Button btnSair = (Button) findViewById(R.id.button2);
		btnSair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editKmPercorrida.setText("");
				editLitros.setText("");
				editValor.setText("");
				editMedia.setText("");
				editValorPorLitro.setText("");
				
				editKmPercorrida.requestFocus();
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.abastecimento, menu);
		return true;
	}

	/**
	 * Calcula a m�dia de consumo de combust�vel a partir do n� de quilometros percorridos e do n�mero total de litros gastos para abastecer o ve�culo.
	 * 
	 * @param quilometrosPercorridos N�mero de quilometros percorridos
	 * @param numLitros N�mero de litros necess�rios para encher o tanque
	 * 
	 * @return M�dia de consumo (em Km/l)
	 * 
	 */
	private double calculaMedia(double quilometrosPercorridos, double numLitros) {
		
		double media=0.0;
		
		if(numLitros!=0) {
			media = quilometrosPercorridos/numLitros;
		}
		
		return media;
		
	}
	
	/**
	 * Calcula o valor pago por litro.
	 * 
	 * @param valorPago Valor total necess�rio para encher o tanque	 
	 * @param numLitros N� de litros necess�rios para encher o tanque
	 * 
	 * @return O valor do litro do combust�vel
	 * 
	 */
	private double calculaValorPorLitro(double valorPago, double numLitros) {
		
		double valorPorLitro=0.0;
		
		if(numLitros!=0) {
			valorPorLitro = valorPago/numLitros;
		}
		
		return valorPorLitro;
		
	}
	
}
