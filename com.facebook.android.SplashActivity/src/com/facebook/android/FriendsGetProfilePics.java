
package com.facebook.android;

import java.util.Hashtable;
import java.util.Stack;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

/*
 * Fetch friends profile pictures request via AsyncTask
 */
public class FriendsGetProfilePics {

  Hashtable<String, Bitmap> friendsImages;

  Hashtable<String, String> positionRequested;

  BaseAdapter listener;

  int runningCount = 0;

  // Pilha
  Stack<ItemPair> queue;

  /*
   * 15 max async tasks at any given time.
   */
  final static int MAX_ALLOWED_TASKS = 15;

  /**
   * Construtor
   */
  public FriendsGetProfilePics() {

    friendsImages = new Hashtable<String, Bitmap>();

    positionRequested = new Hashtable<String, String>();

    queue = new Stack<ItemPair>();

  }

  /*
   * Inform the listener when the image has been downloaded. listener is
   * FriendsList here.
   */
  public void setListener(BaseAdapter listener) {

    this.listener = listener;

    reset();

  }

  /**
   * Limpa as variáveis:
   */
  public void reset() {

    positionRequested.clear();

    runningCount = 0;

    queue.clear();

  }

  /*
   * If the profile picture has already been downloaded and cached, return it
   * else execute a new async task to fetch it - if total async tasks >15, queue
   * the request.
   */
  public Bitmap getImage(String uid, String url) {

    Bitmap image = friendsImages.get(uid);

    if (image != null) {
      return image;
    }

    // verifica se a posição requisitada está no cache
    if (!positionRequested.containsKey(uid)) {

      // posicão não está no cache

      // atualiza a posição
      positionRequested.put(uid, "");

      if (runningCount >= MAX_ALLOWED_TASKS) {

        // enfileira o par de item
        queue.push(new ItemPair(uid, url));

      } else {

        // incrementa o contador de posições ocupadas
        runningCount++;

        // executa uma thread para obtem a foto do usuário 
        new GetProfilePicAsyncTask().execute(uid, url);

      }

    }

    return null;

  }

  /**
   * Obtém a próxima imagem
   */
  public void getNextImage() {

    // se a fila não estiver vazia ;;;
    if (!queue.isEmpty()) {

      // retira um elemento da fila
      ItemPair item = queue.pop();

      // executa uma thread para obtem a foto do usuário
      new GetProfilePicAsyncTask().execute(item.uid, item.url);

    }

  }

  /*
   * Start a AsyncTask to fetch the request
   */
  private class GetProfilePicAsyncTask extends AsyncTask<Object, Void, Bitmap> {

    String uid;

    @Override
    /**
     * Override this method to perform a computation on a background thread
     */
    protected Bitmap doInBackground(Object... params) {

      // atualiza o user id (uid)
      this.uid = (String) params[0];

      // atualiza a URL 
      String url = (String) params[1];

      // Retorna o bitmap localizado na URL
      return Utility.getBitmap(url);

    }

    @Override
    /**
     * Runs on the UI thread after doInBackground(Params...). 
     * 
     * The specified result is the value returned by doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result) {

      // decrementa o contador ...
      runningCount--;

      if (result != null) {

        // imagem foi encontrada
        friendsImages.put(uid, result);

        listener.notifyDataSetChanged();

        // obtém a próxima imagem
        getNextImage();

      }

    }

  }

  /**
   * Par de Itens (uid e url)
   * 
   * 
   */
  class ItemPair {

    String uid; // user id

    String url; // URL

    /**
     * Constroi um novo par formado pelo uid e a url
     * 
     * @param uid
     *          identificador do usuário
     * @param url
     *          url onde se encontra a foto usada no perfil do usuário
     * 
     */
    public ItemPair(String uid, String url) {

      this.uid = uid;
      this.url = url;
    }

  }

}
