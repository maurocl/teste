<?php
/*
 * tela3.php
 * 
 * 
 * 
 */
include_once("conecta.php");

// Obtém a operação desejada
$acao = trim($_GET['acao']);

$ra   = trim($_GET['ra']); // RA do aluno
$nome = trim($_GET['nome']); // Nome do aluno

print "<head>";
print "<link rel=\”stylesheet\” type=\”text/css\” href=\”estilo.css\”>";
print "</head>";

define(DEBUG, 0);

if (DEBUG) {
  // exibe a lista de parâmetros recebidos
  echo "<p>Acao = $acao";
  echo "<p>RA   = $ra";
  echo "<p>Nome = $nome";
}

$codigo = array(
  "alt" => "Alteracao",
  "exc" => "Exclusao",
);

if (DEBUG) {
  echo "<p><b>$codigo[$acao]</b>";
}

if ($acao == 'alt') {
  alterar($ra, $nome);
} else if ($acao == 'exc') {
  excluir($ra, $nome);
} else {
  echo "Comando não encontrado !!!";
}

/**
 * Altera o nome do aluno associado ao ra fornecido
 *
 * @param string $ra1 RA do Aluno
 * @param string $nome1 Nome do Aluno
 */
function alterar($ra1, $nome1) {

  global $acao;
	
  titulo("Alteração");
	
  exibe($ra1,$nome1,$acao);
	
  return;
	
}

/**
 * Exclui o aluno cujo ra é fornecido
 *
 * @param string $ra1 RA do Aluno
 */
function excluir($ra1, $nome1) {

  global $acao;

  titulo("Exclusão");
	
  exibe($ra1,$nome1, $acao);
	
  return;

}

/**
 * voltar()
 
 * Cria um link que permite ao usuário retornar a página inicial
 *
 */
function voltar() {
	echo "<br><hr>";
	echo "<p><a href='tela1.html'>voltar</a>";
}

/**
 * titulo($titulo)
 *
 * Exibe um título centralizado seguido the uma linha horizontal
 *
 * @param string $titulo Título
 */
function titulo($titulo) {
	//echo "<h1 align='center'>$titulo</h1>";
	echo "<p class='titulo'>$titulo</p>";
	echo "<hr>";
}

/**
 * exibe($ra, $nome, $acao)
 *
 * Exibe informações de um aluno
 * 
 * @param string $ra RA do Aluno
 * @param string $nome Nome do Aluno
 * @param string $acao Alteração (alt) ou Exclusão (exc)
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
<h1 align='center'>Cadastro de Alunos - Ação: $acao1</h1>

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

