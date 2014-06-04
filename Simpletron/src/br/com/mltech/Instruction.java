package br.com.mltech;

/**
 * Representa uma instrução
 * 
 * 
 */
public class Instruction {

	private int op; // operation code
	private String name;

	/**
	 * Cria uma nova instrução
	 * 
	 * @param op
	 *            Operation Code
	 * @param name
	 *            Nome
	 */
	public Instruction(int op, String name) {

		super();
		this.op = op;
		this.name = name;

	}

	/**
	 * 
	 * @return
	 */
	public int getOp() {

		return op;
	}

	/**
	 * 
	 * @param op
	 */
	public void setOp(int op) {

		this.op = op;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {

		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {

		this.name = name;
	}

}
