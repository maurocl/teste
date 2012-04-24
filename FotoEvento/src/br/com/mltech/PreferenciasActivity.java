package br.com.mltech;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * PreferenciasActivity
 * 
 * Activity responsável pelo cadastramento de preferências de uso do sistema
 * 
 * @author maurocl
 * 
 */
public class PreferenciasActivity extends Activity {

	private SharedPreferences preferences;
	
  private boolean b;
	
	/**
	 * onCreate(android.os.Bundle savedInstanceState)
	 */
	protected void onCreate(android.os.Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.preferencias);
	
		preferences = getSharedPreferences("preferencias", MODE_PRIVATE);

		boolean externo = preferences.getBoolean("ordenar_alfabeticamente", true);
	
		final CheckBox checkbox = (CheckBox) findViewById(R.id.chkOrdemAlfabetica);

		checkbox.setChecked(externo);

		Button botao = (Button) findViewById(R.id.btnGravarPreferencias);

		// processa o botão de gravação das preferências
		botao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				 Editor edit = preferences.edit();
				 
				 b = checkbox.isChecked();
							 				 
				 edit.putBoolean("ordenar_alfabeticamente", b);
				 
				// grava as preferências
				edit.commit();

				finish();

			}
		});

	};

}
