package br.com.mltech;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * VerificaFuncCameraActivity
 * 
 * Essa activity � respons�vel por obter as configura��es poss�veis de uma
 * c�mera de v�deo
 * 
 * @author maurocl
 * 
 */
public class VerificaFuncCameraActivity extends Activity {

	public static final String TAG = "VerificaFuncCameraActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.d(TAG, "*** onCreate() ***");

		boolean isCameraAvailable = CameraTools.checkCameraHardware(this);

		if (!isCameraAvailable) {
			Log.w(TAG, "hardware n�o est� disponvel");
			return;
		}

		EditText edit = (EditText) findViewById(R.id.editText1);

		CameraTools.checkCameraFeatures(this);

		Log.i(TAG, "N� de c�meras: " + CameraTools.getNumCameras());

		Camera c = CameraTools.getCameraInstance();

		HashMap<String, List<String>> hash = CameraTools.getParametersDetail(c);

		exibeParametrosCamera(edit, hash);
		exibeGravaParametrosCamera(edit, hash);

		if (c != null) {
			// Disconnects and releases the Camera object resources.
			c.release();
		}

	}

	/**
	 * exibeParametrosCamera(EditText edit, HashMap<String, List<String>> hash)
	 * 
	 * L� todos os par�metros de configura��o da c�mera e exibe em um campo de
	 * texto
	 * 
	 * @param edit
	 *          componente onde as informa��es ser�o exibidas
	 * @param hash
	 *          HashMap contendo a lista de par�metros e seus respectivos valores
	 *          v�lidos
	 */
	private void exibeParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

		int num = 0;

		for (String chave : hash.keySet()) {

			num++;

			List<String> listaValues = hash.get(chave);

			Log.d(TAG, "chave(" + num + ")=" + chave);
			edit.append("chave(" + num + ")=" + chave + "\n");

			if (listaValues != null) {

				for (String s2 : listaValues) {
					Log.d(TAG, "  value=" + s2);
					edit.append("  value=" + s2 + "\n");
				}

			}

		}
	}

	/**
	 * y(EditText edit, HashMap<String, List<String>> hash)
	 * 
	 * L� todos os par�metros de configura��o da c�mera e exibe em um campo de
	 * texto e grava o resultado em um arquivo texto.
	 * 
	 * @param edit
	 *          componente onde as informa��es ser�o exibidas
	 * 
	 * @param hash
	 *          HashMap contendo a lista de par�metros e seus respectivos valores
	 *          v�lidos
	 * 
	 * 
	 * @param hash
	 */
	private void exibeGravaParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

		if (hash == null) {
			Log.w(TAG, "lista de par�metros est� vazia");
			return;
		}

		// percorre a lista de chaves
		for (String chave : hash.keySet()) {

			StringBuilder sb = new StringBuilder();

			sb.append(chave).append(";");

			// adiciona a informa��o
			edit.append(chave);
			edit.append(";");

			// percorre a lista de valores
			List<String> listaValues = hash.get(chave);

			if (listaValues != null) {

				for (String valor : listaValues) {

					sb.append(valor + ";");
					edit.append(valor + ";");

				}

			}

			// pula uma linha
			edit.append("\n");

			// escreve a linha
			Log.d(TAG, sb.toString());

		}

		String s = edit.getText().toString();

		gravaString(s, "data.txt");

	}

	/**
	 * gravaString(String s, String filename)
	 * 
	 * Grava a string no arquivo. O arquivo ser� armazenado no diret�rio dado por:
	 * Environment.getExternalStorageDirectory()
	 * 
	 * @param s
	 *          String
	 * @param filename
	 *          nome do arquivo
	 */
	private void gravaString(String s, String filename) {

		// diret�rio
		// File dirs =
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File dirs = Environment.getExternalStorageDirectory();

		// cria o arquivo no diret�rio especificado
		File f = new File(dirs, filename);

		if (dirs.mkdirs()) {
			Log.d(TAG, "diret�rio " + dirs.getAbsolutePath() + " foi criado");
		} else {
			if (dirs.exists()) {
				Log.d(TAG, "diret�rio " + dirs.getAbsolutePath() + " N�O foi criado pois j� existe");
			} else {
				Log.w(TAG, "diret�rio N�O foi criado");
			}
		}

		FileOutputStream stream = null;

		try {

			stream = new FileOutputStream(f);

			DataOutputStream dos = new DataOutputStream(stream);

			// grava o texto no arquivo
			dos.write(s.getBytes());

			// fecha o strean
			dos.close();

			// exibe o n� de caracteres gravados
			Log.d(TAG, s.getBytes().length + " caracteres gravados");

			Log.d(TAG, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso");

			Toast.makeText(this, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso", Toast.LENGTH_SHORT).show();

		} catch (FileNotFoundException e) {
			Log.w(TAG, "Arquivo: " + f.getAbsolutePath() + " n�o foi encontrado", e);

		} catch (IOException e) {
			Log.w(TAG, "Erro na grava��o do arquivo: " + f.getAbsolutePath(), e);
		}

	}

}