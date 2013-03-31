<?php
// start session
session_start();

// check session for flag
if(isset($_SESSION['authorizedUser'])) {

  // autorização existe
  //echo "<p>auth: " . $_SESSION['authorizedUser'];

  if($_SESSION['authorizedUser']==1) {

    //echo "<p>Usuário autorizado";
    // if flag is absent
    // the user does not have view privileges
    // print error message
    // continua
    //echo "<p>auth2: " . $_SESSION['authorizedUser'];

  }
  else {
    // variável de sessão existe porém seu valor é
    // diferente de 1
    //echo "<p>auth3: " . $_SESSION['authorizedUser'];
    //echo "<p>FALSE";
  }

}
else {

  // variável não existe

  //echo "You are not authorized to view this pages.";
  echo "<h1>Você não está autorizado a visualizar essa página.</h1>";
  
  echo "<p><a href=\"login.php\">Faça o login para usar a aplicação</a>";

  // terminate processing
  // kick the client out
  exit();



}

?>