<?php

include_once 'Constantes.php';
include_once 'DBConnection.php';



// form not submitted
if (!$_POST['submit']) {
?>

<html>

<head>
<title>Login</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
</head>

<body>

<hr>
<h1>Sistema de Controle de Despesas</h1>
<hr>

<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST"><br>
<br>
Username: <input type="text" size="10" name="username"><br>
<br>
Password: <input type="password" size="10" name="password"><br>

<p align="center"><input type="submit" name="submit" value="Logar"> <input
	type="reset" name="reset" value="Cancelar"></p>

</form>

</body>

<hr>
<p class="rodape">Copyright 2013 - MLTech</p>
<hr>

</html>

	<?php
} else {

	$inputUser = $_POST['username'];
	$inputPass = $_POST['password'];

	//echo "<p>user=[$inputUser]";
	//echo "<p>pass=[$inputPass]";

	$con = DBConnection::getConnection();

	if($con==null) {
		//echo "<p>conn é null";
	}
	else {
		//echo "<p>conn NÃO é null";
	}

	$cmd="select id from users where username = '$inputUser' and password='$inputPass'";

	//echo "<br>cmd=[$cmd]";

	//$result = $con->getConnection()->query($cmd);
	$result = $con->query($cmd);

	if($result) {

		if($result->num_rows==1) {
			 
			while($linha=$result->fetch_array()) {

				$id = $linha[0];

			}

			// if row exists
			// user/pass combination is correct
			// start a session
			session_start();

			// register a session variable
			$_SESSION['authorizedUser']=1;

			// redirect browser to protected resource
			header("Location: success.php");


		}
		else {
			// if row does not exist
			// user/pass combination is wrong
			// redirect browser to error page
			header("Location: fail.php");

		}

	}
	else {
		// if row does not exist
		// user/pass combination is wrong
		// redirect browser to error page
		header("Location: fail.php");
	}
	
}

?>