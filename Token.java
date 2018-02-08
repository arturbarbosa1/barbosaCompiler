
public class Token {
	
	// constants for all token types
	public static enum Type { LBRACE, RBRACE, LPAREN, RPAREN, CHAR, BOOLOP_EQUAL, BOOLOP_NOT_EQUAL,
		                      ASSIGN, PRINT, WHILE, IF, QUOTE, TYPE, PLUS, DIGIT, ID, BOOL_TRUE, BOOL_FALSE,
		                      EOP, ERROR, EOF};
	
	// the lexeme of the token
	private String lexeme;
	
	// type of the token
	private Type type;
	
	// line number of this token in the input
	private int lineNo;
	
	/**
	 * Class constructor that creates a new Token object.
	 * 
	 * @param lexeme the string if the token
	 * @param type type of the token
	 */
	public Token(String lexeme, Type type, int lineNo){
		this.lexeme = lexeme;
		this.type = type;
		this.lineNo = lineNo;
	}

	/**
	 * Get the lexeme
	 * @return the lexeme
	 */
	public String getLexeme() {
		return lexeme;
	}

	/**
	 * Get the type
	 * @return the type
	 */
	public Type getType() {
		return type;
	}	
	
	/**
	 * Get a string representation of the token object
	 * @return A string with details of the token
	 */
	public String toString(){
		String s = "";
		switch(type){
		case RBRACE:
			s += "T_CLOSING_BRACE";
			break;
		case LBRACE:
			s += "T_OPENING_BRACE";
			break;
		case RPAREN:
			s += "T_CLOSING_PARENTHESIS";
			break;
		case LPAREN:
			s += "T_OPENING_PARENTHESIS";
			break;
		case WHILE:
			s += "T_WHILE";
			break;
		case IF:
			s += "T_IF";
			break;
		case PRINT:
			s += "T_PRINT";
			break;
		case PLUS:
			s += "T_INTOP_PLUS";
			break;
		case EOP:
			s += "T_EOPS";
			break;
		case DIGIT:
			s += "T_DIGIT";
			break;
		case QUOTE:
			s += "T_QUOTE";
			break;
		case TYPE:
			s += "T_VARIABLE_TYPE";
			break;
		case CHAR:
			s += "T_CHAR";
			break;
		case ID:
			s += "T_VARIABLE";
			break;
		case ASSIGN:
			s += "T_ASSIGNMENT";
			break;
		case ERROR:
			s += "ERROR! Unrecognized or Invalid Token";
			break;
		}
		
		s += " [ " + lexeme + " ]  on line " + lineNo;
		return s;
	}
}
