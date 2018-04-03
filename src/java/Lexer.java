 /**
   * file: Lexer.java
   * author: Artur Barbosa
   * course: CMPT 435
   * assignment: project 2 Parcer 
   * due date: 4/3/2018
   * Version 3
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
	
    // input program to lex as a whole string
    private String programInput = "";
    
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
	 * Get all tokens from the input source program
	 * @return a List<Token> of all tokens
	 */
	public List<Token> tokens(){
		List<Token> allTokens = new ArrayList<Token>();
		boolean inQuote = false;    // indicate input is inside a quote
		boolean inComments = false; // indicate input is inside a comment
		boolean strError = false;   // indicate there is an error in quoted string
		String theString = "";		
		int lineNo = 1;
		int quoteBeginLineno = lineNo;
		List<Token> strTokens = new ArrayList<Token>();
		int i = 0;
		
		// process the input program character by character
		while(i < programInput.length()) {
			
			// get the current character
			char c = programInput.charAt(i);
	
			if(c == '/') {
				if( i < programInput.length()-1) {
					char c2 = programInput.charAt(i+1);
					if(c2 == '*') {
						inComments = true;
						i++;
					}
				}
			}
			else if(inComments) {
				if(c == '*') {
					if( i < programInput.length()-1) {
						char c2 = programInput.charAt(i+1);
						if(c2 == '/') {
							inComments = false;
							i++;
						}
					}
				}
			}
			else if(inQuote) {
				theString += c;
				
				if(c == '"') {					
					inQuote = false;
					if(!strError) {
						strTokens.add(new Token(c+"", Token.Type.QUOTE, lineNo));
						allTokens.addAll(strTokens);
					} else {
						allTokens.add(new Token(theString+"", Token.Type.ERROR, quoteBeginLineno));
						break;
					}					
					strTokens.clear();
					theString = "";
				} else {
					if(c == ' ' || Character.isLowerCase(c)) {
						strTokens.add(new Token(c+"", Token.Type.CHAR, lineNo));
					} else {
						strError = true;						
					}
				}
			}			
			else if(c == '"') {
				theString += c;
				inQuote = true;
				quoteBeginLineno = lineNo;
				strTokens.add(new Token(c+"", Token.Type.QUOTE, lineNo));
			}
			else if(c == '(') allTokens.add(new Token(c+"", Token.Type.LPAREN, lineNo));
			else if(c == ')') allTokens.add(new Token(c+"", Token.Type.RPAREN, lineNo));
			else if(c == '{') allTokens.add(new Token(c+"", Token.Type.LBRACE, lineNo)) ;
			else if(c == '}') allTokens.add(new Token(c+"", Token.Type.RBRACE, lineNo));
			else if(c == '$') allTokens.add(new Token(c+"", Token.Type.EOP, lineNo));
			else if(c == '+') allTokens.add(new Token(c+"", Token.Type.PLUS, lineNo));					
			else if(c == '='){
				if(i < programInput.length()-1){
					char c2 = programInput.charAt(i+1);
					if(c2 == '='){
						allTokens.add(new Token("==", Token.Type.BOOLOP_EQUAL, lineNo));
						i++;
					}
					else{
						allTokens.add(new Token(c+"", Token.Type.ASSIGN, lineNo));
					}
				}
				else{
					allTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
					break;
				}
			}
			else if(c == '!'){
				if(i < programInput.length()-1){
					char c2 = programInput.charAt(i+1);
					if(c2 == '='){
						allTokens.add(new Token("!=", Token.Type.BOOLOP_NOT_EQUAL, lineNo));
						i++;
					}
					else{
						allTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
					}
				}
				else{
					allTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
				}
			}
			else if(programInput.startsWith("if", i)){
				allTokens.add(new Token("if", Token.Type.IF, lineNo));
				i += 2; continue;
			}
			else if(programInput.startsWith("while", i)){
				allTokens.add(new Token("while", Token.Type.WHILE, lineNo));
				i += 5; continue;
			}
			else if(programInput.startsWith("print", i)){
				allTokens.add(new Token("print", Token.Type.PRINT, lineNo));
				i += 5; continue;
			}
			else if(programInput.startsWith("int", i)){
				allTokens.add(new Token("int", Token.Type.TYPE, lineNo));
				i += 3;  continue;
			}
			else if(programInput.startsWith("string", i)){
				allTokens.add(new Token("string", Token.Type.TYPE, lineNo));
				i += 6; continue;
			}
			else if(programInput.startsWith("boolean", i)){
				allTokens.add(new Token("boolean", Token.Type.TYPE, lineNo));
				i += 7; continue;
			}
			else if(programInput.startsWith("false", i)){
				allTokens.add(new Token("false", Token.Type.BOOL_FALSE, lineNo));
				i += 5; continue;
			}
			else if(programInput.startsWith("true", i)){
				allTokens.add(new Token("true", Token.Type.BOOL_TRUE, lineNo));
				i += 4; continue;
			}
			else if(Character.isDigit(c)) allTokens.add(new Token(c+"", Token.Type.DIGIT, lineNo));
			else if(Character.isLowerCase(c)) allTokens.add(new Token(c+"", Token.Type.ID, lineNo));
			else if(!Character.isSpace(c)) {
				allTokens.add(new Token(c+"", Token.Type.ERROR, lineNo));
				break;
			}
			
			if(c == '\n')
				lineNo++;
			i++;
		}
		
		
		return allTokens;
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
