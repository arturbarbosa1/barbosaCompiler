import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Lexer {
	//input stream to read tokens from
	private Scanner input;
	//flag to whether print debug message by lexer 
	private boolean debug;
	
	/*
	 *Class constructor that creates a new Lexer Object
	 *
	 *@param debug flag to whether we should print debug message 
	 */
	public Lexer(Scanner input, boolean debug){
		this.debug = debug;
		this.input = input;
		}
	
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
		
