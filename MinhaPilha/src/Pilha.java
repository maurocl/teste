
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa��o de uma Pilha<E>
 * 
 * @author maurocl
 * 
 * @param <T>
 *            Tipo de elementos que a pilha contem
 * 
 */
public class Pilha<T> {

	private static final String TAG = "Pilha";

	private static final boolean DEBUG = true;

	private List<T> lista = null;

	/**
	 * Cria uma nova pilha.
	 * 
	 * @param size
	 */
	public Pilha() {
		lista = new ArrayList<T>();
	}

	/**
	 * Empilha um elemento do tipo T
	 * 
	 * @param t
	 *            Elemento do tipo T
	 * 
	 */
	public void push(T t) {
		lista.add(0,t);

		if (DEBUG)
			Log.i(TAG, "Pilha: " + this);

	}

	/**
	 * Retira e retorna o elemento do topo da pilha.
	 * 
	 * @return Elemento do topo da lista
	 * 
	 */
	public T pop() throws Exception {

		T t = null;

		if (DEBUG)
			Log.i(TAG, "Pilha: " + this);

		if (lista != null) {
			t = lista.get(0);
			lista.remove(0);
		} else {
			throw new Exception("Pilha est� vazia");
		}

		return t;
	}

	/**
	 * Retorna o n� de elementos da pilha
	 * 
	 * @return o tamanho da pilha
	 */
	public int size() {

		if (DEBUG)
			Log.i(TAG, "Pilha: " + this);
		return lista.size();

	}

	/**
	 * Retorna o elemento do topo da pilha. O elemento n�o ser� removido.
	 * 
	 * @return
	 */
	public T top() {
		T t = null;
		if (lista != null) {
			t = lista.get(0);
		}

		if (DEBUG)
			Log.i(TAG, "Pilha: " + this);

		return t;
	}

	@Override
	public String toString() {
		return "Pilha [lista=" + lista + "]";
	}

	public static void main(String[] s) throws Exception {
		
		Pilha<Double> pilha = new Pilha<Double>();
		pilha.push(9.0);
		pilha.push(6.0);

		double x = pilha.pop();
		double y = pilha.pop();

		
		System.out.println(y / x);
		
		pilha.push(y/x);
		

	}

}
