<?php
session_start();
echo "essa � a primeira p�gina";
$_SESSION['nome']="mauro";
$_SESSION['data']=date('d/m/Y', time());
echo "<br><a href=\"pagina2.php\">P�gina 2</a>";
echo "<br>SID=" . SID . "<br>";
?>