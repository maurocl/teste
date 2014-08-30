<?php

include_once("conecta.php");

$acao = trim($_POST['acao']);
$ra   = trim($_POST['ra']);
$nome = trim($_POST['nome']);

define(DEBUG, 0);

if (DEBUG) {
  // exibe a lista de par�metros recebidos
  echo "<p>Acao = $acao";
  echo "<p>RA   = $ra";
  echo "<p>Nome = $nome";
}

// Lista de op��es
$codigo = array(
    "adi" => "Adição",
    "alt" => "Alteração",
    "exc" => "Exclusão",
    "con" => "Consulta",
    "lis" => "Listar"
);

if (DEBUG) {
  // exibe o código da operação
  echo "<p><b>$codigo[$acao]</b>";
}

if ($acao == 'adi') {
  adicionar($ra, $nome);
} else if ($acao == 'alt') {
  alterar($ra, $nome);
} else if ($acao == 'exc') {
  excluir($ra);
} else if ($acao == 'con') {
  consultar($ra);
} else if ($acao == 'lis') {
  listar2();
} else {
  echo "Comando não encontrado !!!";
}


/**
 * adicionar($ra1, $nome1)

 * Adiciona um novo aluno
 *
 * @param String $ra1
 * @param String $nome1
 */
function adicionar($ra1, $nome1) {

  global $con;

  $erro = 0;

  if (!isset($ra1) || empty($ra1)) {
    echo "<p>RA n�o foi fornecido";
    $erro = 1;
  }

  if (!isset($nome1) || empty($ra1)) {
    echo "<p>Nome não foi fornecido";
    $erro = 2;
  }

  if ($erro == 0) {

    titulo("Insersão de novo aluno");

    $cmd = "insert into aluno (ra,nome) values ('$ra1', '$nome1')";

    if (DEBUG) {
      echo "<p>cmd=$cmd";
    }

    $res = mysqli_query($con, $cmd);

    $erro = mysqli_errno($con);

    if ($erro) {
      echo "<p>erro ($erro): " . mysqli_error($con);
    }

  } else {

    echo "<p>Aluno não foi inserido";

  }

  voltar();

}

/**
 * alterar($ra1, $nome1)

 * Altera o nome do aluno associado ao ra fornecido
 *
 * @param string $ra1
 * @param string $nome1
 *
 */
function alterar($ra1, $nome1) {

  titulo("Alteração");

  global $con;

  $cmd = "update aluno set nome='$nome1' where ra='$ra1'";

  if (DEBUG) {
    echo "<p>cmd=$cmd";
  }

  $res = mysqli_query($con, $cmd);

  // obtém o número de linhas afetados pela operação
  $num = mysqli_affected_rows($con);

  echo "<p>linhas=$num";

  if ($num > 0) {

    echo "<p>$ra1: nome alterado para $nome1.";

  } elseif ($num == 0) {

    echo "<p>Aluno: $ra1 n�o foi encontrado";

  } elseif ($num == -1) {

    echo "<p>Erro";
    $erro = mysqli_errno($con);
    echo " - erro ($erro): " . mysqli_error($con);

  }

  voltar();
}

/**
 * excluir($ra1)

 * Exclui o aluno a partir de seu ra.
 *
 * @param string $ra1
 *
 */
function excluir($ra1) {

  titulo("Exclusão");

  global $con;

  $cmd = "delete from aluno where ra='$ra1'";

  if (DEBUG) {
    echo "<p>cmd=$cmd";
  }

  if (!empty($ra1)) {

    $res = mysqli_query($con, $cmd);

    $num = mysqli_affected_rows($con);

    echo "<p>linhas=$num";

    if ($num > 0) {

      echo "<p>$ra1: $nome foi excluido.";

    } elseif ($num == 0) {

      echo "<p>Aluno: $ra1 não foi encontrado";

    } elseif ($num == -1) {

      echo "<p>Erro";
      $erro = mysqli_errno($con);
      echo " - erro ($erro): " . mysqli_error($con);

    }

  } else {
    echo "<p>RA não foi fornecido.";
  }

  voltar();

}

/**
 * consultar($ra1)

 * Consulta as informações de um aluno dado seu RA
 *
 * @param string $ra1
 *
 */
function consultar($ra1) {

  titulo("Consulta");

  $cmd = "select ra, nome from aluno where ra='$ra1'";

  if (DEBUG) {
    echo "<p>cmd=$cmd";
  }

  global $con;

  $res = mysqli_query($con, $cmd);

  echo "linhas=" . $res->num_rows;

  if ($res->num_rows > 0) {

    $dados = mysqli_fetch_assoc($res);

    $ra = $dados['ra'];
    $nome = $dados['nome'];

    echo "<p>$ra: $nome";

  } else {

    echo "<p>Aluno: $ra1 não foi encontrado";

  }

  voltar();
}

/**
 * listar()
 *
 * Lista todos alunos cadastrados na base de dados classificados
 * por nome.
 *
 */
function listar() {

  titulo("Consulta todos alunos");

  $cmd = "select ra, nome from aluno order by nome";

  if (DEBUG) {
    echo "<p>cmd=$cmd";
  }

  global $con;

  $res = mysqli_query($con, $cmd);

  // obtém o nº de linhas retornados pela consulta
  $num = $res->num_rows;

  echo "<p>Numero de linhas: $num";

  echo "<table border=1>";

  while ($dados = mysqli_fetch_assoc($res)) {

    $ra = $dados['ra'];
    $nome = $dados['nome'];

    echo "<tr>";

    echo "<td>$ra</td>";
    echo "<td>$nome</td>";


    echo "</tr>";
  }

  echo "</table>";

  voltar();
}


/**
 * listar2()
 *
 * Lista todos alunos cadastrados na base de dados
 *
 */
function listar2() {

  titulo("Consulta todos alunos");

  $cmd = "select ra, nome from aluno order by nome";

  if (DEBUG) {
    echo "<p>cmd=$cmd";
  }

  global $con;

  $res = mysqli_query($con, $cmd);

  $num = $res->num_rows;

  echo "<p>Número de linhas: $num";

  echo "<table border=1>";

  while ($dados = mysqli_fetch_assoc($res)) {

    $ra = $dados['ra'];
    $nome = $dados['nome'];

    echo "<tr>";

    echo "<td>$ra</td>";
    echo "<td>$nome</td>";
    echo "<td><a href=\"tela3.php?ra=$ra&nome=$nome&acao=alt\">edita</a></td>";
    echo "<td><a href=\"tela3.php?ra=$ra&nome=$nome&acao=exc\">remove</a></td>";


    echo "</tr>";
  }

  echo "</table>";

  voltar();
}


/**
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
 * Exibe um título centralizado seguido de uma linha horizontal
 *
 * @param string $titulo
 */
function titulo($titulo) {
  //echo "<h1 align='center'>$titulo</h1>";
  echo "<p class='titulo'>$titulo</p>";
  echo "<hr>";
}

?>
