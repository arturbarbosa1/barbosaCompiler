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
		
		if(args.length > 0){
			input = new Scanner(new File(args[0]));
		}else{
			input = new Scanner(System.in);
		}
		// instantiate a Lexer object to read from standard input with debug
		Lexer lexer = new Lexer(input, true);
		
		System.out.println("Beginning Lexing Session...*Strings Treated As CharList*\n");
		
		// read all tokens from the input 
		List<Token> tokens = lexer.readTokens();
		
		int numWarnings = 0;
		int numErrors = 0;
		Token lastTok = null;
		// display tokens to console as lexer debug message
		for(Token tok : tokens){
			System.out.println("LEXER --> | " + tok.toString() + "...");
			if(tok.getType() == Token.Type.ERROR) numErrors++;
			lastTok = tok;
		}
		if(lastTok.getType() != Token.Type.EOP)numWarnings++;
		
		// display number of warnings and errors
		if(numErrors > 0){
			System.out.printf("\nLex Failed With %d WARNINGS(S) and %d ERROR(S)...\n", numWarnings, numErrors);
		}
		else{
			System.out.printf("\nLex Completed With %d WARNINGS(S) and %d ERROR(S)...\n", numWarnings, numErrors);
		}
	}

}
