package br.com.mltech;


public class Instruction {
  
  private int op;
  private String name;
  
  public Instruction(int op, String name) {

    super();
    this.op = op;
    this.name = name;
    
  }

  
  public int getOp() {
  
    return op;
  }

  
  public void setOp(int op) {
  
    this.op = op;
  }

  
  public String getName() {
  
    return name;
  }

  
  public void setName(String name) {
  
    this.name = name;
  }
  
  
  
  
}
