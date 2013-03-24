<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

// Identificador da despesa
$id = $_GET['id'];

echo "<p>ctrlAlteraDespesa";

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}

$dao = new DespesaDAO($con);

// busca a consulta que será alterada
$despesa = $dao->consultar($id);

// exibe informações sobre a despesa
echo "<p>Despesa: " . $despesa ;

$daoCategoria = new CategoriaDAO($con);

$lista = $daoCategoria->listarDescrCategoria();

// obtém a categoria da despesa
$idCategoria = $despesa->getCategoria();

echo "<p>Buscando id categoria: $idCategoria";

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

//echo "<p>s=$s";

//$con->close();

?>

<hr>
<h1>Altera Despesa</h1>
<hr>

<form name="frmAlteraDespesa" method="post" action="confirmaAlteracaoDespesa.php">

<p>Id: <input type="text" name="id"
	value="<?php echo $despesa->getId();?>">

<p>Data: <input type="text" name="data"
	value="<?php echo $dao->fmtDMA($despesa->getData());?>">

<p>Descrição: <input type="text" name="descricao"
	value="<?php echo $despesa->getDescricao();?>">

<p>Valor: <input type="text" name="valor"
	value="<?php echo $despesa->getValor();?>">

<p>Categoria: <?php echo $s; ?>

<p><input type="submit" name="btnSubmit" value="submit"> <input
	type="reset" name="btnSubmit" value="reset">

</form>

<?php

//$dao->alterar($despesa);

//echo "<p>Registro alterado id: " . $despesa->getId();

$con->close();

?>