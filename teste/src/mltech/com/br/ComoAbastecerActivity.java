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
		final EditText resultado = (EditText) findViewById(R.id.editText3);
		
		final TextView msg = (TextView) findViewById(R.id.textView2);

		Log.i("Pre�o da gasolina: ", gasolina.getText().toString());
		Log.i("Pre�o do alcool: ", alcool.getText().toString());
		
		
	
		resultado.setText("123");
	
		/*
	
		*/
	
		Button botao = (Button) findViewById(R.id.button1);
		
		botao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				double res,gas,alc;
				
				Log.i("O bot�o foi clicado", "clicado");
				Toast.makeText(v.getContext(), "meu texto de aviso", Toast.LENGTH_LONG);
				
				gas = Double.parseDouble(gasolina.getText().toString());
				alc = Double.parseDouble(alcool.getText().toString());
				
				res = gas*0.7;
				
				if(alc>res) {
					Log.i("","compensa gasolina");
					//msg.setTextColor(180);
					msg.setText("� melhor abastecer com gasolina");
				}
				else {
					Log.i("","compensa alcool");
					msg.setText("� melhor abastecer com �lcool");
				}
			
				// converte o resultado e atualiza o item
				resultado.setText(String.valueOf(res));
				
			}
		});

	

	
	Button btnLimpar = (Button) findViewById(R.id.btnLimpar);
	
	btnLimpar.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Log.i("btnLimpar.setOnClickListener", "O bot�o Limpar foi clicado");
		
			// converte o resultado e atualiza o item
			gasolina.setText("");
			alcool.setText("");
			resultado.setText("");
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