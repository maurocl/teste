package net.tygerstar.android.componente;

import java.util.ArrayList;
import java.util.List;

import net.tygerstar.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 *
 */
public class ListViewDuploAdapter extends BaseAdapter {

  private Context contexto;

  private List<String> lista = new ArrayList<String>();

  /**
   * Construtor
   * 
   * @param contexto
   * @param lista
   * 
   */
  public ListViewDuploAdapter(Context contexto, List<String> lista) {

    this.contexto = contexto;
    this.lista = lista;
    
  }

  @Override
  public int getCount() {

    return lista.size();
  }

  @Override
  public Object getItem(int arg0) {

    return lista.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {

    return arg0;
  }

  @Override
  public View getView(int posicao, View arg1, ViewGroup arg2) {

    LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View v = inflater.inflate(R.layout.layout_listviewduplo, null);
    
    LinearLayout linearlayout = (LinearLayout) v.findViewById(R.listviewduplo.linearlayout);
    
    TextView campo1 = (TextView) v.findViewById(R.listviewduplo.campo1);
    TextView campo2 = (TextView) v.findViewById(R.listviewduplo.campo2);
    
    ImageView imagem = (ImageView) v.findViewById(R.listviewduplo.imagem);

    linearlayout.setBackgroundColor(v.getResources().getColor((posicao % 2 == 0 ? R.color.AMARELO : R.color.VERDE)));
    
    String nome = lista.get(posicao);
    
    campo1.setText("Posição: " + posicao);
    
    campo2.setText("Nome: " + nome);
    
    imagem.setImageResource((posicao % 2 == 0 ? R.drawable.add : R.drawable.negativo));

    return v;
    
  }

}
