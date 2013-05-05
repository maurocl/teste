import java.util.ArrayList;
import java.util.List;

public class EvaluatePostfixExpr {

	Pilha<Token> token;
	Pilha<Token> stack;

	List<Token> tokens;

	public EvaluatePostfixExpr() {
		
		super();
		token = new Pilha<Token>();
		stack = new Pilha<Token>();

		tokens = new ArrayList<Token>();	
		
		/*
		tokens.add(new Token("1",1,"operand"));
		tokens.add(new Token("2",2,"operand"));
		tokens.add(new Token("3",3,"operand"));
		tokens.add(new Token("4",4,"operand"));
		tokens.add(new Token("5",5,"operand"));
		tokens.add(new Token("6",6,"operand"));
		tokens.add(new Token("7",7,"operand"));
		tokens.add(new Token("8",8,"operand"));
		tokens.add(new Token("9",9,"operand"));
		tokens.add(new Token("0",0,"operand"));

		tokens.add(new Token("+",1,"operator"));
		tokens.add(new Token("-",1,"operator"));
		tokens.add(new Token("*",1,"operator"));
		tokens.add(new Token("/",1,"operator"));
		*/
		
		
	}

	public EvaluatePostfixExpr(Pilha token, Pilha stack) {
		super();
		this.token = token;
		this.stack = stack;
	}

	public void evaluate(String s) {

	}

	public static void main(String[] s) {

		EvaluatePostfixExpr e = new EvaluatePostfixExpr();

		e.evaluate("3 4 + 5 1 - *");

	}

}