
<?php


include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}
    
$dao = new DespesaDAO($con);

$lista = $dao->listarTodos();

foreach($lista as $item) {
  echo "<p><b>item</b>=$item";
}

$con->close();
 
    

exit;    


?>