package br.com.mltech.utils;


public interface Transaction {

	public void execute() throws Exception;
	
	public void updateView();
	
}
