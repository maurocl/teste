<?php

include 'Categoria.php';
include 'CategoriaDAO.php';
include 'DBConnection.php';

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}
    
$dao = new CategoriaDAO($con);

$lista = $dao->listarTodos();

foreach($lista as $item) {
  echo "<p><b>item</b>=$item";
}

$con->close();
 
    

exit;    


function pqp() {
$cmd = "select * from categoria";
 $result = $con->query($cmd);

    while($linha=$result->fetch_array()) {

      //echo "<br>$linha[0], $linha[1]";
      
      $c = new Categoria($linha[0], $linha[1]);
      echo "<br>c=$c<br>";
      
    }
}    



?>