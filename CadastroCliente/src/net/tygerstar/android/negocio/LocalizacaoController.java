package net.tygerstar.android.negocio;

import net.tygerstar.android.R;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LocalizacaoController extends Activity {
	private ImageButton btVoltar;
	private TextView tvLatitude, tvLongitude;
	private LocationManager lm = null;
	private LocationListener ll;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_localizacao);
		
		inicializarViews();
		
		btVoltar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});						

		ll = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(LocalizacaoController.this, "Iniciando localização via GPS.", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(LocalizacaoController.this, "Desabilitando GPS.", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLocationChanged(Location location) {
				tvLatitude.setText(""+location.getLatitude());
				tvLongitude.setText(""+location.getLongitude());
			}						
		};
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);		
	}
	
	/**
	 * Obrigatório para desabilitar o GPS ao sair da tela
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		if (lm != null){
			lm.removeUpdates(ll);
		}
	}
	
	private void inicializarViews(){
		btVoltar = (ImageButton) findViewById(R.localizacao.btVoltar);
		tvLatitude = (TextView) findViewById(R.localizacao.tvLatitude);
		tvLongitude= (TextView) findViewById(R.localizacao.tvLongitude);
	}
}
