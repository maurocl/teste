<?php

require_once 'Categoria.php';
require_once 'Despesa.php';

class DespesaDAO {

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
   * inserir($d)
   *
   * Insere uma nova Despesa
   *
   * @param Despesa $c Nova Despesa
   */
  public function inserir(Despesa $despesa) {

    $data = $despesa->getData();
    $desc = $despesa->getDescricao();
    $valor = $despesa->getValor();
    $categoria = $despesa->getCategoria();

    $cmd="insert into despesa (data, descricao, valor, categoria) values ('$data','$desc','$valor','$categoria')";
     
    //echo "<br>cmd=[ $cmd ]";
     
    $rc = $this->getConnection()->query($cmd);

    //echo "<br>rc=$rc\n";

    $id = $this->getConnection()->insert_id;

    $despesa->setId($id);

    return $rc;

  }

  /**
   * alterar($c)
   *
   * Altera as informações de uma Despesa
   *
   * @param Despesa $c
   *
   */
  public function alterar(Despesa $despesa) {

    $id = $despesa->getId();
    $data = $despesa->getData();
    $desc = $despesa->getDescricao();
    $valor = $despesa->getValor();
    $categoria = $despesa->getCategoria();
     
    $cmd = "update despesa set descricao='$descricao', data='$data', valor=$valor, categoria='$categoria' where id=$id";

    return $this->getConnection()->query($cmd);

  }

  /**
   * excluir($despesa)
   *
   * Exclui uma Despesa
   *
   * @param Despesa $despesa
   *
   */
  public function excluir(Despesa $despesa) {

    $id = $despesa->getId();

    $cmd="delete from despesa where id=$id";

    //echo "\$cmd=[ $cmd ]";

    return $this->getConnection()->query($cmd);

  }

  /**
   * consultar($id)
   *
   * Consulta uma despesa dado seu código
   *
   * @param int $cod_categoria
   *
   * @return Um objeto Despesa ou null caso a Despesa não seja encontrada
   *
   */
  public function consultar($id) {

    $cmd = "select * from Despesa where id=$id";

    echo "<br>cmd=[$cmd]<br>";

    $result = $this->getConnection()->query($cmd);

    $c=null;

    while($linha=$result->fetch_array()) {

      $despesa = new Despesa($linha[0],$linha[1], $linha[2], $linha[3], $linha[4]);

    }

    // retorna um objeto Despesa ou null
    return $despesa;

  }


  /**
   * listarTodos()
   *
   * Obtém um array de objetos Despesa
   * ordenados por cod_categoria
   *
   * @return Uma array de objetos Despesa
   *
   */
  public function listarTodos() {

    // cria um array vazio
    $lista = Array();

    $cmd = "select * from despesa order by data";

    $result = $this->getConnection()->query($cmd);

    while($linha=$result->fetch_array()) {

      //echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

      $despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

      // insere o objeto em uma lista
      array_push($lista, $despesa);

      //echo "<p>$despesa";
      //echo "<p>$despesa->getDescricao()";

    }

    // retorna a lista de categorias
    return $lista;

  }

  /**
   * 
   * 
   * @param string $data1 Data inicial
   * @param string $data2 Data final
   */
  public function listarTodosByData($data1,$data2) {
     
    // data1 está no formato dd/mm/aaaa --> aaaammdd
    // data2 está no formato dd/mm/aaaa --> aaaammdd
    
    $fmtData1 = $this->fmtYMD($data1);
    $fmtData2 = $this->fmtYMD($data2);

    // cria um array (lista) vazio(a).
    $lista = Array();

    //$cmd = "select * from despesa between data1 and data2 order by data ";

    //$cmd="SELECT id, date_format(data,'%d/%m/%Y') dt, descricao, valor, categoria  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2'";
    
    $cmd="SELECT id, data, descricao, valor, categoria  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2'";

    //echo "<p>cmd=$cmd<br>";
    
    $result = $this->getConnection()->query($cmd);

    while($linha=$result->fetch_array()) {

      //echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

      $despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

      // insere o objeto em uma lista
      array_push($lista, $despesa);

      //echo "<p>$despesa";
      //echo "<p>$despesa->getDescricao()";

    }

    // retorna a lista de categorias
    return $lista;

  }

  /**
   * Transforma uma data no formato dd/mm/aaaa para o
   * formato aaaammdd
   * 
   * @param string $data Data no formato dd/mm/aaaa
   */
  private function fmtYMD($data) {
    
    $d = substr($data, 0,2);
    $m = substr($data, 3,2);
    $a = substr($data, 6,4);

    //echo "<br>d=$d, m=$m, a=$a<br>";

    return $a . $m . $d;
  }

}

?>