package net.tygerstar.android.componente;

import net.tygerstar.android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 
 * 
 *
 */
public class ImageAdapter extends BaseAdapter {

  private Context myContext;

  private int[] imagens = {
      R.drawable.pic0,
      R.drawable.pic1,
      R.drawable.pic2,
      R.drawable.pic3,
      R.drawable.pic4,
      R.drawable.pic5,
      R.drawable.pic6,
      R.drawable.pic7,
      R.drawable.pic8,
  };

  /**
   * 
   * @param c
   */
  public ImageAdapter(Context c) {

    this.myContext = c;
  }

  @Override
  public int getCount() {

    return this.imagens.length;
  }

  @Override
  public Object getItem(int position) {

    return imagens[position];
  }

  @Override
  public long getItemId(int position) {

    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    ImageView iv = new ImageView(this.myContext);
    iv.setImageResource(imagens[position]);
    iv.setScaleType(ImageView.ScaleType.FIT_XY);
    iv.setLayoutParams(new Gallery.LayoutParams(150, 150));
    return iv;
  }

  /**
   * 
   * @param focuset
   * @param offset
   * 
   * @return
   */
  public float getScale(boolean focuset, int offset) {

    return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
  }
  
}
