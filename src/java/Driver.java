/**
   * file: Driver.java
   * author: Artur Barbosa
   * course: CMPT 432
   * assignment: project 3 Parser 
   * due date: 4/3/2018
   * Version:3
   * 
   * This file contains the Driver for project 3.
   *
   */
import java.io.File;
import java.util.ArrayList;
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
				System.out.println("\nProgram " + progNo + " Lexical Analysis");
				nextProg = false;
			}
			//if(debug) System.out.println("LEXER --> | " + tok.toString() + "...");
			if(tok.getType() == Token.Type.ERROR) numErrors++;
			lastTok = tok;
			nextInputTokens.add(tok);
			if(tok.getType() == Token.Type.EOP){
				if(numErrors == 0){
					if(debug)System.out.println("Program " + progNo + " Lexical Analysis produced " + numErrors + " error(s) and " + numWarnings + " warning(s)");
					Parser parser = new Parser(nextInputTokens, false);
					System.out.println("\nProgram " + progNo + " Parsing");
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
