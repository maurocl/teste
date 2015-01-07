create table aluno (
  ra char(7) not null,
  nome char(30) not null,
  primary key (ra),
  unique (ra)
);