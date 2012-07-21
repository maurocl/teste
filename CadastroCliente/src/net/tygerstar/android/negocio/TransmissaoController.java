package net.tygerstar.android.negocio;

import java.io.InputStream;
import java.util.List;

import net.tygerstar.android.R;
import net.tygerstar.android.componente.ListViewDuploAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * 
 * 
 * 
 */
public class TransmissaoController extends Activity implements OnClickListener, Runnable {

  protected static final String URL = "http://www.tygerstar.net/servidorandroid/arquivo.gz";

  //protected static final String URL = "http://192.168.1.7/servidorandroid/server.php?";
  private Handler handler = new Handler();

  private ProgressDialog progresso;

  private ListView lvLista;

  @Override
  protected void onCreate(Bundle bundle) {

    super.onCreate(bundle);
    setContentView(R.layout.tela_transmitir);

    inicializarViews();

    ImageButton btVoltar = (ImageButton) findViewById(R.transmissao.btVoltar);

    btVoltar.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        finish();
      }
    });

    Button btTransmitir = (Button) findViewById(R.transmissao.btTransmissao);
    btTransmitir.setOnClickListener(this);

  }

  @Override
  public void run() {

    try {
      
      InputStream arquivo = ClienteHttp.downloadArquivo(URL);

      final List<String> resposta = Funcoes.descompactarArquivo(arquivo);

      handler.post(new Runnable() {

        @Override
        public void run() {

          lvLista.setAdapter(new ListViewDuploAdapter(TransmissaoController.this, resposta));
        }
        
      });
      
    } catch (Exception e) {
      
      e.printStackTrace();
      
    } finally {
      
      progresso.dismiss();
      
    }
    
  }

  @Override
  public void onClick(View v) {

    progresso = ProgressDialog.show(this, "Aviso", "Recuperando arquivo do servidor.", false, true);
    new Thread(this).start();
  }

  private void inicializarViews() {

    lvLista = (ListView) findViewById(R.transmissao.lvLista);
  }
  
}
