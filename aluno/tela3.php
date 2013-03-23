<?php
/*
 * tela3.php
 * 
 * 
 * 
 */
include_once("conecta.php");

// Obt�m a opera��o desejada
$acao = trim($_GET['acao']);

$ra   = trim($_GET['ra']);
$nome = trim($_GET['nome']);

define(DEBUG, 0);

if (DEBUG) {
	// exibe a lista de par�metros recebidos
	echo "<p>Acao = $acao";
	echo "<p>RA   = $ra";
	echo "<p>Nome = $nome";
}

$codigo = array(
    "alt" => "Altera��o",
    "exc" => "Exclus�o",
);

if (DEBUG) {
	echo "<p><b>$codigo[$acao]</b>";
}

if ($acao == 'alt') {
	alterar($ra, $nome);
} else if ($acao == 'exc') {
	excluir($ra, $nome);
} else {
	echo "Comando n�o encontrado !!!";
}

/**
 * alterar($ra1, $nome1)

 * Altera o nome do aluno associado ao ra fornecido
 *
 * @param string $ra1
 * @param string $nome1
 */
function alterar($ra1, $nome1) {

	titulo("Altera��o");

	global $acao;
	
	exibe($ra1,$nome1,$acao);
	
	return;
	
}

/**
 * excluir($ra1, $nome)

 * Exclui o aluno cujo ra � fornecido
 *
 * @param unknown_type $ra1
 */
function excluir($ra1, $nome1) {

	titulo("Exclus�o");

	global $acao;
	
	exibe($ra1,$nome1, $acao);
	
	return;

}

/**
 * voltar()
 * 
 * Cria um link que permite ao usu�rio retornar a p�gina inicial
 *
 */
function voltar() {
	echo "<br><hr>";
	echo "<p><a href='tela1.html'>voltar</a>";
}

/**
 * titulo($titulo)
 *
 * Exibe um t�tulo centralizado seguido the uma linha horizontal
 *
 * @param string $titulo
 */
function titulo($titulo) {
	//echo "<h1 align='center'>$titulo</h1>";
	echo "<p class='titulo'>$titulo</p>";
	echo "<hr>";
}

/**
 * 
 * Exibe informa��es de um aluno
 * 
 * @param string $ra RA do Aluno
 * @param string $nome Nome do Aluno
 * @param string $acao Altera��o (alt) ou Exclus�o (exc)
 * 
 */
function exibe($ra, $nome, $acao) {
  
  $acao1 = $acao;
  $ra1   = $ra;
  $nome1 = $nome;
  
  if($acao=="alt") {
    $btnSubmit = "Alterar";    
  }
  elseif ($acao="exc") {
    $btnSubmit = "Excluir";    
  }
  
echo <<<TELA
<h1 align='center'>Cadastro de Alunos - A��o: $acao1</h1>

<hr>

<form name="form" method="post" action="tela2.php">

<p>RA:   <input name='ra'   value="$ra1"   type='text'></p>

<p>Nome: <input name='nome' value="$nome1" type='text'></p>

         <input name='acao' value="$acao1" type='hidden'>

<p>
<input type='submit' value="$btnSubmit"> 
<a href="tela1.html">voltar</a>
</p>

</form>

<hr>
TELA;

}

?>

