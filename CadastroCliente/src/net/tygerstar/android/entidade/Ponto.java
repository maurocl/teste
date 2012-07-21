package net.tygerstar.android.entidade;

import android.location.Location;

import com.google.android.maps.GeoPoint;

/**
 * 
 * @author maurocl
 *
 */
public class Ponto extends GeoPoint{

  /**
   * 
   * @param latitudeE6
   * @param longitudeE6
   */
	public Ponto(int latitudeE6, int longitudeE6) {
		super(latitudeE6, longitudeE6); 
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public Ponto(double latitude, double longitude){
		this((int)(latitude*1E6), (int)(longitude*1E6));
	}
	
	/**
	 * 
	 * @param location
	 */
	public Ponto(Location location){
		this(location.getLatitude(), location.getLongitude());
	}
	
}
