 <?php

class clsDatabase {

	// propriedades
	public $mysqli;

	/**
	 *
	 * 
	 * @var string
	 */
	private $host;

	/**
	 *
	 * 
	 * @var string
	 */
	private $user;

	/**
	 *
	 * 
	 * @var string
	 */
	private $pass;

	/**
	 *
	 * 
	 * @var string
	 */
	private $db;

	/**
	 *
	 * 
	 * @var string
	 */
	private $table;

	// métodos

	/**
	 * __construct
	 *
	 * 
	 * @param $host
	 * @param $user
	 * @param $pass
	 * @param $db
	 */
	function __construct($host, $user, $pass, $db) {
		echo "<p>Executando o construtor ...\n";
		$this->host=$host;
		$this->user=$user;
		$this->pass=$pass;
		$this->db=$db;
	}

	/**
	 * __destruct
	 *
	 * Enter description here ...
	 */
	function __destruct() {
		echo "<p>Executando o destrutor ...\n";
	}

	/**
	 * insert
	 *
	 * 
	 */
	public function insert() {
		$sql = "insert into $this->table values ()";
		if(!$mysqli->query($sql)) {
			die ("Error: " . $mysqli->error . " (query was $sql)");
		}
	}

	/**
	 * update
	 *
	 * 
	 */
	public function update($key) {
		$sql = "update $this->table where id=$key";
		if(!$mysqli->query($sql)) {
			die ("Error: " . $mysqli->error . " (query was $sql)");
		}
	}

	/**
	 * delete
	 *
	 * 
	 */
	public function delete($key) {
		$sql = "delete from $this->table where id=$key";
		if(!$mysqli->query($sql)) {
			die ("Error: " . $mysqli->error . " (query was $sql)");
		}
	}

	/**
	 * query
	 *
	 * 
	 */
	public function query($key) {
		$sql = "select * from $this->table where id=$key";
		if ($result=$mysqli->query($sql)) {
			if ($result->num_rows >0) {
				while($row = $result->fetch_row()) {

				}
			}

		}
	}

	/**
	 * getAll
	 *
	 * Enter description here ...
	 */
	public function getAll() {

		$sql = "select * from $this->table";

		echo "<p>Executando a query: [$sql]\n";

		if ($result=$this->mysqli->query($sql)) {
				
			if ($result->num_rows >0) {

				echo "<br>Retornando $result->num_rows linha(s)\n";

				while($row = $result->fetch_row()) {
						
					//echo "Numero de colunas retornadas: " . count($row) . ".\n";
					//echo "$row[0], $row[1], $row[2]\n";
						
					for($i=0; $i < count($row)-1; $i++) {
						echo "$row[$i], ";
					}
					echo $row[count($row)-1]."\n";

				}
			}

		}
	}

	/**
	 * connect
	 *
	 * Enter description here ...
	 */
	public function connect() {
		echo "<p>Conectando ao servidor: [$this->host] usando o usuario: [$this->user] e password: [$this->pass] e ao banco de dados: [$this->db]\n";
		$this->mysqli = new mysqli($this->host, $this->user, $this->pass, $this->db);
		if(mysqli_connect_errno()) {
			die("Error: Cannot connect. " . mysqli_connect_errno() . "\n");
		}
	}

	/**
	 * disconnect
	 *
	 * Enter description here ...
	 */
	public function disconnect() {
		$this->mysqli->close();
	}

	/**
	 * setTable
	 *
	 * Enter description here ...
	 * @param string $table
	 *
	 */
	public function setTable($table) {
		$this->table=$table;
	}

}

// criação de um objecto
$test = new clsDatabase('localhost', 'root', '', 'mydb');

//$test->setTable("users");
$test->setTable("categoria");

$test->connect();

$test->getAll();

$test->disconnect();

unset($test);


?>