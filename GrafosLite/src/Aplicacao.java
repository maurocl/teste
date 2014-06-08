import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Aplicacao {

	static Map<Integer, List<Integer>> hash = new HashMap<Integer, List<Integer>>();

	static int[] lbl = new int[100];
	static int[] parent = new int[100];

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		// InputStream fi = new FileInputStream(new
		// File("//Users//maurocl//workspace//GrafosLite//src//input2.txt"));

		InputStream fi = new FileInputStream(new File(
				"//home//maurocl//workspace//GrafosLite//src//input2.txt"));

		// Scanner sc = new Scanner(System.in);
		Scanner sc = new Scanner(fi);

		processo2(sc);

		/*
		System.out.println(getNumVertices());

		for (Integer i : getVertices()) {
			System.out.println(i);
		}

		System.out.println(existeAresta(4, 5));
		System.out.println(existeAresta(0, 1));
		System.out.println(existeAresta(0, 2));
		System.out.println(existeAresta(0, 5));

		System.out.println(hash);
        */
		
		int existe = digraphPath(0, 5);
		
		System.out.println("getNumVertices="+getNumVertices());

		System.out.printf("existe=%d\n",existe);
		
		showLbl();

		System.out.println();
		
		showParent();
		
		imprimeCaminhoAoContrario(0,5);
		
		imprimeCaminho(0,5);
		
		System.out.println("fim");

	}

	/**
	 * Obtém o conjunto de vértices
	 * 
	 * @return Retorna o conjunto de vértices
	 */
	private static Set<Integer> getVertices() {
		return hash.keySet();
	}

	/**
	 * Retorna o nº de vértices do grafo
	 * 
	 * @return
	 */
	private static int getNumVertices() {
		//return hash.keySet().size();
		return 6;
	}

	/**
	 * Verifica se existe uma aresta direcionada entre os vértices v e w
	 * 
	 * @param v Vértice origem
	 * @param w Vértice destino
	 * 
	 * @return true se existir ou false
	 */
	private static boolean existeAresta(int v, int w) {
		if (hash.containsKey(v)) {
			return hash.get(v).contains(w);
		}
		return false;
	}

	/**
	 * 
	 * @param sc
	 */
	private static void processo2(Scanner sc) {

		int numVertices = 0;

		if (sc.hasNext()) {
			numVertices = sc.nextInt();
			sc.nextLine();
		}

		System.out.println(numVertices);

		while (sc.hasNext()) {

			int v1 = sc.nextInt();
			int v2 = sc.nextInt();

			if (hash.containsKey(v1)) {
				hash.get(v1).add(v2);
			} else {
				hash.put(v1, new ArrayList<Integer>());
				hash.get(v1).add(v2);
			}

			sc.nextLine();
		}

		
	}

	/**
	 * exibe o vetor lbl[]
	 */
	static void showLbl() {
		for (int i = 0; i < getNumVertices(); i++) {
			System.out.printf("i=%d, lbl[%d]=%d\n", i, i, lbl[i]);
		}
	}

	/**
	 * Exibe o vetor parent[]
	 */
	static void showParent() {
		for (int i = 0; i < getNumVertices(); i++) {
			System.out.printf("i=%d, parent[%d]=%d\n", i, i, parent[i]);
		}
	}

	/**
	 * Verifica se existe um caminho de s a t em G
	 * 
	 * @param s source (origem)
	 * @param t target (destino)
	 * 
	 * @return Devolve 1 se existe caminho e 0 caso ontrário
	 */
	static int digraphPath(int s, int t) {

		// marca todos os vértices como não visitados
		for (int v = 0; v < getNumVertices(); v++) {
			lbl[v] = 0;
		}

		// busca a existencia do caminho a partir do nó origem
		pathR(s);

		if (lbl[t] == 0) {
			return 0;
		} else {
			return 1;

		}

	}

	/**
	 * Busca um caminho a partir do vértice v
	 * 
	 * @param v Vértice
	 * 
	 */
	static void pathR(int v) {
		
		int w;
		
		// marca o nó como visitado
		lbl[v] = 1;
		
		System.out.println("v=" + v);
		
		// percorre todos os vértices e 
		for (w = 0; w < getNumVertices(); w++) {
			// verifica se existe uma aresta entre os vértices v e w e se o vértice w já foi visitado
			if (existeAresta(v, w) == true && lbl[w] == 0) {
				// marca o pai do vértice w
				parent[w] = v;
				// busca o caminho a partir do vértice w
				pathR(w);
			}
		}
	}

	static void imprimeCaminhoAoContrario(int s, int t) {
		int w;
		for(w=t; w!=s; w=parent[w]) {
			System.out.printf("%d-",w);
		}
		System.out.printf("%d\n",s);
	}
	
	static void imprimeCaminho(int s, int t) {
		
		int w;
		int[] pilha = new int[100];
		
		int topo=0;
		
		for(w=t;w!=s;w=parent[w]) {
			pilha[topo++]=w;
		}
		
		System.out.printf("%d",s);
		
		while(topo>0) {
			System.out.printf("-%d",pilha[--topo]);
		}
		
		System.out.println();
		
	}
	
}
