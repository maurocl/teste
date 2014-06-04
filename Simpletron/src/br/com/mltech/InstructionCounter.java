package br.com.mltech;

/**
 * InstructionRegister
 * 
 * @author maurocl
 *
 */
public class InstructionCounter {
  
  /**
   * Código da operação
   */
  private int operationCode;
  
  /**
   * Operando
   */
  private int operand;
  
  /**
   * 
   * @return
   */
  public int getOperationCode() {
  
    return operationCode;
  }
  
  /**
   * 
   * @param operationCode
   */
  public void setOperationCode(int operationCode) {
  
    this.operationCode = operationCode;
  }
  
  /**
   * 
   * @return
   */
  public int getOperand() {
  
    return operand;
  }
  
  /**
   * 
   * @param operand
   */
  public void setOperand(int operand) {
  
    this.operand = operand;
  }
  
}
