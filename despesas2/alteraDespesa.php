<?php

include_once 'Categoria.php';
include_once 'CategoriaDAO.php';
include_once 'DBConnection.php';


$data = $_POST['data'];
$descricao = $_POST['descricao'];
$valor= $_POST['valor'];
$categoria = $_POST['categoria'];

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}
    
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
<h1>Altera Despesa</h1>
<hr>

<form name="frmAlteraDespesa" method="post" action=".php">

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
<!-- 
<select name="descrCategoria">
<option>Descr1</option>
<option>Descr2</option>
<option>Descr3</option>
<option>Descr4</option>
</select>
 -->
 <?php echo $s; ?>


<p>
<input type="submit" name="btnSubmit" value="submit" >
<input type="reset"  name="btnSubmit" value="reset" >

</form>
