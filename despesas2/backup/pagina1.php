<?php
session_start();
echo "essa é a primeira página";
$_SESSION['nome']="mauro";
$_SESSION['data']=date('d/m/Y', time());
echo "<br><a href=\"pagina2.php\">Página 2</a>";
echo "<br>SID=" . SID . "<br>";
?>