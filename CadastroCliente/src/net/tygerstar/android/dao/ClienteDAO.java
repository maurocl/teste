package net.tygerstar.android.dao;

import java.util.ArrayList;
import java.util.List;

import net.tygerstar.android.entidade.Cliente;
import net.tygerstar.android.negocio.Funcoes;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * <b>ClienteDAO</b><br>
 * 
 * Implementa o DAO para classe Cliente.
 *  
 * @version 1.0
 * 
 * @See {@link #listar()}
 * 
 *
 */
public class ClienteDAO {

  /**
   * Cria uma lista de clientes
   * 
   * @return Uma lista de clientes
   * 
   */
  public List<Cliente> listar() {

    List<Cliente> clientes = new ArrayList<Cliente>();

    Cursor c = Funcoes.database.query("cliente", Cliente.colunas, null, null, null, null, "nome");

    if (c.moveToFirst()) {

      do {

        // cria um novo cliente
        Cliente cli = new Cliente();

        cli.setId(c.getInt(0));
        cli.setNome(c.getString(1));
        cli.setEndereco(c.getString(2));
        cli.setBairro(c.getString(3));
        cli.setCidade(c.getString(4));
        cli.setEstado(c.getString(5));

        // adiciona o cliente a lista
        clientes.add(cli);

      } while (c.moveToNext());

    }

    // a lista de clientes
    return clientes;

  }

  /**
   * Salva um cliente
   * 
   * @param cliente
   * 
   * @return
   */
  public long salvar(Cliente cliente) {

    ContentValues cv = new ContentValues();

    cv.put("nome", cliente.getNome());
    cv.put("endereco", cliente.getEndereco());
    cv.put("bairro", cliente.getBairro());
    cv.put("cidade", cliente.getCidade());
    cv.put("estado", cliente.getEndereco());

    long retorno = -1;

    if (cliente.getId() == 0) {

      retorno = Funcoes.database.insert("cliente", null, cv);

    } else {

      cv.put("_id", cliente.getId());

      retorno = Funcoes.database.update("cliente", cv, "_id = ?", new String[] { "" + cliente.getId() });

    }

    return retorno;

  }

}
