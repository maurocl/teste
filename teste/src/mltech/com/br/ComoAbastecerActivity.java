package mltech.com.br;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ComoAbastecerActivity
 * 
 * Esta activity  � respons�vel pelo c�lculo para saber se compensa
 * abastecer um carro com gasolina ou alcool baseado no pre�o do litro dos
 * combust�veis, considerando que o uso do �lcool � vantajoso no caso de seu
 * pre�o ser inferior a 70% do valor do pre�o da gasolina.
 * 
 * 
 * @author maurocl
 *
 */
public class ComoAbastecerActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		Toast.makeText(this, "inicio", Toast.LENGTH_LONG);

		final EditText gasolina  = (EditText) findViewById(R.id.editText1);
		final EditText alcool    = (EditText) findViewById(R.id.editText2);
		
		final TextView msg = (TextView) findViewById(R.id.textView2);
	
		Log.i("Pre�o da gasolina: ", gasolina.getText().toString());
		Log.i("Pre�o do alcool: ", alcool.getText().toString());
	
		Button botao = (Button) findViewById(R.id.button1);
		
		botao.setEnabled(false);
		
		if ( (gasolina.getText().toString()!="") && (alcool.getText().toString()!="") ) {
			botao.setEnabled(true);
		}
		
		botao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				double res,gas,alc,perc;
				
				if ( (gasolina.getText().toString()!="") && (alcool.getText().toString()!="") ) {
					return;
				}
				
				Log.i("O bot�o foi clicado", "clicado");
				Toast.makeText(v.getContext(), "meu texto de aviso", Toast.LENGTH_LONG);
				
				gas = Double.parseDouble(gasolina.getText().toString());
				alc = Double.parseDouble(alcool.getText().toString());
				
				// res possui o valor de 70% do pre�o da gasolina
				res = gas*0.7;
				
				// calcula o valor percentual do alcool em rela��o a gasolina
				perc = alc/gas*100;
				
				String vlrPercentual = String.valueOf(perc).format("%.2f",perc);
				
				if(alc>res) {
					// pre�o do alcool � maior que 70% do valor da gasolina
					// portanto � melhor abastecer o tanque com gasolina
					Log.i("","compensa gasolina");
					//msg.setTextColor(180);
					msg.setText("� melhor abastecer com gasolina ("+ vlrPercentual+")%");
				}
				else {
					// pre�o do alcool � menor ou igual a 70% do valor da gasolina
					// portanto � melhor abastecer o tanque com alcool
					Log.i("","compensa alcool");
					msg.setText("� melhor abastecer com �lcool ("+vlrPercentual+")%");
				}
				
			}
		});

	

	/* bot�o Limpar */
	Button btnLimpar = (Button) findViewById(R.id.btnLimpar);
	
	btnLimpar.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Log.i("btnLimpar.setOnClickListener", "O bot�o Limpar foi clicado");
		
			// converte o resultado e atualiza o item
			gasolina.setText("");
			alcool.setText("");
			msg.setText("");
			
		}
	});

}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("Activity1","onPause()");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("Activity1","onDestroy()");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("Activity1","onResume()");
	}
	
}