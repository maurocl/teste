<?php
include "valida_sessao.php";

include_once 'Categoria.php';
include_once 'CategoriaDAO.php';
include_once 'DBConnection.php';

?>

<!DOCTYPE head PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>Insere Despesa</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css" >
</head>

<?php 
$con = DBConnection::getConnection();
    
$dao = new CategoriaDAO($con);

$lista = $dao->listarDescrCategoria();

$s="<select name=\"descrCategoria\">" . "<br>";

foreach ($lista as $categoria) {
  
  //echo "<br>categoria: " . $categoria;
  
  $s .= "<option>" . $categoria . "</option>" . "<br>";
  
}

$s .= "</select>";

//echo "<p>s=$s";

$con->close();

?>

<hr>
<h1 class="center">Insere Despesa</h1>
<hr>

<form name="frmInsereDespesa" method="post" action="ctrlInsereDespesa.php">

<!-- 
<p>Id:
<input type="text" name="id" value="" >
 -->

<p>Data:
<input type="text" name="data" value="" >

<p>Descrição:
<input type="text" name="descricao" value="" >

<p>Valor:
<input type="text" name="valor" value="" >

<p>Categoria:
<?php echo $s; ?>


<p>

<div class="center">
<input type="submit" name="btnSubmit" value="Inserir" >
<input type="reset"  name="btnSubmit" value="Limpar" >
</div>
</form>
