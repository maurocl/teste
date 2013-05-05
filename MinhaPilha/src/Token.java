
public class Token {

	String symbol;
	double value;
	String type; // operator; operand
	
	public Token(String symbol, double value, String type) {
		super();
		this.symbol = symbol;
		this.value = value;
		this.type = type;
	}
	
	public boolean isToken() {
		return false;
	}
	
	
	
}
