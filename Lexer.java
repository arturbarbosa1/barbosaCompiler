 /**
   * file: Lexer.java
   * author: Artur Barbosa
   * course: CMPT 435
   * assignment: project 1 Lexer 
   * due date: 2/6/2018
   * 
   * 
   * This file contains the lexer for project 1.
   *
   */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class implements the lexer that reads input program from standard input
 * and generates the list of Token objects.
 */
public class Lexer {
	
	// input stream to read tokens from
    private Scanner input;
	
	// flag to whether print debug message by lexer
	private boolean debug;
	
	/**
	 * Class constructor that creates a new Lexer object.
	 * 
	 * @param debug flag to whether print debug message
	 */
	public Lexer(Scanner input, boolean debug){
		this.debug = debug;
		//this.input = input;
		while(input.hasNextLine()) {
			if(programInput.length() == 0)
				programInput += input.nextLine();
			else
				programInput += "\n" + input.nextLine();
		}
	}
	
	
	/**
	 * Read the input and return all the tokens in a list.
	 * @return a List<Token> of all tokens
	 */
	public List<Token> readTokens(){
		List<Token> tokens = new ArrayList<Token>();
		String line;
		int currentLine = 1;
		
		while(input.hasNextLine()){
			line = input.nextLine();
			List<Token> nextTokens = getTokensFromString(line, currentLine);
			tokens.addAll(nextTokens);
			currentLine++;
		}		
		//tokens.add(new Token("", Token.Type.EOF, currentLine));
		return tokens;
	}
	
	/**
	 * Read all tokens in a string from input
	 * 
	 * @return a List<Token> of all tokens read
	 */
	private List<Token> getTokensFromString(String s, int lineNo){
		List<Token> nextTokens = new ArrayList<Token>();
		Token tok = null;
		int i = 0;
		boolean inQuote = false;
		while(i < s.length()){
			char c = s.charAt(i);
			if(!inQuote && (c == ' ' || c == '\t') ){
				i++;
				continue;
			}
			if(c == '"'){ 
				nextTokens.add(new Token(c+"", Token.Type.QUOTE, lineNo));
				inQuote = !inQuote;
			}
			else if(c == '(') nextTokens.add(new Token(c+"", Token.Type.LPAREN, lineNo));
			else if(c == ')') nextTokens.add(new Token(c+"", Token.Type.RPAREN, lineNo));
			else if(c == '{') nextTokens.add(new Token(c+"", Token.Type.LBRACE, lineNo)) ;
			else if(c == '}') nextTokens.add(new Token(c+"", Token.Type.RBRACE, lineNo));
			else if(c == '$') nextTokens.add(new Token(c+"", Token.Type.EOP, lineNo));
			else if(c == '+') nextTokens.add(new Token(c+"", Token.Type.PLUS, lineNo));
			else if(c == ' ') nextTokens.add(new Token(c+"", Token.Type.CHAR, lineNo));
			else if(Character.isDigit(c)) nextTokens.add(new Token(c+"", Token.Type.DIGIT, lineNo));
			else if(c == '='){
				if(i < s.length()-1){
					char c2 = s.charAt(i+1);
					if(c2 == '='){
						nextTokens.add(new Token("==", Token.Type.BOOLOP_EQUAL, lineNo));
						i++;
					}
					else{
						nextTokens.add(new Token(c+"", Token.Type.ASSIGN, lineNo));
					}
				}
				else{
					nextTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
				}
			}
			else if(c == '!'){
				if(i < s.length()-1){
					char c2 = s.charAt(i+1);
					if(c2 == '='){
						nextTokens.add(new Token("!=", Token.Type.BOOLOP_NOT_EQUAL, lineNo));
						i++;
					}
					else{
						nextTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
					}
				}
				else{
					nextTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
				}
			}
			else if(s.startsWith("print", i)){
				nextTokens.add(new Token("print", Token.Type.PRINT, lineNo));
				i += 5; continue;
			}
			else if(s.startsWith("int", i)){
				nextTokens.add(new Token("int", Token.Type.TYPE, lineNo));
				i += 3;  continue;
			}
			else if(s.startsWith("string", i)){
				nextTokens.add(new Token("string", Token.Type.TYPE, lineNo));
				i += 6; continue;
			}
			else if(s.startsWith("boolean", i)){
				nextTokens.add(new Token("boolean", Token.Type.TYPE, lineNo));
				i += 7; continue;
			}
			else if(s.startsWith("false", i)){
				nextTokens.add(new Token("false", Token.Type.BOOL_FALSE, lineNo));
				i += 5; continue;
			}
			else if(s.startsWith("true", i)){
				nextTokens.add(new Token("true", Token.Type.BOOL_TRUE, lineNo));
				i += 4; continue;
			}
			else if(Character.isLowerCase(c)){
				if(inQuote){
					nextTokens.add(new Token(c+"", Token.Type.CHAR, lineNo));
				}
				else{
					nextTokens.add(new Token(c+"", Token.Type.ID, lineNo));
				}
			}
			else nextTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
			
			i++;
		}
		
		return nextTokens;
	}
	

}
