create table users (
  id int(11),
  username varchar(100),
  password varchar(100),
  primary key (username),
  unique (username)
  );
  
  
create table aluno (
  ra char(7) not null,
  nome char(30) not null,
  primary key (ra),
  unique (ra)
);




create table categoria (
  id int(11),
  descricao varchar(100),
  primary key (id).
  unique(descricao)
);

create table despesa (
  id int(11),
  data date,
descricao varchar(100)
valor number(100,2)
categoria number(11),
  
  primary key (id).
  unique(descricao)
);






CREATE TABLE IF NOT EXISTS `despesa` (
  `id` int(11) NOT NULL ,
  `data` date NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `valor` decimal(12,2) NOT NULL,
  `categoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;





mysql> desc despesa;
+-----------+---------------+------+-----+---------+----------------+
| Field     | Type          | Null | Key | Default | Extra          |
+-----------+---------------+------+-----+---------+----------------+
| id        | int(11)       | NO   | PRI | NULL    | auto_increment |
| data      | date          | NO   |     | NULL    |                |
| descricao | varchar(100)  | NO   |     | NULL    |                |
| valor     | decimal(12,2) | NO   |     | NULL    |                |
| categoria | int(11)       | NO   |     | NULL    |                |
+-----------+---------------+------+-----+---------+----------------+
5 rows in set (0.02 sec)

mysql>

mysql> desc users;
+----------+--------------+------+-----+---------+----------------+
| Field    | Type         | Null | Key | Default | Extra          |
+----------+--------------+------+-----+---------+----------------+
| id       | int(11)      | NO   | PRI | NULL    | auto_increment |
| username | varchar(100) | NO   | UNI |         |                |
| password | varchar(100) | YES  |     | NULL    |                |
+----------+--------------+------+-----+---------+----------------+
3 rows in set (0.01 sec)

mysql>


mysql> desc categoria
    -> ;
+-----------+--------------+------+-----+---------+----------------+
| Field     | Type         | Null | Key | Default | Extra          |
+-----------+--------------+------+-----+---------+----------------+
| id        | int(11)      | NO   | PRI | NULL    | auto_increment |
| descricao | varchar(100) | NO   |     | NULL    |                |
+-----------+--------------+------+-----+---------+----------------+
2 rows in set (0.00 sec)