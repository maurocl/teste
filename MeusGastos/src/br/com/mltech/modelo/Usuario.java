
package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Categoria
 * 
 * @author maurocl
 * 
 */
public class Usuario implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  private Long id;

  /**
   * 
   */
  private String username;

  /**
   * 
   */
  private String password;

  /**
   * 
   * @param _id
   * @param username
   * @param password
   */
  public Usuario(Long _id, String username, String password) {

    super();
    this.id = _id;
    this.username = username;
    this.password = password;
  }

  /**
   * 
   * @return
   */
  public Long getId() {

    return id;
  }

  /**
   * 
   * @return
   */
  public String getUsername() {

    return username;
  }

  /**
   * 
   * @return
   */
  public String getPassword() {

    return password;
  }

  /**
   * 
   * @param _id
   */
  public void setId(Long _id) {

    this.id = _id;
  }

  /**
   * 
   * @param username
   */
  public void setUsername(String username) {

    this.username = username;
  }

  /**
   * 
   * @param password
   */
  public void setPassword(String password) {

    this.password = password;
  }

  @Override
  public String toString() {

    return "Usuario [_id=" + id + ", username=" + username + ", password=" + password + "]";
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Usuario other = (Usuario) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

  /**
   * Valida o usuário e senha
   * 
   * @return true se o usuário estizer autorizado ou false caso contrário
   */
  public boolean validaUsuario() {

    if (("root".equalsIgnoreCase(this.getUsername())) && ("root").equalsIgnoreCase(this.getPassword()))
    {

      return true;
    }
    else {
      return false;
    }

  }

}
