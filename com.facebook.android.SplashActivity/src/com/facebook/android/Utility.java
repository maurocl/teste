package com.facebook.android;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.provider.MediaStore;

/**
 * Classe Utilitária
 * 
 * 
 */
public class Utility extends Application {

	public static Facebook mFacebook;

	public static AsyncFacebookRunner mAsyncRunner;

	public static JSONObject mFriendsList;

	public static String userUID = null;
	public static String objectID = null;

	public static FriendsGetProfilePics model;

	public static AndroidHttpClient httpclient = null;

	public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();

	// Dimensão máxima da imagem
	private static int MAX_IMAGE_DIMENSION = 720;

	// Endereço da imagem usada
	public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";

	/**
	 * Obtem o bitmap dada sua URL
	 * 
	 * @param url URL onde se encontra o bitmap
	 * 
	 * @return o bitmap associado a URL ou null em caso de erro
	 * 
	 */
	public static Bitmap getBitmap(String url) {

		Bitmap bm = null;

		try {

			URL aURL = new URL(url);

			URLConnection conn = aURL.openConnection();

			conn.connect();

			InputStream is = conn.getInputStream();
		
			BufferedInputStream bis = new BufferedInputStream(is);

			// Cria o bitmap
			bm = BitmapFactory.decodeStream(new FlushedInputStream(is));

			bis.close();

			is.close();

		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (httpclient != null) {
				httpclient.close();
			}
			
		}
		
		return bm;

	}

	/**
	 * 
	 * Classe Estática
	 * 
	 */
	static class FlushedInputStream extends FilterInputStream {
		
		/**
		 * 
		 * @param inputStream
		 */
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		/**
		 * 
		 */
		public long skip(long n) throws IOException {
			
			long totalBytesSkipped = 0L;
			
			while (totalBytesSkipped < n) {
			
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				
				if (bytesSkipped == 0L) {
				
					int b = read();
					
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
					
				}
				
				totalBytesSkipped += bytesSkipped;
				
			}
			
			// nº total de bytes "pulados"
			return totalBytesSkipped;
			
		}
		
	}

	/**
	 * Redimensiona uma imagem
	 * 
	 * @param context contexto da aplicação
	 * @param photoUri URI da foto
	 * 
	 * @return um array de bytes com a imagem escalonada
	 * 
	 * @throws IOException
	 * 
	 */
	public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {

		InputStream is = context.getContentResolver().openInputStream(photoUri);

		BitmapFactory.Options dbo = new BitmapFactory.Options();

		dbo.inJustDecodeBounds = true;

		// Decode an input stream into a bitmap. 
		// If the input stream is null, or cannot be used to decode a bitmap, 
		// the function returns null. 
		// The stream's position will be where ever
		// it was after the encoded data was read.
		BitmapFactory.decodeStream(is, null, dbo);

		// fecha o stream
		is.close();

		int rotatedWidth, rotatedHeight;
		
		int orientation = getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270) {
			
			// The resulting height of the bitmap, set independent of the state of inJustDecodeBounds. 
			// However, if there is an error trying to decode, 
			// outHeight will be set to -1.
			
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
			
		} else {
			
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
			
		}

		Bitmap srcBitmap;
		
		is = context.getContentResolver().openInputStream(photoUri);

		// se a largura ou altura for maior que a dimensão máxima da imagem
		if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
			
			// calcula a taxa de redução da largura
			float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
			
			// calcula a taxa de redução da altura
			float heightRatio = ((float) rotatedHeight)	/ ((float) MAX_IMAGE_DIMENSION);
			
			// calcula o maior taxa de redução
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			
			options.inSampleSize = (int) maxRatio;
			
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
			
		} else {
			
			// Decode an input stream into a bitmap
			srcBitmap = BitmapFactory.decodeStream(is);
			
		}
		
		is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0) {
			
			Matrix matrix = new Matrix();
			
			// Postconcats the matrix with the specified rotation. M' = R(degrees) * M
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
			
		}

		String type = context.getContentResolver().getType(photoUri);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		if (type.equals("image/png")) {
			
			srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			
		} else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
			
			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			
		}
		
		byte[] bMapArray = baos.toByteArray();
		
		baos.close();
		
		return bMapArray;
		
	}

	/**
	 * Obtém a orientação da tela
	 * 
	 * @param context contexto da aplicação
	 * @param photoUri URI da foto
	 * 
	 * @return
	 */
	public static int getOrientation(Context context, Uri photoUri) {

		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(
				photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		
		return cursor.getInt(0);
		
	}

}
