/*
 * Copyright 2010 Facebook, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.facebook.android;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Utility class supporting the Facebook Object.
 * 
 * Classe utilitária de suporte ao objeto Facebook
 * 
 * @author ssoneff@facebook.com
 * 
 */
public final class Util {

  /**
   * Set this to true to enable log output. Remember to turn this back off
   * before releasing. Sending sensitive data to log is a security risk.
   */
  private static boolean ENABLE_LOG = true;

  /**
   * Generate the multi-part post body providing the parameters and boundary
   * string
   * 
   * @param parameters
   *          the parameters need to be posted
   * @param boundary
   *          the random string as boundary
   * @return a string of the post body
   */
  public static String encodePostBody(Bundle parameters, String boundary) {

    if (parameters == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    for (String key : parameters.keySet()) {

      Object parameter = parameters.get(key);

      if (!(parameter instanceof String)) {
        continue;
      }

      sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + (String) parameter);

      sb.append("\r\n" + "--" + boundary + "\r\n");

    }

    return sb.toString();
  }

  /**
   * Codifica uma URL
   * 
   * @param parameters
   *          bundle contendo os parâmetros para codificação
   * 
   * @return a string codificada
   */
  public static String encodeUrl(Bundle parameters) {

    if (parameters == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    boolean first = true;

    // percorre o conjunto formado por todas as chaves
    for (String key : parameters.keySet()) {

      // objeto o valor associado a chave
      Object parameter = parameters.get(key);

      if (!(parameter instanceof String)) {
        // valor não é uma String, então processo a próxima chave
        continue;
      }

      if (first) {
        first = false;
      } else {
        sb.append("&");
      }

      sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(parameters.getString(key)));

    }

    return sb.toString();

  }

  /**
   * Decodifica uma URL contida em uma String
   * 
   * @param s
   *          String contendo a URL
   * 
   * @return um Bundle com os pares chave ==> valor
   */
  public static Bundle decodeUrl(String s) {

    Bundle params = new Bundle();

    if (s != null) {

      // cria um array separado por "&"
      String array[] = s.split("&");

      // para cada parâmetro do array
      for (String parameter : array) {

        // cria um array para cada lado da atribuição (chave=valor)
        String v[] = parameter.split("=");

        if (v.length == 2) {
          // insere uma nova string no bundle cuja chave e valor é dado pelo vetor criado
          params.putString(URLDecoder.decode(v[0]), URLDecoder.decode(v[1]));
        }

      }

    }

    // um bundle contendo chaves e valores
    return params;

  }

  /**
   * Parse a URL query and fragment parameters into a key-value bundle.
   * 
   * @param url
   *          the URL to parse
   *          
   * @return a dictionary bundle of keys and values
   */
  public static Bundle parseUrl(String url) {

    // hack to prevent MalformedURLException
    // the sequence to replace, the replacement sequence
    url = url.replace("fbconnect", "http");

    try {

      // cria uma nova URL
      URL u = new URL(url);

      // cria um bundle a partir da query da URL
      Bundle b = decodeUrl(u.getQuery());

      b.putAll(decodeUrl(u.getRef()));

      // retorna um bundle contendo chaves e valors
      return b;

    } catch (MalformedURLException e) {

      return new Bundle();

    }

  }

  /**
   * Connect to an HTTP URL and return the response as a string.
   * 
   * Note that the HTTP method override is used on non-GET requests. (i.e.
   * requests are made as "POST" with method specified in the body).
   * 
   * @param url
   *          - the resource to open: must be a welformed URL
   * @param method
   *          - the HTTP method to use ("GET", "POST", etc.)
   * @param params
   *          - the query parameter for the URL (e.g. access_token=foo)
   * 
   * @return the URL contents as a String
   * 
   * @throws MalformedURLException
   *           - if the URL format is invalid
   * @throws IOException
   *           - if a network problem occurs
   */
  public static String openUrl(String url, String method, Bundle params) throws MalformedURLException, IOException {

    // random string as boundary for multi-part http post
    String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";

    // caracteres terminadores de linha
    String endLine = "\r\n";

    // Stream de saída
    OutputStream os;

    // se o método for GET
    if (method.equals("GET")) {
      // acrescenta a string query
      url = url + "?" + encodeUrl(params);
    }

    Util.logd("Facebook-Util", method + " URL: " + url);

    // Abre uma conexão HTTP
    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

    // Atribui o USer-Agent como FacebookAndroidSDK
    conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " FacebookAndroidSDK");

    // se o método não for GET
    if (!method.equals("GET")) {

      // cria um novo bundle
      Bundle dataparams = new Bundle();

      for (String key : params.keySet()) {

        Object parameter = params.get(key);

        if (parameter instanceof byte[]) {
          dataparams.putByteArray(key, (byte[]) parameter);
        }

      }

      // use method override
      if (!params.containsKey("method")) {
        params.putString("method", method);
      }

      if (params.containsKey("access_token")) {
        String decoded_token = URLDecoder.decode(params.getString("access_token"));
        params.putString("access_token", decoded_token);
      }

      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + strBoundary);

      // Sets the flag indicating whether this URLConnection allows output. 
      // It cannot be set after the connection is established.
      conn.setDoOutput(true);

      // Sets the flag indicating whether this URLConnection allows input. 
      // It cannot be set after the connection is established.
      conn.setDoInput(true);

