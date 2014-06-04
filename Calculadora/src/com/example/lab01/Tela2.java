package com.example.lab01;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * 
 *
 */
public class Tela2 extends Activity implements
		android.view.View.OnClickListener {

	public static final String TAG = "Tela2";

	public String[] tokens = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "+", "-", "*", "/", "ENTER", ".", "DEL" };

	Pilha<Double> pilha = new Pilha<Double>();

	EditText display;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.i(TAG, "Pilha class: " + pilha.getClass());

		if (pilha instanceof Pilha<?>) {
			Log.i(TAG, "Ok");
		}

		// setContentView(R.layout.calculadora);

		setContentView(R.layout.activity_tela2);

		display = ((EditText) findViewById(R.id.editText1));

		display.setText("0");

		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
		((Button) findViewById(R.id.button3)).setOnClickListener(this);
		((Button) findViewById(R.id.button4)).setOnClickListener(this);
		((Button) findViewById(R.id.button5)).setOnClickListener(this);
		((Button) findViewById(R.id.button6)).setOnClickListener(this);
		((Button) findViewById(R.id.button7)).setOnClickListener(this);
		((Button) findViewById(R.id.button8)).setOnClickListener(this);
		((Button) findViewById(R.id.button9)).setOnClickListener(this);
		((Button) findViewById(R.id.button0)).setOnClickListener(this);

		((Button) findViewById(R.id.buttonMais)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonMenos)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonMultiplica)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonDivide)).setOnClickListener(this);

		((Button) findViewById(R.id.buttonEnter)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonDel)).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		CharSequence texto = ((EditText) findViewById(R.id.editText1))
				.getText();

		String token = null;

		if (v == ((Button) findViewById(R.id.button1))) {
			Log.i(TAG, "botao 1 pressionado");
			token = "1";
		} else if (v == ((Button) findViewById(R.id.button2))) {
			Log.i(TAG, "botao 2 pressionado");
			token = "2";
		} else if (v == ((Button) findViewById(R.id.button3))) {
			Log.i(TAG, "botao 3 pressionado");
			token = "3";
		} else if (v == ((Button) findViewById(R.id.button4))) {
			Log.i(TAG, "botao 4 pressionado");
			token = "4";
		} else if (v == ((Button) findViewById(R.id.button5))) {
			Log.i(TAG, "botao 5 pressionado");
			token = "5";
		} else if (v == ((Button) findViewById(R.id.button6))) {
			Log.i(TAG, "botao 6 pressionado");
			token = "6";
		} else if (v == ((Button) findViewById(R.id.button7))) {
			Log.i(TAG, "botao 7 pressionado");
			token = "7";
		} else if (v == ((Button) findViewById(R.id.button8))) {
			Log.i(TAG, "botao 8 pressionado");
			token = "8";
		} else if (v == ((Button) findViewById(R.id.button9))) {
			Log.i(TAG, "botao 9 pressionado");
			token = "9";
		} else if (v == ((Button) findViewById(R.id.button0))) {
			Log.i(TAG, "botao 0 pressionado");
			token = "0";
		} else if (v == ((Button) findViewById(R.id.buttonPonto))) {
			Log.i(TAG, "botao . pressionado");
			token = ".";
		} else if (v == ((Button) findViewById(R.id.buttonDel))) {
			Log.i(TAG, "botao Del pressionado");
			token = "DEL";
			display.setText("");
		} else if (v == ((Button) findViewById(R.id.buttonMais))) {
			Log.i(TAG, "botao + pressionado");
			token = "+";
			// empilha o valor do visor
			pilha.push(Double.valueOf(texto.toString()));
			calculaValorOperacao(token);
		} else if (v == ((Button) findViewById(R.id.buttonMenos))) {
			Log.i(TAG, "botao - pressionado");
			token = "-";
			// empilha o valor do visor
			pilha.push(Double.valueOf(texto.toString()));
			calculaValorOperacao(token);
		} else if (v == ((Button) findViewById(R.id.buttonMultiplica))) {
			Log.i(TAG, "botao * pressionado");
			token = "*";
			// empilha o valor do visor
			pilha.push(Double.valueOf(texto.toString()));
			calculaValorOperacao(token);
		} else if (v == ((Button) findViewById(R.id.buttonDivide))) {
			Log.i(TAG, "botao / pressionado");
			token = "/";
			// empilha o valor do visor
			pilha.push(Double.valueOf(texto.toString()));
			calculaValorOperacao(token);

		} else if (v == ((Button) findViewById(R.id.buttonEnter))) {

			token = "ENTER";
			Log.i(TAG, "botao <ENTER> pressionado");

			if (texto != null && !texto.equals("")) {

				// ((EditText) findViewById(R.id.editText1)).setText(texto);
				// empilha o valor do visor
				pilha.push(Double.valueOf(texto.toString()));
				// atualiza();
				// limpa o display

			}
			display.setText("");
		}

		if (isDigitToken(token) || token.equals(".")) {
			((EditText) findViewById(R.id.editText1)).setText(texto + token);
		}

	}

	/**
	 * Verifica se o token e um digito entre 0 e 9
	 * 
	 * @param token Token (representad por uma string)
	 * 
	 * @return true se o token for um digito ou false caso contrario.
	 */
	boolean isDigitToken(String token) {

		if (token == null) {
			return false;
		}

		char c = token.charAt(0);

		if (c < '0' || c > '9') {
			return false;
		}

		return true;

	}

	/**
	 * Verifica se o token e um operador
	 * 
	 * @param token Token
	 * 
	 * @return true se o token for um operador ou false caso contrario.
	 */
	boolean isOperatorToken(String token) {

		if (token.equals("+") || token.equals("-") || token.equals("*")
				|| token.equals("/")) {
			
			return true;
			
		} else {
			
			return false;
			
		}

	}

	/**
	 * Atualiza o conteudo do topo da pilha no display
	 * 
	 */
	void atualiza() {

		Double valor = pilha.top();

		if (valor != null) {
			display.setText(String.valueOf(valor));
		}

		else {
			display.setText("");
		}

	}

	/**
	 * Executa uma operacao entre os dois valores no topo da pilha e insere o
	 * resultado na pilha
	 * 
	 * @param token
	 * 
	 * @throws Exception
	 */
	void calculaValorOperacao(String token) {

		double x = 0.0;
		
		try {
			x = pilha.pop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Erro ao remover operando da pilha");
			e.printStackTrace();
		}

		double y = 0.0;
		try {
			y = pilha.pop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Erro ao remover operando da pilha");
			e.printStackTrace();
		}

		double topo = 0.0;

		Log.d(TAG, "x=" + x + ", y=" + y);

		if (token.equals("+")) {
			topo = x + y;
		} else if (token.equals("-")) {
			topo = y - x;
		} else if (token.equals("*")) {
			topo = x * y;
		} else if (token.equals("/")) {
			topo = y / x;
		}

		Log.d(TAG, "oper: " + token + ", res=" + topo);

		// Empilha o resultado da operacao
		pilha.push(topo);

		((EditText) findViewById(R.id.editText1)).setText("");

		atualiza();

	}

}
