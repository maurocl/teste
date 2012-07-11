package br.com.mltech.testasync;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.com.mltech.utils.Transaction;
import br.com.mltech.utils.TransactionTask;

public class MainActivity extends Activity implements Transaction {

	TextView tv;
	int k = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button b1 = (Button) findViewById(R.id.button1);
		Button b2 = (Button) findViewById(R.id.button2);

		tv = (TextView) findViewById(R.id.textView1);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		TransactionTask t = new TransactionTask(this, this, 1);
		t.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void execute() throws Exception {
		Log.d("TESTE", "execute() ...");
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 10000; j++) {

			}
			k++;
		}

	}

	@Override
	public void updateView() {

		tv.setText(k);
	}

	public static void x() {
	}

}
