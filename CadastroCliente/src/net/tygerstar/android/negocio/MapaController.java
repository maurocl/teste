package net.tygerstar.android.negocio;

import net.tygerstar.android.R;
import net.tygerstar.android.entidade.ImagemOverlay;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapaController extends MapActivity {
	private ImageButton btVoltar, btOndeEstou, btOndeVou, btZoomIn, btZoomOut;
	private TextView tvLatitude, tvLongitude;	
	private LocationListener locationlistener;
	private MapView mapa;
	private ImagemOverlay boneco = null, cliente = null;
	private double latitude = 0, longitude = 0, latDest = 0, lonDest = 0;
	private ProgressDialog aguarde;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView(R.layout.tela_mapa);
		
		inicializarViews();
		
		if (!Funcoes.gpsAtivado()){
			AlertDialog.Builder msg = Funcoes.msgERRO(this, "GPS não habilitado");
			msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			msg.show();
			
		}else{		
			Intent it = getIntent();
			if (it != null){
				latDest = (double)it.getDoubleExtra("latitudeDestino", 0.0);
				lonDest = (double)it.getDoubleExtra("longitudeDestino", 0.0);	
				if (latDest != 0.0 && lonDest != 0.0){
					ondeVou();
					btOndeVou.setEnabled(true);
				}
			}
			
			btVoltar.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					finish();
				}
			});		
			
			btOndeEstou.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
					ondeEstou();
				}
			});
			
			btOndeVou.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
					ondeVou();
				}
			});
			
			btZoomIn.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					if (latitude != 0 && longitude != 0){
						mapa.getController().zoomIn();
					}
				}
			});
	
			btZoomOut.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					if (latitude != 0 && longitude != 0){
						mapa.getController().zoomOut();
					}
				}
			});
			
			locationlistener = new LocationListener() {			
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {				
				}			
				@Override
				public void onProviderEnabled(String provider) {
					Toast.makeText(MapaController.this, "Iniciando localização via GPS.", Toast.LENGTH_SHORT).show();
				}			
				@Override
				public void onProviderDisabled(String provider) {
					Toast.makeText(MapaController.this, "Desabilitando GPS.", Toast.LENGTH_SHORT).show();
				}			
				@Override
				public void onLocationChanged(Location location) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					if (latitude != 0.0 && longitude != 0.0){
						tvLatitude.setText(Funcoes.formatoCoordenada.format(latitude));
						tvLongitude.setText(Funcoes.formatoCoordenada.format(longitude));
						if (boneco == null){
							ondeEstou();
						}
						if (aguarde.isShowing()){
							aguarde.dismiss();
						}
					}
				}						
			};
			
			Funcoes.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
			
			aguarde.show();
		}
	}
	
	private void ondeEstou(){
		GeoPoint ponto = new GeoPoint(((int)(latitude * 1E6)), ((int)(longitude * 1E6)));
		MapController mapcontroller = mapa.getController();
		if (boneco == null){
			boneco = new ImagemOverlay(ponto, R.drawable.informacoes_cliente);
			//mapa.getOverlays().clear();
			mapa.getOverlays().add(boneco);
		}
		mapcontroller.animateTo(ponto);
		mapcontroller.setZoom(16);		
	}
	
	private void ondeVou(){
		GeoPoint ponto = new GeoPoint(((int)(latDest * 1E6)), ((int)(lonDest * 1E6)));
		MapController mapcontroller = mapa.getController();
		if (cliente == null){
			cliente = new ImagemOverlay(ponto, R.drawable.local_cliente);
			//mapa.getOverlays().clear();
			mapa.getOverlays().add(cliente);
		}
		mapcontroller.animateTo(ponto);
		mapcontroller.setZoom(16);		
	}
	
	/**
	 * Obrigatório para desabilitar o GPS ao sair da tela
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();		
		if (Funcoes.locationManager != null){
			if (locationlistener != null){
				Funcoes.locationManager.removeUpdates(locationlistener);
			}
		}		
	}

	
	private void inicializarViews() {
		btVoltar = (ImageButton) findViewById(R.mapa.btVoltar);
		btOndeEstou = (ImageButton) findViewById(R.mapa.btOndeEstou);
		btOndeVou = (ImageButton) findViewById(R.mapa.btOndeVou);
		btOndeVou.setEnabled(false);
		btZoomIn = (ImageButton) findViewById(R.mapa.btZoomIn);
		btZoomOut = (ImageButton) findViewById(R.mapa.btZoomOut);
		tvLatitude = (TextView) findViewById(R.mapa.tvLatitude);
		tvLongitude= (TextView) findViewById(R.mapa.tvLongitude);
		mapa = (MapView) findViewById(R.mapa.mvMapa);
		mapa.setStreetView(true);
		mapa.setClickable(true);	
		aguarde = new ProgressDialog(MapaController.this);
		aguarde.setTitle("Aguarde...");
		aguarde.setMessage("Obtendo posição do GPS.");
		aguarde.setIndeterminate(true);
		aguarde.setIcon(R.drawable.satelite);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
