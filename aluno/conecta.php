<?php

define(servidor,"localhost");

$servidor= "localhost";
$usuario = "root";
$senha   = "";
$banco   = "aluno";

/**
 * Abre uma conex�o com o banco de dados
 * 
 * @var conex�o mysqli
 */
$con=mysqli_connect($servidor, $usuario, $senha, $banco);

$num_erro=mysqli_connect_errno();

if($num_erro) {

    if($num_erro==1044) {
      echo "Base de dados: $banco nao existe";
    }
    elseif ($num_erro==1045) {
      echo "<p>Erro ($num_erro): Usuario: $usuario nao cadastrado\n";
    }
    else {
      echo "<p>Erro ($num_erro): " . mysqli_connect_error() . "\n";
    }

    die;
}

?>

