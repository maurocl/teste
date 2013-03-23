<?php
// form not submitted
if (!$_POST['submit']) {
?>

<html>

<head>
<title>Login</title>
</head>

<body>

<hr>
<h1>Sistema de Controle de Despesas</h1>
<hr>

<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST"><br>

<br>Username: <input type="text" size="10" name="username"><br>

<br>Password: <input type="password" size="10" name="password"><br>

<p align="center">
<input type="submit" name="submit" value="Logar">
<input type="reset" name="reset" value="Cancelar">
</p>

</form>

</body>

<hr>
<p align="center">Copyright (2013) - MLTech</p>
<hr>


</html>

<?php
} else {

	// form submitted

	// check for username

	// check for password

	// assign to variables and escape

	$inputUser = $_POST['username'];
	$inputPass = $_POST['password'];
	
	//echo "<p>user=[$inputUser]";
	//echo "<p>pass=[$inputPass]";

	// connect and execute SQL query
	$connection = mysql_connect("localhost","root","") or die ("Unable to connect!");

	mysql_select_db("mydb");

	$query="select id from users where username = '$inputUser' and password='$inputPass'";

	$result = mysql_query($query, $connection) or die ("Error in query: $query. " . mysql_error());

	if (mysql_num_rows($result)==1) {
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

?>