
package br.com.mltech;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 */
public class Simpletron {

  // Operações de entrada/saída
  private static final int READ = 10;
  private static final int WRITE = 11;

  // Operações de carga/armazenamento
  private static final int LOAD = 20;  
  private static final int STORE = 21;

  // Operações aritméticas
  private static final int ADD = 30;
  private static final int SUBTRACT = 31;
  private static final int DIVIDE = 32;
  private static final int MULTIPLY = 33;

  // Operações de transferência de controle
  private static final int BRANCH = 40;
  private static final int BRANCHNEG = 41;
  private static final int BRANCHZERO = 42;
  private static final int HALT = 43;

  static Instruction[] iset = new Instruction[12];
  
  
  Memory mem = new Memory();

  static Simpletron simpletron;

  // Accumulator Register
  static int accumulator = 0;
  
  // Program Counter
  static int  pc=0;
  
  // Operation
  static int  op=0;
  
  // Operand
  static int  operand=0;

  static Map<Integer,String> hash = new HashMap<Integer,String>();
  
  /**
   * 
   */
  public static void execute() {
    
    hash.put(10, "READ");
    hash.put(11, "WRITE");
    hash.put(20, "LOAD");
    hash.put(21, "STORE");
    hash.put(30, "ADD");
    hash.put(31, "SUBTRACT");
    hash.put(32, "DIVIDE");
    hash.put(33, "MULTIPLY");
    hash.put(40, "BRANCH");
    hash.put(41, "BRANCHNEG");
    hash.put(42, "BRANCHZERO");
    hash.put(43, "HALT");

    System.out.println("Welcome to Simpletron");

    simpletron = new Simpletron();

    accumulator = 0;
    simpletron.mem.clear();

    simpletron.mem.write(0, 1007); // (Read A)
    simpletron.mem.write(1, 1008); // (Read B)
    simpletron.mem.write(2, 2007); // (Load A
    simpletron.mem.write(3, 3008); // (Add B)
    simpletron.mem.write(4, 2109); // (Store C)
    simpletron.mem.write(5, 1109); // (Write C)
    simpletron.mem.write(6, 4300); // (Halt)
    simpletron.mem.write(7, 0000); // (Variable A)
    simpletron.mem.write(8, 0000); // (Variable B)
    simpletron.mem.write(9, 0000); // (Variable C)
    
    for(int i=0; i<10;i++) {
      
      int word = simpletron.mem.read(i);

      // operation code
      op = word / 100;
      operand = word % 100;
      
      System.out.printf("%02d: %04d (%s %d)\n", i, word, hash.get(op), operand);
      
    }
    System.out.println();

    //System.out.println(simpletron.mem);

    pc = 0;

    Scanner sc = new Scanner(System.in);
    
    boolean halt=false;

    //while (pc < simpletron.mem.size) {
    while (!halt) {

      int word = simpletron.mem.read(pc);

      // operation code
      op = word / 100;
      operand = word % 100;

      System.out.printf("pc=%04d %02d%02d (%s %d)\n", pc, op, operand, hash.get(op), operand);

      switch (op) {
        case READ:
          System.out.print("==> ");
          int value = sc.nextInt();
          simpletron.mem.write(operand, value);
          break;
        case WRITE:
          System.out.println(simpletron.mem.read(operand));
          break;
        case LOAD:
          accumulator = simpletron.mem.read(operand);
          break;
        case STORE:
          simpletron.mem.write(operand, accumulator);
          break;
        case ADD:
          accumulator += simpletron.mem.read(operand);
          break;
        case SUBTRACT:
          accumulator -= simpletron.mem.read(operand);
          break;
        case DIVIDE:
          accumulator /= simpletron.mem.read(operand);
          break;
        case MULTIPLY:
          accumulator *= simpletron.mem.read(operand);
          break;
        case BRANCH:
          pc = operand;
          break;
        case BRANCHNEG:
          if (accumulator < 0) {
            pc = operand;
            continue;
          }
          break;
        case BRANCHZERO:
          if (accumulator == 0) {
            pc = operand;
            continue;
          }
          break;
        case HALT:
          /*
          pc = 100;
          System.out.println("HALT");
          continue;
          */
          halt=true;
          continue;

      }

      pc++;

    }

    /*
    System.out.println(simpletron.mem.read(7));
    System.out.println(simpletron.mem.read(8));
    System.out.println(simpletron.mem.read(9));
    */
    
    simpletron.dump();

  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {

    execute();
  }

  public void dump() {

    
    System.out.println("\nREGISTERS:");
    
    System.out.printf("accumulator:   %04d\n", accumulator);
    System.out.printf("PC:            %02d\n", pc);
    System.out.printf("operationCode: %02d\n", op);
    System.out.printf("operand:       %02d\n", operand);
    
    System.out.println();
    
    System.out.println("MEMORY:");
    
    System.out.print("       ");
    for (int i = 0; i < 10; i++) {
      System.out.printf("%4d   ", i);
    }
    System.out.println();
    
    for (int i = 0; i < 10; i++) {
      
      System.out.printf("%4d   ",i*10);
      
      for (int j = 0; j < 10; j++) {
        
        int value = simpletron.mem.read(i*10+j);
        
        System.out.printf("%04d   ",value);
        
      }
      
      System.out.println();

    }

  }

  /**
   * 
   */
  public static void createInstructionSet() {
    
    iset[0].setOp(10);
    iset[0].setName("READ");
    
    iset[1].setOp(11);
    iset[1].setName("WRITE");
    
    iset[2].setOp(20);
    iset[2].setName("LOAD");
    
    iset[3].setOp(21);
    iset[3].setName("STORE");
    
    iset[4].setOp(30);
    iset[4].setName("ADD");
    
    iset[5].setOp(31);
    iset[5].setName("SUBTRACT");
    
    iset[6].setOp(32);
    iset[6].setName("DIVIDE");
    
    iset[7].setOp(33);
    iset[7].setName("MULTIPLY");

    iset[8].setOp(40);
    iset[8].setName("BRANCH");
    
    iset[9].setOp(41);
    iset[9].setName("BRANCHNEG");
    
    iset[10].setOp(42);
    iset[10].setName("BRANCHZERO");
    
    iset[11].setOp(43);
    iset[11].setName("HALT");

  }
  
}
