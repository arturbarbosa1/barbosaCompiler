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

	static final boolean debug = true;
	
	public static void main(String[] args) throws Exception {
		Scanner input;
		
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
			if(debug)
				System.out.println("LEXER --> | " + tok.toString() + "...");
			if(tok.getType() == Token.Type.ERROR) numErrors++;
			lastTok = tok;
			nextInputTokens.add(tok);
			if(tok.getType() == Token.Type.EOP){
				if(numErrors == 0){
					if(debug)System.out.println("Program " + progNo + " Lexical Analysis produced " + numErrors + " error(s) and " + numWarnings + " warning(s)");
					Parser parser = new Parser(nextInputTokens, debug);
					System.out.println("\nProgram " + progNo + " Parsing");
					AST ast = parser.parse();
					if(parser.isParseOk()){
						System.out.println("Program " + progNo + " Parsing produced " + numErrors + " error(s) and " + numWarnings + " warning(s)");
						
						System.out.println("\nProgram " + progNo + " Semantic Analysis");
						SemanticAnalyser semanticAnalyser = new SemanticAnalyser(ast);
						boolean semanticOk = semanticAnalyser.validate();
						for(Entry e : semanticAnalyser.getEntries()) {
							if(!e.isInitialised()) {
								System.out.println("Warning: Variable [ "  + e.getName() + " ] on line " + e.getType().getLineNo() + " has been declared but is not initialized...");
							}
							else if(!e.isUsed()) {
								System.out.println("Warning: Variable [ "  + e.getName() + " ] on line " + e.getType().getLineNo() + " has been initialized but is not used...");
							}
						}
						System.out.println("Program " + progNo + " Semantic Analysis produced " + semanticAnalyser.getNumErrors() + " error(s) and " + semanticAnalyser.getNumWarnings() + " warning(s)");
						
						System.out.println("\nProgram " + progNo+" Concerete Syntax Tree");
						System.out.println("-------------------------------------------");
						System.out.println(parser.getCST());
						
						System.out.println("\nProgram " + progNo+" Abstract Syntax Tree");
						System.out.println("-------------------------------------------");
						ast.print(0);
						
						System.out.println("\nProgram " + progNo + " Symbol Table");
						if(semanticOk) {
							List<Entry> entries = semanticAnalyser.getEntries();
							System.out.println("--------------------------");
							System.out.println("Name Type      Scope  Line");
							System.out.println("--------------------------");
							for(Entry e : entries) {
								System.out.println(e.name+"    " + e.getType().getLexeme()+"\t" + e.getScope() + "\t" + e.getType().getLineNo());
							}
						} else {
							System.out.println("not produced due to error(s) produced by semantic analysis");
						}
						
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
