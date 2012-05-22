package br.com.mltech;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
public class MyActivity7 extends Activity implements OnClickListener {

  public static final String TAG = "MyActivity7";

  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";
  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  int numConfirmar;
  int numCancelar;

  Bitmap mBitmap;
  ImageView mImageView;

  ManipulaImagem imagem;

  int num;

  static int sNum;

  Button btn1;
  Button btn2;
  Button btn3;

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

    btn1 = (Button) findViewById(R.id.btnConfirmar);
    btn1.setOnClickListener(this);

    btn2 = (Button) findViewById(R.id.btnCancelar);
    btn2.setOnClickListener(this);

    btn3 = (Button) findViewById(R.id.btnFim);
    btn3.setOnClickListener(this);

    imagem = new ManipulaImagem();

    mImageView = (ImageView) findViewById(R.id.imageView1);

    Bitmap bi = imagem.getBitmapFromFile(PATH_FOTOS + "bia-480x640.png");

    Log.d(TAG, "bi: " + imagem.getStringBitmapSize(bi));

    Bitmap bi2 = imagem.getScaledBitmap2(bi, 240, 320);

    Log.d(TAG, "bi2: " + imagem.getStringBitmapSize(bi2));

    Bitmap bi3 = xxx(bi2);

    imagem.exibeBitmap(mImageView, bi3);

    showDisplayMetrics(this.getResources().getDisplayMetrics());
    
