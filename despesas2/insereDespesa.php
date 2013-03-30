<?php

include_once 'Categoria.php';
include_once 'CategoriaDAO.php';
include_once 'DBConnection.php';

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
<h1>Insere Despesa</h1>
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
<input type="submit" name="btnSubmit" value="submit" >
<input type="reset"  name="btnSubmit" value="reset" >

</form>
