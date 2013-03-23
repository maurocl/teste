Este � um exemplo simples de uma aplica��o desenvolvida em PHP
usando banco de dados (MySQL).

A aplica��o possui apenas uma tabela (aluno). Veja a descri��o da tabela abaixo:

create table aluno (
  ra      char(7) not null,
  nome c  har(30) not null,
  primary key (ra),
  unique (ra)
);

conecta.php

  Este arquivo implementa a fun��o que atualiza a vari�vel $con
  com uma conex�o (se for poss�vel).
  Para criar uma conex�o usando MySQL s�o necess�rias as seguintes
  informa��es:
  
  $servidor= "localhost"; // nome do servidor no banco de dados
  $usuario = "root"; // nome do usu�rio
  $senha   = ""; // senha do usu�rio
  $banco   = "test"; // nome  do database onde est�o as tabelas do banco

tela1.html --> tela2.php --> tela3.php

tela1.html
  Tela inicial da apli��o. � um forml�rio onde � selecionada a
  a��o desejada (inclus�o, altera��o, exclus�o e consulta)
  de um aluno.
  Uma vez submetido o aluno a a��o desejada ser� executada.

tela2.php
  Obt�m os par�metros e decide qual a��o ser� executada.
  
tela3.php

