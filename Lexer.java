import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Lexer {

	private Scanner input;
	
	private boolean debug;
	
	public Lexer(Scanner input, boolean debug){
		this.debug = debug;
		this.input = input;
		}

    
