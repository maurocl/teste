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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.android.Facebook.DialogListener;

/**
 * Facebook Dialog
 * 
 * 
 */
public class FbDialog extends Dialog {

  /**
	 * Facebook Blue
	 */
  static final int FB_BLUE = 0xFF6D84B4;

  /**
	 * 
	 */
  static final float[] DIMENSIONS_DIFF_LANDSCAPE = { 20, 60 };

  /**
	 * 
	 */
  static final float[] DIMENSIONS_DIFF_PORTRAIT = { 40, 60 };

  /**
	 * 
	 */
  static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.FILL_PARENT,
      ViewGroup.LayoutParams.FILL_PARENT);

  // tamanho da margem
  static final int MARGIN = 4;

  // tamanho do padding
  static final int PADDING = 2;

  //
  static final String DISPLAY_STRING = "touch";

  //
  static final String FB_ICON = "icon.png";

  // 
  private String mUrl;

  //
  private DialogListener mListener;

  //
  private ProgressDialog mSpinner;

  //
  private ImageView mCrossImage;

  //
  private WebView mWebView;

  //
  private FrameLayout mContent;

  /**
   * Construtor
   * 
   * @param context contexto da aplica��o
   * @param url URL
   * @param listener listner respons�vel pelo tratamento de eventos do di�logo
   * 
   */
  public FbDialog(Context context, String url, DialogListener listener) {

    super(context, android.R.style.Theme_Translucent_NoTitleBar);

    mUrl = url;

    mListener = listener;

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // cria uma caixa de di�logo
    mSpinner = new ProgressDialog(getContext());

    mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);

    mSpinner.setMessage("Loading...");

    requestWindowFeature(Window.FEATURE_NO_TITLE);

    mContent = new FrameLayout(getContext());

    /*
     * Create the 'x' image, but don't add to the mContent layout yet at this
     * point, we only need to know its drawable width and height to place the
     * webview
     */
    createCrossImage();

    /*
     * Now we know 'x' drawable width and height, layout the webivew and add it
     * the mContent layout
     */
    int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();

    setUpWebView(crossWidth / 2);

    /*
     * Finally add the 'x' image to the mContent layout and add mContent to the
     * Dialog view
     */
    mContent.addView(mCrossImage, new LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));

  }

  /**
   * Cria um image view contendo uma cruz. Associa um listener de click na
   * imagem.
   * 
   */
  private void createCrossImage() {

    // Retrieve the Context this Dialog is running in.
    mCrossImage = new ImageView(getContext());

    // Dismiss the dialog when user click on the 'x'
    mCrossImage.setOnClickListener(new View.OnClickListener() {

      public void onClick(View v) {

        // executa a funcionalidade associada ao cancelamento
        mListener.onCancel();

        // dismiss the dialog
        FbDialog.this.dismiss();

      }

    });

    // Obtem o drawable
    Drawable crossDrawable = getContext().getResources().getDrawable(
        R.drawable.close);

    // associa a imagem
    mCrossImage.setImageDrawable(crossDrawable);

    /*
     * 'x' should not be visible while webview is loading make it visible only
     * after webview has fully loaded
     */
    mCrossImage.setVisibility(View.INVISIBLE);

  }

  /**
   * Estabele uma WebView com um determinado tamanho de margem
   * 
   * @param margin
   *          tamanho da margem
   * 
   */
  private void setUpWebView(int margin) {

    LinearLayout webViewContainer = new LinearLayout(getContext());

    mWebView = new WebView(getContext());

    mWebView.setVerticalScrollBarEnabled(false);
    mWebView.setHorizontalScrollBarEnabled(false);

    mWebView.setWebViewClient(new FbDialog.FbWebViewClient());

    mWebView.getSettings().setJavaScriptEnabled(true);

    mWebView.loadUrl(mUrl);
    mWebView.setLayoutParams(FILL);
    mWebView.setVisibility(View.INVISIBLE);

    // a margem � aplicada a esquerda, a direita, ao topo e ao fundo
    webViewContainer.setPadding(margin, margin, margin, margin);

    // adiciona 
    webViewContainer.addView(mWebView);

    //
    mContent.addView(webViewContainer);

  }

  /**
   * Facebook web view client
   * 
   * 
   */
  private class FbWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

      Util.logd("Facebook-WebView", "Redirect URL: " + url);

      if (url.startsWith(Facebook.REDIRECT_URI)) {

        // cria um bundle com os valores parseados da url
        Bundle values = Util.parseUrl(url);

        String error = values.getString("error");

        if (error == null) {
          error = values.getString("error_type");
        }

        if (error == null) {

          // nenhum erro foi retornado. Executa o m�todo associado a execu��o com sucesso.
          mListener.onComplete(values);

        } else if (error.equals("access_denied") || error.equals("OAuthAccessDeniedException")) {

          // acesso negado - executa m�todo associado ao cancelamento da opera��o
          mListener.onCancel();

        } else {

          // cria um erro associado ao Facebook
          mListener.onFacebookError(new FacebookError(error));

        }

        // dismiss the dialog
        FbDialog.this.dismiss();

        return true;

      } else if (url.startsWith(Facebook.CANCEL_URI)) {

        mListener.onCancel();

        FbDialog.this.dismiss();

        return true;

      } else if (url.contains(DISPLAY_STRING)) {

        return false;

      }

      // launch non-dialog URLs in a full browser
      getContext().startActivity(
          new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

      return true;

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

      super.onReceivedError(view, errorCode, description, failingUrl);

      mListener.onError(new DialogError(description, errorCode, failingUrl));

      FbDialog.this.dismiss();

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

      Util.logd("Facebook-WebView", "Webview loading URL: " + url);

      // Notify the host application that a page has started loading.
      super.onPageStarted(view, url, favicon);

      mSpinner.show();

    }

    @Override
    public void onPageFinished(WebView view, String url) {

      // Notify the host application that a page has finished loading.
      super.onPageFinished(view, url);

      mSpinner.dismiss();

      /*
       * Once webview is fully loaded, set the mContent background to be
       * transparent and make visible the 'x' image.
       */
      mContent.setBackgroundColor(Color.TRANSPARENT);
      
      mWebView.setVisibility(View.VISIBLE);
      
      mCrossImage.setVisibility(View.VISIBLE);

    }

  }

}
