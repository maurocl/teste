<?php
session_start();
echo "essa é a segunda página<br>";
echo "nome: " . $_SESSION['nome'] . "<br>";
echo "data: " . $_SESSION['data'] . "<br>";
echo "<br><a href=\"pagina1.php\">Página 1</a>";

echo "<br>";
echo "session_cache_expire(): " . session_cache_expire() . "<br>";

echo "session_cache_limiter(): " . session_cache_limiter() . "<br>";

echo "session_cache_expire(): " . session_cache_expire() . "<br>";

echo "session_encode(): " . session_encode() . "<br>";

echo "session_id(): " . session_id() . " - len: " . strlen(session_id()) . "<br>";

echo "session_module_name(): " . session_module_name() . "<br>";

echo "session_save_path(): " . session_save_path() . "<br>";

unset($_SESSION['authorizedUser']);

?>