package shank_interpreter;

public class Token {
	
	/**
	 * An enumeration representing the types of tokens.
	 */
	public enum tokenType { 
		//keywords
		IDENTIFIER, NUMBER, ENDOFLINE,
		WHILE, FOR, FROM, TO, OF,
		CONSTANTS, VARIABLES, INTEGER, REAL, BOOLEAN, CHAR, STRING, ARRAY, VAR,
		DEFINE, IF, THEN, ELSIF, ELSE, REPEAT, UNTIL,
		WRITE, TRUE, FALSE,
		
		//punctuation & operators
		PLUS, MINUS, MULTIPLY, DIVIDE, MOD,
		NOT, AND, OR,
		EQUALS, NOTEQUAL, GREATER, LESS, GREATEROREQUAL, LESSOREQUAL,
		ASSIGNMENT, COLON, SEMICOLON, COMMA,
		LPAREN, RPAREN, LCBRACK, RCBRACK, LBRACK, RBRACK,
		
		//literals
		STRINGLITERAL, CHARACTERLITERAL,
		
		//other
		INDENT, DEDENT
	}
	
	/**
	 * Token type, value, and lineNumber fields.
	 */
	private tokenType type;
	private String value;
	private int lineNumber;
	
	/**
	 * Constructor to set token type and value.
	 * 
	 * @param t
	 * 				the token type
	 * @param value
	 * 				the value of the token
	 */
	public Token(tokenType t, String value, int lineNumber) {
		
		this.type = t;
		this.value = value;
		this.lineNumber = lineNumber;
		
	}
	
	/**
	 * Returns the token type of the token.
	 * 
	 * @return The token type
	 */
	public tokenType getTokenType() {
		return type;
	}
	
	/**
	 * Returns the value of the token.
	 * 
	 * @return The value of the token
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Override toString method.
	 */
	@Override
	public String toString() {
		if(value == null)
			return type + "\n";
		else if(value == "")
			return type + " ";
		else
			return type + "(" + value + ") ";
	}

}
