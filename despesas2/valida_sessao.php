<?php
// start session
session_start();

// check session for flag
if(isset($_SESSION['authorizedUser'])) {

  // autoriza��o existe
  //echo "<p>auth: " . $_SESSION['authorizedUser'];

  if($_SESSION['authorizedUser']==1) {

    //echo "<p>Usu�rio autorizado";
    // if flag is absent
    // the user does not have view privileges
    // print error message
    // continua
    //echo "<p>auth2: " . $_SESSION['authorizedUser'];

  }
  else {
    // vari�vel de sess�o existe por�m seu valor �
    // diferente de 1
    //echo "<p>auth3: " . $_SESSION['authorizedUser'];
    //echo "<p>FALSE";
  }

}
else {

  // vari�vel n�o existe

  //echo "You are not authorized to view this pages.";
  echo "<h1>Voc� n�o est� autorizado a visualizar essa p�gina.</h1>";
  
  echo "<p><a href=\"login.php\">Fa�a o login para usar a aplica��o</a>";

  // terminate processing
  // kick the client out
  exit();



}

?>