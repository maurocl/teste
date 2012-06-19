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
 * Essa activity é responsável por obter as configurações possíveis de uma
 * câmera de vídeo
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
			Log.w(TAG, "hardware não está disponvel");
			return;
		}

		EditText edit = (EditText) findViewById(R.id.editText1);

		CameraTools.checkCameraFeatures(this);

		Log.i(TAG, "Nº de câmeras: " + CameraTools.getNumCameras());

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
	 * Lê todos os parâmetros de configuração da câmera e exibe em um campo de
	 * texto
	 * 
	 * @param edit
	 *          componente onde as informações serão exibidas
	 * @param hash
	 *          HashMap contendo a lista de parâmetros e seus respectivos valores
	 *          válidos
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
	 * Lê todos os parâmetros de configuração da câmera e exibe em um campo de
	 * texto e grava o resultado em um arquivo texto.
	 * 
	 * @param edit
	 *          componente onde as informações serão exibidas
	 * 
	 * @param hash
	 *          HashMap contendo a lista de parâmetros e seus respectivos valores
	 *          válidos
	 * 
	 * 
	 * @param hash
	 */
	private void exibeGravaParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

		if (hash == null) {
			Log.w(TAG, "lista de parâmetros está vazia");
			return;
		}

		// percorre a lista de chaves
		for (String chave : hash.keySet()) {

			StringBuilder sb = new StringBuilder();

			sb.append(chave).append(";");

			// adiciona a informação
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
	 * Grava a string no arquivo. O arquivo será armazenado no diretório dado por:
	 * Environment.getExternalStorageDirectory()
	 * 
	 * @param s
	 *          String
	 * @param filename
	 *          nome do arquivo
	 */
	private void gravaString(String s, String filename) {

		// diretório
		// File dirs =
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File dirs = Environment.getExternalStorageDirectory();

		// cria o arquivo no diretório especificado
		File f = new File(dirs, filename);

		if (dirs.mkdirs()) {
			Log.d(TAG, "diretório " + dirs.getAbsolutePath() + " foi criado");
		} else {
			if (dirs.exists()) {
				Log.d(TAG, "diretório " + dirs.getAbsolutePath() + " NÃO foi criado pois já existe");
			} else {
				Log.w(TAG, "diretório NÃO foi criado");
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

			// exibe o nº de caracteres gravados
			Log.d(TAG, s.getBytes().length + " caracteres gravados");

			Log.d(TAG, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso");

			Toast.makeText(this, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso", Toast.LENGTH_SHORT).show();

		} catch (FileNotFoundException e) {
			Log.w(TAG, "Arquivo: " + f.getAbsolutePath() + " não foi encontrado", e);

		} catch (IOException e) {
			Log.w(TAG, "Erro na gravação do arquivo: " + f.getAbsolutePath(), e);
		}

	}

}