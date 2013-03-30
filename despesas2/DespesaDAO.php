<?php

require_once 'Categoria.php';
require_once 'Despesa.php';

class DespesaDAO {

	private $con;

	/**
	 * __construct($con)
	 *
	 * Método construtor
	 *
	 * @param unknown_type $con
	 */
	public function __construct($con) {
		$this->con=$con;
	}

	/**
	 * getConnection()
	 *
	 */
	public function getConnection() {
		return $this->con;
	}

	/**
	 * inserir($d)
	 *
	 * Insere uma nova Despesa
	 *
	 * @param Despesa $despesa Nova Despesa
	 */
	public function inserir(Despesa $despesa) {

		$data = $despesa->getData();
		$desc = $despesa->getDescricao();
		$valor = $despesa->getValor();
		$categoria = $despesa->getCategoria();

		$dataYMD = $this->fmtYMD($data);

		$cmd="insert into despesa (data, descricao, valor, categoria) values ('$dataYMD','$desc','$valor','$categoria')";
		 
		//echo "<br>cmd=[ $cmd ]";
		 
		$rc = $this->getConnection()->query($cmd);

		//echo "<br>rc=$rc\n";

		$id = $this->getConnection()->insert_id;

		$despesa->setId($id);

		return $rc;

	}

	/**
	 * alterar($c)
	 *
	 * Altera as informações de uma Despesa
	 *
	 * @param Despesa $despesa
	 *
	 */
	public function alterar(Despesa $despesa) {

		$id        = $despesa->getId();
		$data      = $despesa->getData();
		$descricao = $despesa->getDescricao();
		$valor     = $despesa->getValor();
		$categoria = $despesa->getCategoria();
		 
		$dataYMD = $this->fmtYMD($data);

		$cmd = "update despesa set descricao='$descricao', data='$dataYMD', valor=$valor, categoria='$categoria' where id=$id";

		return $this->getConnection()->query($cmd);

	}

	/**
	 * excluir($despesa)
	 *
	 * Exclui uma Despesa
	 *
	 * @param Despesa $despesa
	 *
	 */
	public function excluir(Despesa $despesa) {

		$id = $despesa->getId();

		$cmd="delete from despesa where id=$id";

		//echo "\$cmd=[ $cmd ]";

		return $this->getConnection()->query($cmd);

	}

	/**
	 * consultar($id)
	 *
	 * Consulta uma despesa dado seu código
	 *
	 * @param int $id Identificador da despesa
	 *
	 * @return Um objeto Despesa ou null caso a Despesa não seja encontrada
	 *
	 */
	public function consultar($id) {

		$cmd = "select * from despesa where id=$id";

		//echo "<br>Consultar - cmd=[$cmd]<br>";

		$result = $this->getConnection()->query($cmd);

		$despesa=null;

		if($result!=null) {
			while($linha=$result->fetch_array()) {

				$despesa = new Despesa($linha[0],$linha[1], $linha[2], $linha[3], $linha[4]);

			}
		}

		// retorna um objeto Despesa ou null
		return $despesa;

	}

	/**
	 * listarTodos()
	 *
	 * Obtém um array de objetos Despesa
	 * ordenados por data.
	 *
	 * @return Uma array de objetos Despesa
	 *
	 */
	public function listarTodos() {

		// cria um array vazio
		$lista = Array();

		$cmd = "select * from despesa order by data";

		$result = $this->getConnection()->query($cmd);

		while($linha=$result->fetch_array()) {

			//echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

			$despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

			// insere o objeto em uma lista
			array_push($lista, $despesa);

			//echo "<p>$despesa";
			//echo "<p>$despesa->getDescricao()";

		}

		// retorna a lista de categorias
		return $lista;

	}

	/**
	 * Obtém uma lista de despesas compreendidas entre
	 * um período ordenadas pela data da despesa
	 *
	 * @param string $data1 Data inicial
	 * @param string $data2 Data final
	 *
	 * @return Uma lista de objetos
	 */
	public function listarTodosByData($data1,$data2) {
		 
		// data1 está no formato dd/mm/aaaa --> aaaammdd
		// data2 está no formato dd/mm/aaaa --> aaaammdd

		$fmtData1 = $this->fmtYMD($data1);
		$fmtData2 = $this->fmtYMD($data2);

		// cria um array (lista) vazio(a).
		$lista = Array();

		//$cmd = "select * from despesa between data1 and data2 order by data ";

		//$cmd="SELECT id, date_format(data,'%d/%m/%Y') dt, descricao, valor, categoria  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2'";

		$cmd="SELECT id, data, descricao, valor, categoria  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2' order by data";

		//echo "<p>cmd=$cmd<br>";

		$result = $this->getConnection()->query($cmd);

		if($result!=null) {

			while($linha=$result->fetch_array()) {

				//echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

				$despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

				// insere o objeto em uma lista
				array_push($lista, $despesa);

				//echo "<p>$despesa";
				//echo "<p>$despesa->getDescricao()";

			}

		}

		// retorna a lista de categorias
		return $lista;

	}

