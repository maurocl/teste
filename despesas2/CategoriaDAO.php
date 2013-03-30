<?php

require_once 'Categoria.php';

/**
 * CategoriaDAO
 *
 * Objeto de acesso a dados de Categoria
 *
 * @author maurocl
 *
 */
class CategoriaDAO {

  private $con;

  /**
   * __construct($con)
   *
   * Método construtor
   *
   * @param unknown_type $con
   */
  public function __construct($con) {
    $this->con=$con;
  }

  /**
   * getConnection()
   *
   * Enter description here ...
   */
  public function getConnection() {
    return $this->con;
  }

  /**
   * inserir($c)
   *
   * Insere uma nova Categoria
   *
   * @param Categoria $c Nova categoria
   */
  public function inserir(Categoria $c) {

    $desc = $c->getDescricao();

    $cmd="insert into categoria (descricao) values ('$desc')";
     
    //echo "<br>cmd=[ $cmd ]";
     
    $rc = $this->getConnection()->query($cmd);

    //echo "<br>rc=$rc\n";

    $id = $this->getConnection()->insert_id;

    $c->setId($id);

    return $rc;

  }

  /**
   * alterar($c)
   *
   * Altera as informações de uma Categoria
   *
   * @param Categoria $c
   *
   */
  public function alterar(Categoria $c) {

    $id = $c->getId();
    $descricao = $c->getDescricao();
     
    $cmd = "update categoria set descricao='$descricao' where id=$id";

    return $this->getConnection()->query($cmd);
    
  }

  /**
   * excluir($c)
   *
   * Exclui uma Categoria
   *
   * @param Categoria $c
   *
   */
  public function excluir(Categoria $c) {

    $id = $c->getId();

    $cmd="delete from categoria where id=$id";

    //echo "\$cmd=[ $cmd ]";

    return $this->getConnection()->query($cmd);

  }

  /**
   * consultar($cod_categoria)
   *
   * Consulta uma categoria dado seu código de acesso
   *
   * @param int $cod_categoria
   *
   * @return Um objeto Categoria ou null caso a categoria não seja encontrada
   *
   */
  public function consultar($id) {

    $cmd = "select * from categoria where id=$id";

    //echo "<br>cmd=[$cmd]<br>";

    $result = $this->getConnection()->query($cmd);

    $c=null;

    while($linha=$result->fetch_array()) {

      $c = new Categoria($linha[0],$linha[1]);

    }

    // retorna um objeto categoria ou null
    return $c;

  }


  /**
   * listarTodos()
   *
   * Obtém um array de objetos Categoria
   * ordenados pela descrição da categoria
   *
   * @return Uma array de objetos Categoria
   *
   */
  public function listarTodos() {

    // cria um array vazio
    $lista = Array();

    $cmd = "select * from categoria order by descricao";

    $result = $this->getConnection()->query($cmd);

    while($linha=$result->fetch_array()) {

      $c = new Categoria($linha[0], $linha[1]);

      // insere o objeto em uma lista
      array_push($lista, $c);

    }

    // retorna a lista de categorias
    return $lista;

  }

  /**
   * listarDescrCategoria()
   *
   * Obtém um array de Strings onde cada elemento é
   * formado pela descrição da categoria + "|" +
   * o código da categoria
   *
   * @return Uma array de strings onde cada posicao ...
   *
   */
  public function listarDescrCategoria() {

    $lista = Array();

    $cmd = "select id, descricao from categoria order by descricao";

    $result = $this->getConnection()->query($cmd);

    if($result!=null) {

      while($linha=$result->fetch_array()) {

        $cod_categoria = $linha[0];
        $descricao     = $linha[1];

        $s = $descricao . "|" . $cod_categoria;

        //$hash[$descricao] = $cod_categoria;

        // insere o objeto em uma lista
        array_push($lista, $s);

      }

    }

    // retorna a lista de categorias
    //return $hash;
    return $lista;

  }

  /**
   * listarCategoriaDescr
   *
   * Obtém um array associativo onde a chave é dada
   * pelo codigo da categoria e o valor é a descrição da
   * categoria
   *
   * @return Uma array associativo
   *
   */
  public function listarCategoriaDescr() {

    $cmd = "select id, descricao from categoria order by descricao";

    $result = $this->getConnection()->query($cmd);

    while($linha=$result->fetch_array()) {

      $cod_categoria = $linha[0];
      $descricao     = $linha[1];

      // insere o objeto em uma lista
      $hash[$cod_categoria]=$descricao;

    }

    // retorna a lista de categorias
    return $hash;

  }

}

?>