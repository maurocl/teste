<?php
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
<select name="descrCategoria">
<option>Descr1</option>
<option>Descr2</option>
<option>Descr3</option>
<option>Descr4</option>
</select>


<p>
<input type="submit" name="btnSubmit" value="submit" >
<input type="reset"  name="btnSubmit" value="reset" >

</form>
