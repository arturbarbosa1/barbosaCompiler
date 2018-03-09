/**
   * file: Driver.java
   * author: Artur Barbosa
   * course: CMPT 435
   * assignment: project 2 Parser 
   * due date: 3/6/2018
   * Version:2
   * 
   * This file contains the Driver for project 2.
   *
   */
import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * This is the driver program to test the Lexer.
 *
 */
public class Driver {

	public static void main(String[] args) throws Exception {
		Scanner input;
		boolean debug = true;
		
		if(args.length > 0){
			input = new Scanner(new File(args[0]));
		}else{
			input = new Scanner(System.in);
		}
		// instantiate a Lexer object to read from standard input with debug enabled
		Lexer lexer = new Lexer(input, debug);
		
		if(debug){
			System.out.println("DEBUG: Running in verbose mode");
		}
		
		// read all tokens from the input 
		List<Token> tokens = lexer.tokens();
		
		int numWarnings = 0;
		int numErrors = 0;
		Token lastTok = null;
		List<Token> nextInputTokens = new ArrayList<Token>();
		int progNo = 1;
		boolean nextProg = true;
		
		// display tokens to console as lexer debug message
		for(Token tok : tokens){
			if(debug && nextProg){ 
				System.out.println("\nLEXER: Lexing program " + progNo + "...");
				nextProg = false;
			}
			if(debug) System.out.println("LEXER --> | " + tok.toString() + "...");
			if(tok.getType() == Token.Type.ERROR) numErrors++;
			lastTok = tok;
			nextInputTokens.add(tok);
			if(tok.getType() == Token.Type.EOP){
				if(numErrors == 0){
					if(debug)System.out.println("LEXER: Lex completed successfully\n");
					Parser parser = new Parser(nextInputTokens, debug);
					parser.parse();
					if(parser.isParseOk()){
						System.out.println("PARSER: Parse completed successfully");
						System.out.println("\nCST for program " + progNo+"...");
						System.out.println(parser.getCST());
					}else {
						System.out.println("PARSER: Parse failed");
						System.out.println("\nCST for program " + progNo+": Skipped due to PARSER error(s)");
					}
				}
				else{
					System.out.println("\nPARSER: Skipped due to LEXER error(s)");
					System.out.println("\nCST for program " + progNo+": Skipped due to LEXER error(s)");
				}
				numErrors = 0;
				numWarnings = 0;
				progNo++;
				nextInputTokens = new ArrayList<Token>();
				nextProg = true;
			}
		}
	}

}
