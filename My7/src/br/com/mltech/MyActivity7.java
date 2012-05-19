package br.com.mltech;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.mltech.utils.ManipulaImagem;

/**
 * MyActivity7
 * 
 * @author maurocl
 * 
 */
public class MyActivity7 extends Activity {

  int numConfirmar;
  int numCancelar;

  Bitmap mBitmap;
  ImageView mImageView;

  ManipulaImagem imagem;

  int num;

  static int sNum;

  public static final String TAG = "MyActivity7";

  public static int opcao = 0;

  private static Context mContext = null;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    mContext = getApplicationContext();

    num++;
    sNum++;

    Log.v(TAG, "num=" + num + ", sNum=" + sNum);

    Log.i(TAG, "*** " + getClassName() + " onCreate() ***");

    Button btn1 = (Button) findViewById(R.id.btnConfirmar);
    Button btn2 = (Button) findViewById(R.id.btnCancelar);
    Button btn3 = (Button) findViewById(R.id.btnFim);

    imagem = new ManipulaImagem();

    mImageView = (ImageView) findViewById(R.id.imageView1);

    /**
     * btn1
     */
    btn1.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Log.d(TAG, "Botão Confirmar pressionado: " + numConfirmar++);
        mImageView.setVisibility(ImageView.INVISIBLE);

      }
    });

    /**
     * btn2
     */
    btn2.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Log.d(TAG, "Botão Cancelar pressionado: " + numCancelar++);
        mImageView.setVisibility(ImageView.VISIBLE);
      }
    });

    btn3.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Log.d(TAG, "Botão 3 pressionado: ");

        // 
        testaVerticalJoin();

        Application app = MyActivity7.this.getApplication();

        Context ctx = MyActivity7.this.getApplicationContext();

        ApplicationInfo appInfo = MyActivity7.this.getApplicationInfo();

        Log.v(TAG, "getApplication()=" + MyActivity7.this.getApplication());
        Log.v(TAG, "getApplicationContext()=" + MyActivity7.this.getApplicationContext());

        Log.v(TAG, "getApplicationInfo()=" + MyActivity7.this.getApplicationInfo());

        desejaSairDaAplicacao();

      }

    });

    showDisplayMetrics(this.getResources().getDisplayMetrics());

  }

  /**
   * showDisplayMetrics(DisplayMetrics d)
   * 
   * @param d
   *          instância da classe DisplayMetrics
   */
  public void showDisplayMetrics(DisplayMetrics d) {
    Log.v(TAG, "showDisplayMetrics(): " + d.toString());
    Log.v(TAG, "  widthPixels=" + d.widthPixels);
    Log.v(TAG, "  heightPixels=" + d.heightPixels);
    Log.v(TAG, "  density=" + d.density);
  }

  /**
   * 
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // TODO Auto-generated method stub
    super.onSaveInstanceState(outState);
    Log.d(TAG, "*** onSaveInstanceState ***");
  }

  /**
   * 
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onRestoreInstanceState(savedInstanceState);
    Log.d(TAG, "*** onRestoreInstanceState ***");
  }

  /**
   * 
   * 
   * Define uma moldura. Cria um arquivo com a moldura Lê o bitmap a partir do
   * arquivo Exibe o bitmap anteriormente criado em um ImageView referenciado
   * por mImageView
   * 
   */
  private void yyy() {

    // String filename =
    // "/mnt/sdcard/Pictures/fotoevento/molduras/moldura-320x240-red.png";
    // String filename = "/mnt/sdcard/Pictures/fotoevento/fotos/blue.png";

    String filename = "/mnt/sdcard/Pictures/fotoevento/molduras/moldura-320x240-green.png";

    File f = new File(filename);

    if (!f.exists()) {
      Log.w(TAG, "arquivo: " + filename + " não existente.");
      return;
    } else {
      Log.d(TAG, "O arquivo: " + filename + " existe no sistema de arquivos.");
    }

    Bitmap bm = imagem.getBitmapFromFile(filename);

    if ((bm != null)) {
      // bitmap lido (e criado com sucesso)

      if (mImageView != null) {
        mImageView.setImageBitmap(bm);
        Log.d(TAG, "*** N O T NULL ***");
      } else {
        Log.d(TAG, "*** NULL ***");
      }

      // exibe detalhes a respeito do bitmap criado
      imagem.showBitmapInfo(bm);

    } else {
      Log.d(TAG, "Bitmap is null (falha na criação do bitmap)");

    }

  }

  /**
   * testaAplicaMolduraFoto()
   * 
   * Testa o funcionamento da inserção de uma moldura em uma foto. Lê as fotos a
   * partir de um arquivo Cria um bitmap de overley inserindo a moldura em cima
   * da foto Exibe a foto gerada Grava o resultado
   * 
   */
  private void testaAplicaMolduraFoto() {

    // String moldura =
    // "/mnt/sdcard/Pictures/fotoevento/molduras/moldura-320x240-green.png";

    // Define uma moldura
    String moldura = "/mnt/sdcard/Pictures/fotoevento/molduras/moldura-320x240-red.png";

    // Define uma foto
    String foto = "/mnt/sdcard/Pictures/fotoevento/fotos/casa-320x240.png";

    // Lê a foto e transforma em um bitmap
    Bitmap bmFoto = imagem.getBitmapFromFile(foto);

    // Lê a moldura e transforma em um bitmap
    Bitmap bmMoldura = imagem.getBitmapFromFile(moldura);

    // Cria um bitmap sobrepondo a moldura a foto
    Bitmap bmOverlay = imagem.aplicaMolduraFoto(bmFoto, bmMoldura);

    if (bmOverlay != null) {

      imagem.exibeBitmap(mImageView, bmOverlay);

      String nomeArquivo = "/mnt/sdcard/Pictures/fotoevento/fotos/xxx.png";

      // Grava o bitmap gerado
      boolean b = imagem.gravaBitmapArquivo(bmOverlay, nomeArquivo);

      // Exibe o resultado da gravação do arquivo
      if (b) {
        Log.i(TAG, "Arquivo: " + nomeArquivo + " gravado com sucesso");
        Toast.makeText(this, "Arquivo: " + nomeArquivo + " gravado com sucesso", Toast.LENGTH_SHORT).show();
      } else {
        Log.i(TAG, "Falha na gravação do arquivo: " + nomeArquivo);
      }

    }

  }

  /**
   * testaVerticalJoin()
   * 
   * Testa o funcionamento da função verticalJoin().
   * 
   * Testa o funcionamento da criação de uma única foto a partir de três outras
   * fotos Lê a fotos a partir de um arquivo. Cria um bitmap contendo três
   * cópias da foto Exibe a foto gerada Grava o resultado
   * 
   */
  private void testaVerticalJoin() {

    String foto = "/mnt/sdcard/Pictures/fotoevento/fotos/casa-320x240.png";
    String moldura = "/mnt/sdcard/Pictures/fotoevento/molduras/moldura-cabine-132x567-green.png";

    // Lê o arquivo contendo a foto
    Bitmap bmFoto = imagem.getBitmapFromFile(foto);
    
    // Lê o arquivo contendo a moldura
    Bitmap bmMoldura = imagem.getBitmapFromFile(moldura);

    if (bmFoto != null) {
      Log.v(TAG, "Tamanho da foto original: " + getStringBitmapSize(bmFoto));
    } else {
      Log.w(TAG, "Não foi possível ler o arquivo: " + foto);
      return;
    }

    // Cria um novo bitmap a partir da repetição de 3 fotos
    // No caso estamos repetindo a mesma foto três vezes
    Bitmap bmImagem = imagem.verticalJoin(bmFoto, bmFoto, bmFoto);

    Bitmap scaledBitmap = null;

    if (bmImagem != null) {

      Log.v(TAG, "Tamanho da foto após join: " + getStringBitmapSize(bmImagem));

      // imagem.exibeBitmap(mImageView, bmImagem);

      String arqSaida = "/mnt/sdcard/Pictures/fotoevento/fotos/xxx-join.png";

      // grava a foto resultante
      boolean gravou = imagem.gravaBitmapArquivo(bmImagem, arqSaida);

      if (gravou) {

        Log.i(TAG, "testaVerticalJoin() - Arquivo: " + arqSaida + " gravado com sucesso");

        // Obtém uma imagem em escala
        scaledBitmap = imagem.getScaledBitmap(bmImagem);

        if (scaledBitmap != null) {

          Log.v(TAG, "Tamanho depois da transformação: " + getStringBitmapSize(scaledBitmap));

          // exibe a imagem escalonada
          imagem.exibeBitmap(mImageView, scaledBitmap);

          arqSaida = "/mnt/sdcard/Pictures/fotoevento/fotos/xxx2-join.png";
          
          boolean gravouImagemEscalonda = imagem.gravaBitmapArquivo(scaledBitmap, arqSaida);
          
          if (gravouImagemEscalonda) {
            Log.v(TAG, "Imagem escalonada gravada com sucesso no arquivo "+arqSaida);
          } else {
            Log.v(TAG, "Falha na gravação da imagem escalonada no arquivo: "+ arqSaida);
          }

          // combina a foto com a moldura
          Bitmap fotoCompleta = imagem.aplicaMolduraFoto(bmFoto, bmMoldura);
          
          

          // exibe a foto com a moldura
          imagem.exibeBitmap(mImageView, fotoCompleta);

          
        } else {
          Log.w(TAG, "Scaled bitmap é null");
        }

      } else {
        Log.i(TAG, "testaVerticalJoin() - Falha na gravação do arquivo: " + arqSaida);
      }

    }

  }

  /**
   * onResume()
   */
  @Override
  protected void onResume() {

    super.onResume();

    Log.d(TAG, "*** onResume() ***");

  }

  /**
   * Obtém um bitmap em escala reduzida
   * 
   * @param bm
   *          Bitmap original
   * 
   * @return Um bitmap em escala reduzida.
   */
  public Bitmap pqp(Bitmap bm) {

    if (bm == null) {
      Log.w(TAG, "Bitmap is null");
      return null;
    }

    Bitmap bm2 = imagem.getScaledBitmap(bm, 70);

    if (bm2 != null) {

      if (mImageView != null) {
        mImageView.setImageBitmap(bm2);
      }

      if (imagem != null) {
        imagem.showBitmapInfo(bm2);
        imagem.showImageViewInfo(mImageView);
      }

    }

    return bm2;

  }

  private String getClassName() {

    // retorna o nome da classe sem o pacote
    String s = getClass().getName();

    return s.substring(s.lastIndexOf("."));
  }

  /**
   * desejaSairDaAplicacao()
   * 
   * Exibe uma caixa de diálogo e a mensagem operguntando se deseja sair da
   * aplicação
   */
  private void desejaSairDaAplicacao() {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Você tem certeza que deseja sair ?").setCancelable(false)
        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {
            MyActivity7.this.finish();
          }

        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }

        });

    AlertDialog alert = builder.create();
    alert.show();

  }

  /**
   * msgDialog(String msg, String sim, String nao)
   * 
   * @param msg
   * @param sim
   * @param nao
   * 
   * @return
   */
  private int msgDialog(String msg, String sim, String nao) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage(msg);

    builder.setCancelable(false);

    builder.setPositiveButton(sim, new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialog, int id) {
        // MyActivity7.this.finish();
        dialog.dismiss();
        opcao = 1;
      }

    });

    builder.setNegativeButton(nao, new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
        opcao = 0;
      }

    });

    AlertDialog alert = builder.create();
    alert.show();

    return opcao;

  }

  /**
   * getStringBitmapSize(Bitmap bm)
   * 
   * Retorna ums string contendo o tamanho (largura x altura) do bitmap.
   *  
   * @param bm Bitmap
   * 
   * @return o tamanho (largura x altura) do bitmap ou null caso o bitmap seja nulo
   * 
   */
  public String getStringBitmapSize(Bitmap bm) {
    
    String s = null;
    
    if (bm != null) {
      
      s = bm.getWidth() + "x" + bm.getHeight();
    } else {
      Log.w(TAG, "Bitmap é nulo");
    }
    
    return s;
    
  }

}