    imagem.showBitmapInfo(bi3);

  }

  Bitmap xxx(Bitmap bitmap) {

    // Cria um novo bitmap
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

    Canvas canvas = new Canvas(output);

    final Paint paint = new Paint();

    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    
    paint.setAntiAlias(true);

    // canvas.drawARGB(0, 0, 0, 0);
    //canvas.drawARGB(0, 255, 255, 255);
    canvas.drawARGB(150, 150, 150, 150);

    final Rect rect2 = new Rect(0, 0, 199, 199);

    
    boolean b = canvas.clipRect(20, 20, 220, 220);
    if (!b) {
      Log.d(TAG, "false");
    }
   
    canvas.drawBitmap(bitmap, rect, rect, paint);
    
    return output;

  }

  /**
   * onClick(View v)
   * 
   * Trata os eventos dos botões
   * 
   */
  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    if (v == btn1) {
      numConfirmar++;
      Log.d(TAG, "Botão Confirmar pressionado: " + numConfirmar);
      mImageView.setVisibility(ImageView.INVISIBLE);

    } else if (v == btn2) {
      numCancelar++;
      Log.d(TAG, "Botão Cancelar pressionado: " + numCancelar);
      mImageView.setVisibility(ImageView.VISIBLE);
    } else if (v == btn3) {
      Log.d(TAG, "Botão Executar pressionado: ");
      executaAcao();

    }

  }

  /**
   * executaAcao()
   */
  private void executaAcao() {

    Log.i(TAG, "---> testaVerticalJoin()");
    testaVerticalJoin();

    // showInfoApps();

    // desejaSairDaAplicacao();

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

    String filename = PATH_MOLDURAS + "moldura-320x240-green.png";

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
    String moldura = PATH_MOLDURAS + "moldura-320x240-red.png";

    // Define uma foto
    String foto = PATH_FOTOS + "casa-320x240.png";

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
   * fotos. Carrega as imagens a partir de um arquivo localizado em memória
   * externa (sdcard).
   * 
   * Cria um bitmap contendo três cópias da foto Exibe a foto gerada Grava o
   * resultado
   * 
   */
  private void testaVerticalJoin() {

    // Foto 1
    String foto1 = PATH_FOTOS + "img-3x4-blue.png";
    Bitmap bmFoto1 = carregaFoto(foto1);

    if (bmFoto1 == null) {
      // Log.w(TAG, "Não foi possível ler o arquivo: " + foto1);
      return;
    }

    // Foto 2
    String foto2 = PATH_FOTOS + "img-3x4-green.png";

    Bitmap bmFoto2 = carregaFoto(foto2);
    if (bmFoto2 == null) {
      // Log.w(TAG, "Não foi possível ler o arquivo: " + foto2);
      return;
    }

    // Foto 3
    String foto3 = PATH_FOTOS + "img-3x4-yellow.png";

    Bitmap bmFoto3 = carregaFoto(foto3);
    if (bmFoto3 == null) {
      // Log.w(TAG, "Não foi possível ler o arquivo: " + foto3);
      return;
    }

    // Lê o arquivo contendo a moldura

    String arqModura = "moldura-cabine-132x568-red.png";

    String moldura = PATH_MOLDURAS + arqModura;

    Bitmap bmMoldura = imagem.getBitmapFromFile(moldura);

    if (bmMoldura != null) {
      Log.v(TAG, " ==> Tamanho da moldura original: " + getStringBitmapSize(bmMoldura));
    } else {
      Log.w(TAG, "Não foi possível ler o arquivo: " + moldura);
      return;
    }

    // =========================================================================
    // Cria um novo bitmap a partir da composição das 3 fotos
    // A foto será repetida na vertical, isto é, uma nova foto
    // será colocada embaixo da outra.
    // =========================================================================
    Bitmap bmImgJoin = imagem.verticalJoin(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin != null) {
      Log.i(TAG, "Imagens foram juntadas com sucesso");
      Log.v(TAG, " ==> Tamanho da foto após join: " + getStringBitmapSize(bmImgJoin));
      // imagem.exibeBitmap(mImageView, bmImagem);
    } else {
      Log.w(TAG, "Erro no merge das três fotos");
      return;
    }

    Bitmap scaledBitmap = null;

    String arqSaida = PATH_FOTOS + "xxx-join.png";

    // grava a foto das imagens "juntada"
    boolean gravou = imagem.gravaBitmapArquivo(bmImgJoin, arqSaida);

    if (!gravou) {
      Log.w(TAG, "Erro na gravação do arquivo contendo as três fotos: " + arqSaida);
      return;
    } else {

      Log.i(TAG, "testaVerticalJoin() - Arquivo: " + arqSaida + " gravado com sucesso");
    }

    // Obtém uma imagem em escala
    // scaledBitmap = imagem.getScaledBitmap(bmImgJoin);
    scaledBitmap = imagem.getScaledBitmap2(bmImgJoin, 113, 453);

    if (scaledBitmap == null) {
      //
      return;
    }

    Log.v(TAG, "Tamanho depois do escalonamento: " + getStringBitmapSize(scaledBitmap));

    // exibe a imagem escalonada
    imagem.exibeBitmap(mImageView, scaledBitmap);

    arqSaida = PATH_FOTOS + "xxx2-join.png";

    boolean gravouImagemEscalonda = imagem.gravaBitmapArquivo(scaledBitmap, arqSaida);

    if (gravouImagemEscalonda) {

      // imagem escalonada gravada com sucesso
      Log.v(TAG, "Imagem escalonada gravada com sucesso no arquivo " + arqSaida);
    } else {
      Log.v(TAG, "Falha na gravação da imagem escalonada no arquivo: " + arqSaida);
      return;
    }

    // combina a foto com a moldura
    Bitmap fotoComMoldura = imagem.aplicaMolduraFoto(scaledBitmap, bmMoldura);

    // exibe a foto com a moldura
    imagem.exibeBitmap(mImageView, fotoComMoldura);

    arqSaida = PATH_FOTOS + "xxx3-join-moldura.png";

    boolean gravouImagemComMoldura = imagem.gravaBitmapArquivo(fotoComMoldura, arqSaida);

    if (gravouImagemComMoldura) {

      // imagem escalonada gravada com sucesso
      Log.v(TAG, "Imagem com moldura gravada com sucesso no arquivo " + arqSaida);
    } else {
      Log.v(TAG, "Falha na gravação da imagem com moldura no arquivo: " + arqSaida);
      return;
    }

  }

  /**
   * carregaFoto(String foto)
   * 
   * @param foto
   *          Caminho onde encontrar a foto
   * 
   * @return Um bitmap contendo a foto ou null caso a foto não seja encontrada
   * 
   */
  private Bitmap carregaFoto(String foto) {

    Bitmap bmFoto = imagem.getBitmapFromFile(foto);

    if (bmFoto != null) {
      Log.v(TAG, " ==> Tamanho da foto original: " + getStringBitmapSize(bmFoto));
    } else {
      Log.w(TAG, "Não foi possível ler o arquivo: " + foto);
    }

    return bmFoto;

  }

  /**
   * getScaled70Bitmap(Bitmap bm)
   * 
   * Reduz as dimensções de um bitmap em 30%
   * 
   * @param bm
   *          Bitmap original
   * 
   * @return Um bitmap em escala reduzida ou null no caso de erro.
   */
  public Bitmap getScaled70Bitmap(Bitmap bm) {

    if (bm == null) {
      Log.w(TAG, "getScaledBitmap() - Bitmap is null");
      return null;
    }

    Bitmap bm2 = imagem.getScaledBitmap(bm, 70);

    return bm2;

  }

  /**
   * getClassName()
   * 
   * @return uma string contendo o nome da classe
   * 
   */
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
   * getStringBitmapSize(Bitmap bm)
   * 
   * Retorna uma string contendo o tamanho (largura x altura) do bitmap.
   * 
   * @param bm
   *          Bitmap
   * 
   * @return o tamanho (largura x altura) do bitmap ou null caso o bitmap seja
   *         nulo
   * 
   */
  public String getStringBitmapSize(Bitmap bm) {

    String s = null;

    if (bm != null) {

      s = bm.getWidth() + "x" + bm.getHeight();
    } else {
      Log.w(TAG, "getStringBitmapSize() - Bitmap é nulo");
    }

    return s;

  }

  /**
   * showInfoApps()
   */
  private void showInfoApps() {

    Application app = MyActivity7.this.getApplication();

    Context ctx = MyActivity7.this.getApplicationContext();

    ApplicationInfo appInfo = MyActivity7.this.getApplicationInfo();

    Log.v(TAG, "getApplication()=" + MyActivity7.this.getApplication());
    Log.v(TAG, "getApplicationContext()=" + MyActivity7.this.getApplicationContext());

    Log.v(TAG, "getApplicationInfo()=" + MyActivity7.this.getApplicationInfo());
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

}