      // Sets the value of the specified request header field.
      conn.setRequestProperty("Connection", "Keep-Alive");

      conn.connect();

      // opens a connection to the resource
      os = new BufferedOutputStream(conn.getOutputStream());

      os.write(("--" + strBoundary + endLine).getBytes());
      os.write((encodePostBody(params, strBoundary)).getBytes());
      os.write((endLine + "--" + strBoundary + endLine).getBytes());

      if (!dataparams.isEmpty()) {

        for (String key : dataparams.keySet()) {
          os.write(("Content-Disposition: form-data; filename=\"" + key + "\"" + endLine).getBytes());
          os.write(("Content-Type: content/unknown" + endLine + endLine).getBytes());
          os.write(dataparams.getByteArray(key));
          os.write((endLine + "--" + strBoundary + endLine).getBytes());

        }
      }

      os.flush();

    }

    String response = "";

    try {
      response = read(conn.getInputStream());
    } catch (FileNotFoundException e) {
      // Error Stream contains JSON that we can parse to a FB error
      response = read(conn.getErrorStream());
    }

    return response;

  }

  /**
   * Lê de um stream
   * 
   * @param in
   *          input stream
   * 
   * @return os dados lidos do stream
   * 
   * @throws IOException
   */
  private static String read(InputStream in) throws IOException {

    StringBuilder sb = new StringBuilder();

    BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);

    for (String line = r.readLine(); line != null; line = r.readLine()) {
      sb.append(line);
    }

    in.close();

    return sb.toString();

  }

  /**
   * Clear cookies
   * 
   * @param context
   *          contexto da aplicação
   * 
   */
  public static void clearCookies(Context context) {

    // Edge case: an illegal state exception is thrown if an instance of
    // CookieSyncManager has not be created. CookieSyncManager is normally
    // created by a WebKit view, but this might happen if you start the
    // app, restore saved state, and click logout before running a UI
    // dialog in a WebView -- in which case the app crashes

    @SuppressWarnings("unused")
    CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);

    CookieManager cookieManager = CookieManager.getInstance();

    cookieManager.removeAllCookie();

  }

  /**
   * Parse a server response into a JSON Object. This is a basic implementation
   * using org.json.JSONObject representation. More sophisticated applications
   * may wish to do their own parsing.
   * 
   * The parsed JSON is checked for a variety of error fields and a
   * FacebookException is thrown if an error condition is set, populated with
   * the error message and error type or code if available.
   * 
   * @param response
   *          - string representation of the response
   * 
   * @return the response as a JSON Object
   * 
   * @throws JSONException
   *           - if the response is not valid JSON
   * 
   * @throws FacebookError
   *           - if an error condition is set
   */
  public static JSONObject parseJson(String response) throws JSONException, FacebookError {

    // Edge case: when sending a POST request to /[post_id]/likes
    // the return value is 'true' or 'false'. Unfortunately
    // these values cause the JSONObject constructor to throw
    // an exception.
    if (response.equals("false")) {
      throw new FacebookError("request failed");
    }

    if (response.equals("true")) {
      response = "{value : true}";
    }

    JSONObject json = new JSONObject(response);

    // errors set by the server are not consistent
    // they depend on the method and endpoint
    if (json.has("error")) {
      JSONObject error = json.getJSONObject("error");
      throw new FacebookError(error.getString("message"), error.getString("type"), 0);
    }

    if (json.has("error_code") && json.has("error_msg")) {
      throw new FacebookError(json.getString("error_msg"), "", Integer.parseInt(json.getString("error_code")));
    }

    if (json.has("error_code")) {
      throw new FacebookError("request failed", "", Integer.parseInt(json.getString("error_code")));
    }

    if (json.has("error_msg")) {
      throw new FacebookError(json.getString("error_msg"));
    }

    if (json.has("error_reason")) {
      throw new FacebookError(json.getString("error_reason"));
    }

    return json;

  }

  /**
   * Display a simple alert dialog with the given text and title.
   * 
   * @param context
   *          Android context in which the dialog should be displayed
   * @param title
   *          Alert dialog title
   * @param text
   *          Alert dialog message
   */
  public static void showAlert(Context context, String title, String text) {

    Builder alertBuilder = new Builder(context);

    alertBuilder.setTitle(title);

    alertBuilder.setMessage(text);

    alertBuilder.create().show();

  }

  /**
   * A proxy for Log.d api that kills log messages in release build. It not
   * recommended to send sensitive information to log output in shipping apps.
   * 
   * @param tag
   *          tag
   * @param msg
   *          mensagem
   */
  public static void logd(String tag, String msg) {

    if (ENABLE_LOG) {
      Log.d(tag, msg);
    }

  }

  /**
   * 
   * @param tag
   * @param msg
   */
  public static void logi(String tag, String msg) {

    if (ENABLE_LOG) {
      Log.i(tag, msg);
    }

  }

  /**
   * 
   * @param tag
   * @param msg
   */
  public static void logw(String tag, String msg) {

    if (ENABLE_LOG) {
      Log.w(tag, msg);
    }

  }

  /**
   * 
   * @param tag
   * @param msg
   */
  public static void logv(String tag, String msg) {

    if (ENABLE_LOG) {
      Log.d(tag, msg);
    }

  }

}
