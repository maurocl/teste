package net.tygerstar.android.negocio;

import java.util.ArrayList;
import java.util.List;

import net.tygerstar.android.R;
import net.tygerstar.android.dao.ClienteDAO;
import net.tygerstar.android.entidade.Cliente;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ListagemController extends Activity {
	
	private ClienteDAO clidao = new ClienteDAO();
	
	private ListView lvListaClientes;
	private ArrayAdapter<Cliente> clientes;
	private ImageButton btVoltar;
	private Button btExportar;
	public Cliente cliente;
	public List<Cliente> arrayclientes = new ArrayList<Cliente>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_listagem);
		
		inicializarViews();
		
		btVoltar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});				
		
		btExportar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Funcoes.exportar(arrayclientes, ListagemController.this);
			}
		});								
		
		lvListaClientes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				cliente = (Cliente)lvListaClientes.getAdapter().getItem(arg2);
				
		    	AlertDialog.Builder aviso = new AlertDialog.Builder(ListagemController.this);
				aviso.setTitle("Opção");				
								
				final CharSequence[] itens = {"Editar", "Excluir"};
				aviso.setItems(itens, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int botao) {						
						if (botao == 0){
							//Botão Editar
							Intent it = new Intent(ListagemController.this, CadastroController.class);														
							it.putExtra("cliente", cliente);
							startActivity(it);
							finish();
						}
						if (botao == 1){
							//Botão Excluir
							AlertDialog.Builder avisoExcluir = new AlertDialog.Builder(ListagemController.this);
							avisoExcluir.setTitle("Confirma exclusão?");
							avisoExcluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {									
									Funcoes.database.delete("cliente", "_id = ?", new String[]{""+cliente.getId()});
									finish();
								}
							});
							avisoExcluir.setNegativeButton("Não", null);
							avisoExcluir.create().show();
						}
					}
				});
								
				aviso.create().show();
				
			}
		});
		
		popularListagem();
	}
	
	private void popularListagem() {
		try {
			//Busca no banco pelos clientes
			arrayclientes = clidao.listar();
			//Verifica se retornou alguma coisa
			if (arrayclientes.size() > 0) {		
				//Cria o array adapter
				clientes = new ArrayAdapter<Cliente>(this, android.R.layout.simple_list_item_1);
				//Popula o array adapter com o resultado da busca
				for (Cliente cli : arrayclientes) {
					clientes.add(cli);
				}
				//injeta o array adaptar na listagem
				lvListaClientes.setAdapter(clientes);
			} else {
				Toast.makeText(this, "Tabela vazia.", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			AlertDialog.Builder aviso = new AlertDialog.Builder(ListagemController.this);
			aviso.setTitle("Aviso");
			aviso.setMessage("Erro na listagem de clientes");
			aviso.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			aviso.show();
		}
	}
	
	private void inicializarViews(){
		btVoltar = (ImageButton) findViewById(R.listagem.btVoltar);		
		btExportar = (Button) findViewById(R.listagem.btExportar);
		lvListaClientes = (ListView) findViewById(R.listagem.lvListaClientes);
	}
}
