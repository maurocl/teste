
package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormDatas extends Activity {

  private static final String TAG = "FormDatas";

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG,"onCreate()");
    
    setContentView(R.layout.formdatas);

    Intent i = getIntent();

    String paramDtInicial = null, paramDtFinal = null;

    if (i.getData() != null) {
      
      Log.d(TAG, "Data: " + i.getData());
      
      paramDtInicial = i.getStringExtra("br.com.mltech.dtInicial");
      paramDtFinal = i.getStringExtra("br.com.mltech.dtFinal");

    }

    Button botao = (Button) findViewById(R.id.inserir);

    // Data inicial
    final EditText dtInicial = (EditText) findViewById(R.id.dtInicial);

    if (paramDtInicial!=null && !paramDtInicial.equals("")) {
      dtInicial.setText(paramDtInicial);
    }

    // Data final
    final EditText dtFinal = (EditText) findViewById(R.id.dtFinal);

    if (paramDtFinal!=null && !paramDtFinal.equals("")) {
      dtFinal.setText(paramDtFinal);
    }

    botao.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        // verifica se a data inicial está preenchida
        if (dtInicial.getText().toString().equals("")) {
          showToast("Data inicial não foi preenchida");
          return;
        }

        // verifica se a data inicial é válida

        // verifica se a data final está preenchida
        if (dtFinal.getText().toString().equals("")) {
          showToast("Data final não foi preenchida");
          return;
        }

        // verifica se a data final é válida

        // verifica se a data final é maior ou igual a data inicial

        Intent i = new Intent();

        i.putExtra("br.com.mltech.dtInicial", dtInicial.getText().toString());
        i.putExtra("br.com.mltech.dtFinal", dtFinal.getText().toString());

        setResult(RESULT_OK, i);

        finish();

      }

    });

  }

  /**
   * Exibe uma mensagem usando um Toast
   * 
   * @param msg
   *          Mensagem exibida pelo Toast
   * 
   */
  void showToast(String msg) {

    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  /**
   * 
   * @param data
   * @return
   */
  boolean isDataValida(String data) {

    return true;
  }

}