	/**
	 * Obtém uma lista de despesas compreendidas entre
	 * um período ordenadas pela data da despesa
	 *
	 * @param string $data1 Data inicial
	 * @param string $data2 Data final
	 *
	 * @return Uma lista de objetos
	 */
	public function listarTodosByDataDescricao($data1,$data2) {
		 
		// data1 está no formato dd/mm/aaaa --> aaaammdd
		// data2 está no formato dd/mm/aaaa --> aaaammdd

		$fmtData1 = $this->fmtYMD($data1);
		$fmtData2 = $this->fmtYMD($data2);

		// cria um array (lista) vazio(a).
		$lista = Array();

		
		$cmd = "SELECT d.id, d.data, d.descricao, d.valor, d.categoria, c.descricao".
" FROM   despesa d left outer join categoria c on c.id = d.id " .
"where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2' order by data";
		
		//echo "<p>cmd=$cmd<br>";

		$result = $this->getConnection()->query($cmd);

		if($result!=null) {

			while($linha=$result->fetch_array()) {

				//echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

				$despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4], $linha[5]);

				// insere o objeto em uma lista
				array_push($lista, $despesa);

				//echo "<p>$despesa";
				//echo "<p>$despesa->getDescricao()";

			}

		}

		// retorna a lista de categorias
		return $lista;

	}


	/**
	 * Transforma uma data representada por uma string no
	 * formato dd/mm/aaaa para o formato aaaammdd
	 *
	 * @param string $data Data no formato dd/mm/aaaa
	 *
	 * @return string com data no formato aaaammdd
	 */
	private function fmtYMD($data) {

		//echo "<br>data: $data";

		$d = substr($data, 0,2);
		$m = substr($data, 3,2);
		$a = substr($data, 6,4);

		//echo "<br>d=$d, m=$m, a=$a<br>";

		return $a . $m . $d;
	}

	/**
	 * Transforma uma data representada por uma string no
	 * formato aaaa/mm/dd para o formato dd/mm/aaaa
	 *
	 * @param string $data Data no formato aaaa/mm/dd
	 *
	 * @return string com data no formato dd/mm/aaaa
	 */
	public function fmtDMA($data) {

		//echo "<br>data: $data";

		$a = substr($data, 0,4);
		$m = substr($data, 5,2);
		$d = substr($data, 8,2);

		//echo "<br>d=$d, m=$m, a=$a<br>";

		return "$d/$m/$a";

	}
	
	/**
	 * 
	 * Obtem o total por categoria
	 * 
	 * @param unknown_type $data1
	 * @param unknown_type $data2
	 */
	public function listarTotalByCategoria($data1,$data2) {
		 
		// data1 está no formato dd/mm/aaaa --> aaaammdd
		// data2 está no formato dd/mm/aaaa --> aaaammdd

		$fmtData1 = $this->fmtYMD($data1);
		$fmtData2 = $this->fmtYMD($data2);

		// cria um array (lista) vazio(a).
		$lista = Array();

		//$cmd = "select * from despesa between data1 and data2 order by data ";

		//$cmd="SELECT id, date_format(data,'%d/%m/%Y') dt, descricao, valor, categoria  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2'";

		$cmd="SELECT categoria, sum(valor) total  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2' group by categoria  order by total desc";

		//echo "<p>cmd=$cmd<br>";

		$result = $this->getConnection()->query($cmd);

		if($result!=null) {

			while($linha=$result->fetch_array()) {

				//echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

				//$despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

				// insere o objeto em uma lista
				array_push($lista, $linha);			

			}

		}

		// retorna a lista de categorias
		return $lista;

	}
	

		/**
	 * 
	 * Obtem o total por categoria
	 * 
	 * @param unknown_type $data1
	 * @param unknown_type $data2
	 */
	public function listarVlrTotalByCategoria($data1,$data2) {
		 
		// data1 está no formato dd/mm/aaaa --> aaaammdd
		// data2 está no formato dd/mm/aaaa --> aaaammdd

		$fmtData1 = $this->fmtYMD($data1);
		$fmtData2 = $this->fmtYMD($data2);

		$cmd="SELECT sum(valor) total FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2' ";

		//echo "<p>cmd=$cmd<br>";

		$result = $this->getConnection()->query($cmd);

		if($result!=null) {

			while($linha=$result->fetch_array()) {

				//echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

				//$despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

				// insere o objeto em uma lista
				$lista= $linha[0];			

			}

		}

		// retorna a lista de categorias
		return $lista;

	}
	
	
	/**
	 * 
	 * Obtem o total por categoria
	 * 
	 * @param unknown_type $data1
	 * @param unknown_type $data2
	 */
	public function listarByCategoriaBetweenData($idCategoria, $data1,$data2) {
		 
		// data1 está no formato dd/mm/aaaa --> aaaammdd
		// data2 está no formato dd/mm/aaaa --> aaaammdd

		$fmtData1 = $this->fmtYMD($data1);
		$fmtData2 = $this->fmtYMD($data2);

		// cria um array (lista) vazio(a).
		$lista = Array();

		//$cmd = "select * from despesa between data1 and data2 order by data ";

		//$cmd="SELECT id, date_format(data,'%d/%m/%Y') dt, descricao, valor, categoria  FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2'";

		$cmd="SELECT * FROM despesa g where date_format(data,'%Y%m%d') between '$fmtData1' and '$fmtData2' and categoria=$idCategoria order by data";

		//echo "<p>cmd=$cmd<br>";

		$result = $this->getConnection()->query($cmd);

		if($result!=null) {

			while($linha=$result->fetch_array()) {

				//echo "<p>$linha[0], $linha[1], $linha[2], $linha[3], $linha[4], ";

				//$despesa = new Despesa($linha[0], $linha[1], $linha[2], $linha[3], $linha[4]);

				// insere o objeto em uma lista
				array_push($lista, $linha);			

			}

		}

		// retorna a lista de categorias
		return $lista;

	}
	
	
}
?>