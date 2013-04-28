package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.mltech.view.ExibeFotoActivity;



public class TestaExibeFotoActivity extends Activity implements OnClickListener {

	Button botao ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testa_exibe_foto);
        
        Button b = new Button(this);
        b.setText("Clique aqui");
        b.setOnClickListener(this);
        
        botao = (Button) findViewById(R.id.button1);
        
        botao.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(TestaExibeFotoActivity.this,ExibeFotoActivity.class);
			    	
			    	Bundle params = new Bundle();
			    	params.putString("msg", "olá");
			    	
			    	i.putExtras(params);
			    	
			    	i.putExtra("msg2", "outra msg");
			    	
			    	startActivity(i);			
					}
				});
        
    }

    public void onClick(View v) {
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_testa_exibe_foto, menu);
        return true;
    }

    
}
