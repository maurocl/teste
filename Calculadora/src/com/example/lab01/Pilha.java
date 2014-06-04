package com.example.lab01;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Implementacao de uma Pilha<E>
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

		if (DEBUG) {
			Log.i(TAG, "Pilha: " + this);
		}

	}

	/**
	 * Retira e retorna o elemento do topo da pilha.
	 * 
	 * @return Elemento do topo da lista
	 * 
	 */
	public T pop() throws Exception {

		T t = null;

		if (DEBUG) {
			Log.i(TAG, "Pilha: " + this);
		}

		if (lista != null) {
			t = lista.get(0);
			lista.remove(0);
		} else {
			throw new Exception("Pilha esta vazia");
		}

		return t;
	}

	/**
	 * Retorna o numero de elementos da pilha
	 * 
	 * @return o tamanho da pilha
	 */
	public int size() {

		if (DEBUG) {
			Log.i(TAG, "Pilha: " + this);
		}
		
		return lista.size();

	}

	/**
	 * Retorna o elemento do topo da pilha. O elemento nao sera removido.
	 * 
	 * @return
	 */
	public T top() {
		
		T t = null;
		
		if (lista != null) {
			t = lista.get(0);
		}

		if (DEBUG) {
			Log.i(TAG, "Pilha: " + this);
		}

		return t;
	}

	@Override
	public String toString() {
		return "Pilha [lista=" + lista + "]";
	}

}
