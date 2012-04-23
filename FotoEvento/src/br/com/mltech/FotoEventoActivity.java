package br.com.mltech;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Participante;

/**
 * Activity principal da aplicação
 * 
 * @author maurocl
 * 
 */
public class FotoEventoActivity extends Activity {

	private static final String TAG = "FotoEventoActivity";
	
	private Contratante contratante;
	private Evento evento;
	private List<Participante> listaParticipantes;
	private Participante usuario;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Cria/usa um contratante

		// cria/usa um evento

		// lista de participantes

		contratante = new Contratante("Casa da Benção-ITEJ", "bencao@itej.com.br");

		evento = new Evento();

		evento.setContratante(contratante);

		evento.setNome("Dia da Caridade");

		usuario = new Participante("Mauro Cesar Lopes", "maurocl@terra.com.br", "(19) 8143-8978");

		Button botao = (Button) findViewById(R.id.btnParticipante);

		// trata o botão Participante
		botao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// verifica se existe um contratante
				
				// verifica se existe um evento cadastrado
				
				// verifica se as bordas das fotos já foram disponibilizadas ao evento
				
				
				
				// abre a Activity para enviar uma foto ao evento
				
				// processa o caso de uso enviar foto
				Log.d(TAG,"Enviar foto ...");
				
			}

		});

	}

	/**
	 * Criação dos Menus
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		criaMenus(menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * criaMenus(Menu menu)
	 * 
	 * @param menu
	 */
	private void criaMenus(Menu menu) {
		
		// ---------------------------------------------
		// Menu: Cadastro de Usuários
		// ----------------------------------------------
		MenuItem participante = menu.add(0, 0, 0, "Participante");
		participante.setIcon(R.drawable.ic_launcher);

		participante.setIntent(new Intent(this, ParticipanteActivity.class));

		// ---------------------------------------------
		// Menu: Relatórios
		// ----------------------------------------------
		MenuItem relatorio = menu.add(0, 1, 0, "Relatórios");
		relatorio.setIcon(R.drawable.ic_launcher);

		relatorio.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(FotoEventoActivity.this, RelatorioActivity.class);
				startActivity(intent);
				return (false);
			}
		});

		// ----------------------------------------------
		// Menu: Eventos
		// ---------------------------------------------
		MenuItem evento = menu.add(0, 2, 0, "Eventos");
		evento.setIcon(R.drawable.ic_launcher);

		evento.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(FotoEventoActivity.this, EventoActivity.class);
				startActivity(intent);
				return (false);
			}
		});

		// ----------------------------------------------
		// Menu: Enviar Foto
		// ---------------------------------------------
		MenuItem foto = menu.add(0, 3, 0, "Enviar Foto");
		foto.setIcon(R.drawable.ic_launcher);

		foto.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Intent intent = new Intent(this, UsuarioActivity.class);
				// startActivity(intent);
				Toast.makeText(FotoEventoActivity.this, "Enviar Foto", Toast.LENGTH_LONG).show();
				return (false);
			}
		});
		// ----------------------------------------------
		// Menu: Contratante
		// ---------------------------------------------
		MenuItem contratante = menu.add(0, 4, 0, "Contratante");
		contratante.setIcon(R.drawable.ic_launcher);

		contratante.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Intent intent = new Intent(this, UsuarioActivity.class);
				// startActivity(intent);
				Toast.makeText(FotoEventoActivity.this, "Contratante", Toast.LENGTH_LONG).show();
				return (false);
			}
		});
	}
}