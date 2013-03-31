<?php
include "valida_sessao.php";

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

// Identificador da despesa
$id = $_GET['id'];

$con = DBConnection::getConnection();

$dao = new DespesaDAO($con);

// busca a consulta que será alterada
$despesa = $dao->consultar($id);

$daoCategoria = new CategoriaDAO($con);

$lista = $daoCategoria->listarDescrCategoria();

// obtém a categoria da despesa
$idCategoria = $despesa->getCategoria();

$s="<select name=\"categoria\">" . "<br>";

foreach ($lista as $categoria) {

  $valores = explode("|",$categoria);

  $id2 = $valores[1];
  $descr2 = $valores[0];

  //echo "<br>categoria: " . $categoria . ", id2=$id2, descr=$descr2";

  if($id2==$idCategoria) {
    //echo "<br>igual";
    $s .= "<option value=\"$id2\" selected>" . $descr2 . "</option>" . "<br>";
  }
  else {
    $s .= "<option value=\"$id2\"  >" . $descr2 . "</option>" . "<br>";
    //echo "<br>diferente";
    //$s .= "<option selected>" . $categoria . "</option>" . "<br>";
  }

}

$s .= "</select>";

?>

<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Alteração de Despesa</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
</head>

<hr>
<h1>Altera Despesa</h1>
<hr>

<form name="frmAlteraDespesa" method="post"
	action="confirmaAlteracaoDespesa.php">

<p>Id: <input type="text" name="id"
	value="<?php echo $despesa->getId();?>">

<p>Data: <input type="text" name="data"
	value="<?php echo $dao->fmtDMA($despesa->getData());?>"></p>

<p>Descrição: <input type="text" name="descricao"
	value="<?php echo $despesa->getDescricao();?>">

<p>Valor: <input type="text" name="valor"
	value="<?php echo $despesa->getValor();?>"></p>

<p>Categoria: <?php echo $s; ?></p>

<div class="center">
<p><input type="submit" name="btnSubmit" value="Alterar"> <input
	type="reset" name="btnSubmit" value="Cancelar"></p>
</div>

</form>

<?php

$con->close();

?>