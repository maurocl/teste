Este é um exemplo simples de uma aplicação desenvolvida em PHP
usando banco de dados (MySQL).

A aplicação possui apenas uma tabela (aluno). Veja a descrição da tabela abaixo:

create table aluno (
  ra      char(7) not null,
  nome c  har(30) not null,
  primary key (ra),
  unique (ra)
);

conecta.php

  Este arquivo implementa a função que atualiza a variável $con
  com uma conexão (se for possível).
  Para criar uma conexão usando MySQL são necessárias as seguintes
  informações:
  
  $servidor= "localhost"; // nome do servidor no banco de dados
  $usuario = "root"; // nome do usuário
  $senha   = ""; // senha do usuário
  $banco   = "test"; // nome  do database onde estão as tabelas do banco

tela1.html --> tela2.php --> tela3.php

tela1.html
  Tela inicial da aplição. É um formlário onde é selecionada a
  ação desejada (inclusão, alteração, exclusão e consulta)
  de um aluno.
  Uma vez submetido o aluno a ação desejada será executada.

tela2.php
  Obtém os parâmetros e decide qual ação será executada.
  
tela3.php

