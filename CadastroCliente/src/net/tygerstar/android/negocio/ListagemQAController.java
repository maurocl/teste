package net.tygerstar.android.negocio;

import java.util.ArrayList;
import java.util.List;

import net.tygerstar.android.R;
import net.tygerstar.android.componente.ActionItem;
import net.tygerstar.android.componente.QuickAction;
import net.tygerstar.android.componente.QuickAction.OnActionItemClickListener;
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

/**
 * 
 * 
 *
 */
public class ListagemQAController extends Activity {

  private ClienteDAO clidao = new ClienteDAO();

  private ListView lvListaClientes;

  private ArrayAdapter<Cliente> clientes;

  private ImageButton btVoltar;

  private Button btExportar;

  public Cliente cliente;

  public List<Cliente> arrayClientes = new ArrayList<Cliente>();

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

        Funcoes.exportar(arrayClientes, ListagemQAController.this);
      }
      
    });

    // --------- Configuração para o evento de clicar na listagem --------
    lvListaClientes.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View view, int posicao, long id) {

        cliente = (Cliente) lvListaClientes.getAdapter().getItem(posicao);

        ActionItem aiAtualizar = new ActionItem();
        aiAtualizar.setIcon(getResources().getDrawable(R.drawable.edit));
        aiAtualizar.setTitle("Atualizar");

        ActionItem aiExcluir = new ActionItem();
        aiExcluir.setIcon(getResources().getDrawable(R.drawable.delete));
        aiExcluir.setTitle("Excluir");

        final QuickAction qaListagem = new QuickAction(ListagemQAController.this);
        qaListagem.addActionItem(aiAtualizar);
        qaListagem.addActionItem(aiExcluir);
        qaListagem.setOnActionItemClickListener(new OnActionItemClickListener() {

          @Override
          public void onItemClick(QuickAction source,
              int botao, int actionId) {

            if (botao == 0) {
              // BotÃ£o Editar
              Intent it = new Intent(ListagemQAController.this, CadastroController.class);
              it.putExtra("cliente", cliente);
              startActivity(it);
              finish();
            }
            
            if (botao == 1) {
              // BotÃ£o Excluir
              AlertDialog.Builder avisoExcluir = new AlertDialog.Builder(ListagemQAController.this);
              avisoExcluir.setTitle("Confirma exclusão?");
              avisoExcluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                  Funcoes.database.delete("cliente", "_id = ?", new String[] { "" + cliente.getId() });
                  finish();
                }
              });
              avisoExcluir.setNegativeButton("NÃ£o", null);
              avisoExcluir.show();
            }
          }
        });
        qaListagem.show(view);
      }
    });

    popularListagem();

  }

  private void popularListagem() {

    try {
      
      //Busca no banco pelos clientes
      arrayClientes = clidao.listar();
      
      //Verifica se retornou alguma coisa
      if (arrayClientes.size() > 0) {
        
        //Cria o array adapter
        clientes = new ArrayAdapter<Cliente>(this, android.R.layout.simple_list_item_1);
        
        //Popula o array adapter com o resultado da busca
        for (Cliente cli : arrayClientes) {
          clientes.add(cli);
        }
        
        //injeta o array adaptar na listagem
        lvListaClientes.setAdapter(clientes);
        
      } else {
        
        Toast.makeText(this, "Tabela vazia.", Toast.LENGTH_LONG).show();
        
      }
    } catch (Exception e) {
      
      AlertDialog.Builder aviso = new AlertDialog.Builder(ListagemQAController.this);
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

  /**
	 * 
	 */
  private void inicializarViews() {

    btVoltar = (ImageButton) findViewById(R.listagem.btVoltar);
    btExportar = (Button) findViewById(R.listagem.btExportar);
    lvListaClientes = (ListView) findViewById(R.listagem.lvListaClientes);
  }

}
