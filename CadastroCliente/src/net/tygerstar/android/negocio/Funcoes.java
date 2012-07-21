package net.tygerstar.android.negocio;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.tygerstar.android.R;
import net.tygerstar.android.entidade.Cliente;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.util.Log;


public class Funcoes {
  
	public static SQLiteDatabase database = null;
	
	public static LocationManager locationManager;
	
	public static NumberFormat formatoCoordenada = new DecimalFormat("###0.00000");
	
	/**
	 * Descompacta o arquivo
	 * 
	 * @param arquivo
	 * 
	 * @return Um array de strings
	 * 
	 */
	public static List<String> descompactarArquivo(InputStream arquivo) {
	  
		ArrayList<String> lista = new ArrayList<String>();
		
		try {
		  
			GZIPInputStream zipado = new GZIPInputStream(arquivo);
			
			Reader reader = new InputStreamReader(zipado, "ISO8859_1");
			
			BufferedReader br = new BufferedReader(reader);
			
			String linha = "";
			
			while ((linha = br.readLine()) != null){
				lista.add(linha);
			}
			
		} catch (Exception e) {
		  
			Log.e("Funcoes.descompactarArquivo", e.getMessage());
			
		}
		
		return lista;
		
	}
	
	/**
	 * Compacta o arquivo
	 *  
	 * @param conteudo
	 * @param context
	 * 
	 * @return
	 */
	public static boolean compactarArquivo(String conteudo, Context context){
	  
		try {
		  
			//FileOutputStream fos = new FileOutputStream(new File("arquivo.gz"));
			
		  FileOutputStream fos = context.openFileOutput("Arquivo.gz", Context.MODE_WORLD_WRITEABLE);
			GZIPOutputStream zipado = new GZIPOutputStream(fos);	
			
			byte[] saida = conteudo.getBytes("ISO8859_1");			
			
			zipado.write(saida);
			zipado.finish();
			
			zipado.close();
			
			fos.close();
			
			return true;
			
		} catch (Exception e) {
		  
			Log.e("Funcoes.compactarArquivo", e.getMessage());
			
		}
		
		return false;
		
	}
	
	/**
	 * Lê um array de bytes de um InsputStream
	 *  
	 * @param is InputStream
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream is) throws IOException {
	  
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try{
		  
			byte[] buffer = new byte[1024];
			
			int tam;
			
			while ((tam = is.read(buffer)) > 0){
				baos.write(buffer, 0, tam);
			}
			
			return baos.toByteArray();
			
		}finally{
		  
			baos.close();
			is.close();
			
		}
		
	}
	
	/**
	 * Lê uma string de um InputString
	 * 
	 * @param is InputString
	 * 
	 * @return
	 */
	public static String readString(InputStream is){
	  
		try{
		  
			byte[] bytes = Funcoes.readBytes(is);
			
			String texto = new String(bytes);
			
			return texto;
			
		}catch(IOException e){
			
		}
		
		return null;
		
	}
	
	/**
	 * Exporta a lista de clientes
	 * 
	 * @param clientes Lista de Clientes
	 * @param context Contexto da aplicação
	 * 
	 */
	public static void exportar(List<Cliente> clientes, Context context){
	  
	  StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < clientes.size(); i++){
		  
			Cliente c = clientes.get(i);
			
			sb.append("CLIENTE|N=").append(c.getNome());
			sb.append("|E=").append(c.getEndereco()).append("|C=").append(c.getCidade()).append("\n");
			
		}
		
		compactarArquivo(sb.toString(), context);
		
	}
	
	/**
	 * Cria uma mensagem de erro na tela
	 * 
	 * @param Context contexto
	 * @param String mensagem
	 * 
	 * @return AlertDialog.Builder
	 * 
	 */
	public static AlertDialog.Builder msgERRO(Context contexto, String mensagem){
	  
		AlertDialog.Builder alerta = new AlertDialog.Builder(contexto);
		
		alerta.setTitle("Erro !");
		alerta.setMessage(mensagem);
		alerta.setIcon(R.drawable.negativo);
		
		alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		});
		
		return alerta;
		
	}
	
	/**
	 * Retorna o status do GPS do aparelho (ativado ou desativado)
	 * 
	 * @return boolean Retorna se o GPS estÃ¡ ativado ou nÃ£o
	 */
	public static boolean gpsAtivado(){
	  
		boolean ativado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || 
				locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		return ativado;
		
	}
		
	
	/**
	 * Verifica se o banco existe, senÃ£o, cria as tabelas do banco
	 * 
	 * @return String Mensagem de retorno 
	 */
	public static String inicializarBanco(){
	  
		//Comando para criação das tabelas do banco
    	String sqlCriarBanco = "CREATE TABLE cliente(" +
    			"_id integer primary key autoincrement, " +
    			"nome text not null, " +
    			"endereco text not null, " +
    			"bairro text not null, " +
    			"cidade text not null, " +
    			"estado char(2));";
    	
    	//Comando para apagar as tabelas do banco
    	String sqlApagarBanco = "DROP TABLE IF EXISTS cliente";
    	
    	String msgRegorno = "";
    	    	
    	//Testa pra ver se a tabela existe
    	try{
    	  
    		Funcoes.database.query("cliente", Cliente.colunas, null, null, null, null, null);
    		
    		msgRegorno = "Usando tabelas já criadas.";
    		
    	}catch(Exception e){
    	  
    		//Se as tabelas não existirem ou ocorrer um erro no select
    		//apaga as tabelas
    		Funcoes.database.execSQL(sqlApagarBanco);
    		
    		//Depois recria com o comando correto
    		Funcoes.database.execSQL(sqlCriarBanco);
    		msgRegorno = "Tabelas criadas";
    		
    	}
    	
    	return msgRegorno;   
    	
    }
	
}
