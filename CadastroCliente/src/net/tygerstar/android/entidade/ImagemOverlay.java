package net.tygerstar.android.entidade;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 * @author maurocl
 * 
 */
public class ImagemOverlay extends Overlay {

  private Paint paint = new Paint();

  private final GeoPoint geopoint;

  private final int idimagem;

  public ImagemOverlay(GeoPoint geopoint, int id) {

    this.geopoint = geopoint;
    this.idimagem = id;
  }

  @Override
  public void draw(Canvas canvas, MapView mapview, boolean sombra) {

    super.draw(canvas, mapview, sombra);

    Point ponto = mapview.getProjection().toPixels(geopoint, null);
    Bitmap bitmap = BitmapFactory.decodeResource(mapview.getResources(), this.idimagem);
    RectF r = new RectF(ponto.x, ponto.y, ponto.x + bitmap.getWidth(), ponto.y + bitmap.getHeight());
    canvas.drawBitmap(bitmap, null, r, paint);

  }

}
