 /**
   * file: Parser.java
   * author: Artur Barbosa
   * course: CMPT 435
   * assignment: project 2 parser 
   * due date: 3/6/2018
   * 
   * 
   * This file contains the parcer for project 2.
   *
   */
import java.util.List;
import java.util.Stack;


/**
 * This class implements the parser of the grammar that takes a list of Token
 * objects generated by the Lexer, from input source program, and then validates the program
 * as per grammar.
 *
 */
public class Parser {

	private List<Token> tokens;
	private boolean debug;
	private static int progNum = 1;
	
	private String cst = "";
	private boolean parseError = false;
	private boolean warning = false;
	private int nextTokenIndex;
	Stack<Integer> cstIdentValues = new Stack<Integer>();
        /**
	 * Class constructor that creates a new Parser for a given set of Token objects
	 * 
	 * @param tokens a List of Token objects 
	 * @param debug boolean flag whether to print debug message to console
	 */	
	public Parser(List<Token> tokens, boolean debug){
		this.tokens = tokens;
		this.debug = debug;
		cst = "";
		parseError = false;
		nextTokenIndex = 0;
	}
	
	private Token getNextToken(){
		if(nextTokenIndex >= tokens.size()) return null;
		return tokens.get(nextTokenIndex++);
	}
	
	private Token peekNextToken(){
		if(nextTokenIndex >= tokens.size()) return null;
		return tokens.get(nextTokenIndex);
	}
	
	private void appendCSTHeader(String header, int indentation) {		
		for(int i=0; i < indentation; i++) cst += "-";
		cst += header + "\n";
	}
	
	public void parse(){
		if(debug){
			System.out.println("PARSER: Parsing program " + progNum + "...");
			System.out.println("PARSER: parse()");
		}		
		parseProgram();
		progNum++;
	}
	
	private void parseProgram(){
		if(debug) System.out.println("PARSER: parseProgram()");
		cst += "<Program>\n";
		cstIdentValues.push(1);
		parseBlock();
		if(parseError) return;
		Token t = getNextToken();
		if(t == null){
			warning = true;
			return;
		}
		if(t.getType() != Token.Type.EOP){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [EOP] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		cst += "-[$]";
	}

	private void parseBlock(){
		if(debug) System.out.println("PARSER: parseBlock()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Block>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.LBRACE){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_OPENING_BRACE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("[{]", indentation+1);
		
		cstIdentValues.push(indentation+1);
		parseStatementList();
		if(parseError) return;
		t = getNextToken();
		if(t.getType() != Token.Type.RBRACE){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_CLOSING_BRACE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("[}]", indentation+1);
		cstIdentValues.pop();
	}
	
