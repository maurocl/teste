package net.tygerstar.android.negocio;

import net.tygerstar.android.R;
import net.tygerstar.android.dao.ClienteDAO;
import net.tygerstar.android.entidade.Cliente;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CadastroController extends Activity {

  private ClienteDAO clidao = new ClienteDAO();

  private ImageButton btCadastrar, btVoltar;

  private static final String[] estados = new String[] { "RN", "PB", "CE", "PE", "MA", "PI" };

  private EditText edNome, edEndereco, edBairro, edCidade;

  private Spinner spEstado;

  private Cliente cliente;

  private Button btLimpar;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    overridePendingTransition(R.anim.fadeout, R.anim.fadein);

    setContentView(R.layout.tela_cadastro);

    btCadastrar = (ImageButton) findViewById(R.telacadastro.btCadastrar);
    btVoltar = (ImageButton) findViewById(R.telacadastro.btVoltar);

    edNome = (EditText) findViewById(R.chico.edNome);
    edEndereco = (EditText) findViewById(R.telacadastro.edEndereco);
    edBairro = (EditText) findViewById(R.telacadastro.edBairro);
    edCidade = (EditText) findViewById(R.telacadastro.edCidade);
    spEstado = (Spinner) findViewById(R.telacadastro.spEstado);
    btLimpar = (Button) findViewById(R.telacadastro.btLimpar);

    ArrayAdapter<String> aaEstados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados);
    spEstado.setAdapter(aaEstados);

    Intent it = getIntent();
    if (it != null) {
      cliente = (Cliente) it.getSerializableExtra("cliente");
      if (cliente != null) {
        edNome.setText(cliente.getNome());
        edEndereco.setText(cliente.getEndereco());
        edBairro.setText(cliente.getBairro());
        edCidade.setText(cliente.getCidade());
        spEstado.setSelection(aaEstados.getPosition(cliente.getEstado()), true);
      } else {
        cliente = new Cliente();
      }
    }

    btCadastrar.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        salvar();
      }
    });

    btVoltar.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        //runFadeInAnimation();
        finish();
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
      }
    });

    btLimpar.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        edNome.setText("");
        edCidade.setText("");
        edEndereco.setText("");
        edBairro.setText("");
      }
    });
  }

  /**
	 * 
	 */
  private void salvar() {

    cliente.setNome(edNome.getText().toString());
    cliente.setEndereco(edEndereco.getText().toString());
    cliente.setBairro(edBairro.getText().toString());
    cliente.setCidade(edCidade.getText().toString());
    cliente.setEstado(estados[spEstado.getSelectedItemPosition()]);

    //String st = (String) spEstado.getAdapter().getItem(spEstado.getSelectedItemPosition());				

    long retorno = clidao.salvar(cliente);

    String msgRetorno;

    if (retorno == -1) {
      msgRetorno = "Erro ao salvar cliente.";
    } else {
      msgRetorno = "Cliente salvo com sucesso.";
      Log.i("teste", "Salvou o cliente com sucesso");
    }

    AlertDialog.Builder aviso = new AlertDialog.Builder(CadastroController.this);

    aviso.setTitle("Aviso");
    aviso.setMessage(msgRetorno);
    aviso.setNeutralButton("OK", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {

        finish();
      }
    });

    aviso.show();

  }

}
