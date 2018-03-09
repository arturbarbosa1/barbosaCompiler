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
	private void parseStatementList(){
		if(debug) System.out.println("PARSER: parseStatementList()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Statemment List>", indentation);
		Token t = peekNextToken();
		
		while(t.getType() == Token.Type.PRINT || t.getType() == Token.Type.ID
				|| t.getType() == Token.Type.TYPE || t.getType() == Token.Type.WHILE
				|| t.getType() == Token.Type.IF || t.getType() == Token.Type.LBRACE){
			cstIdentValues.push(indentation+1);
			parseStatement();
			if(parseError) return;
			t = peekNextToken();
		}		
		cstIdentValues.pop();
	}
	private void parseStatement(){
		if(debug) System.out.println("PARSER: parseStatement()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Statement>", indentation);
		Token t = peekNextToken();
		cstIdentValues.push(indentation+1);
		
		switch(t.getType()){
		case PRINT:
			parsePrintStatement();
			break;
		case ID:
			parseId();
			break;
		case TYPE:
			parseType();
			break;
		case WHILE:
			parseWhileStatement();
			break;
		case IF:
			parseIfStatement();
			break;
		case LBRACE:
			parseBlock();
			break;
		}
		cstIdentValues.pop();
	}
	
	private void parsePrintStatement(){
		if(debug) System.out.println("PARSER: parsePrintStatement()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<PrintStatement>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.PRINT){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_PRINT] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		t = getNextToken();
		if(t.getType() != Token.Type.LPAREN){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_OPENING_PARENTHESIS] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.push(indentation+1);
		parseExpr();
		if(parseError) return;
		t = getNextToken();
		if(t.getType() != Token.Type.RPAREN){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_CLOSING_PARENTHESIS] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.pop();
	}
	
	private void parseAssignmentStatement(){
		if(debug) System.out.println("PARSER: parseAssignmentStatement()");
		
	}
	
	private void parseVarDecl(){
		if(debug) System.out.println("PARSER: parseVarDecl()");
	}
	
	private void parseWhileStatement(){
		if(debug) System.out.println("PARSER: parseWhileStatement()");
	}
	
	private void parseIfStatement(){
		if(debug) System.out.println("PARSER: parseIfStatement()");
	}
	
	private void parseExpr(){
		if(debug) System.out.println("PARSER: parseExpr()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Expr>", indentation);
		Token t = peekNextToken();
		if(t.getType() == Token.Type.DIGIT){
			cstIdentValues.push(indentation+1);
			parseIntExpr();
			if(parseError) return;
		}
		else if(t.getType() == Token.Type.QUOTE){
			cstIdentValues.push(indentation+1);
			parseStringExpr();
			if(parseError) return;
		}
		else if(t.getType() == Token.Type.LPAREN){
			cstIdentValues.push(indentation+1);
			parseBooleanExpr();
			if(parseError) return;
		}
		else if(t.getType() == Token.Type.ID){
			cstIdentValues.push(indentation+1);
			parseId();
			if(parseError) return;
		}
		else{
			if(debug){
				System.out.println("PARSER: ERROR: Expected Expr Got " + t.toString());
			}
			parseError = true;
			return;
		}
		cstIdentValues.pop();
	}
	
	private void parseIntExpr(){
		if(debug) System.out.println("PARSER: parseIntExpr()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<IntExpr>", indentation);
		cstIdentValues.push(indentation+1);
		parseDigit();
		if(parseError) return;
		Token t = peekNextToken();
		if(t.getType() == Token.Type.PLUS){
			cstIdentValues.push(indentation+1);
			parseIntOp();
			if(parseError) return;
			cstIdentValues.push(indentation+1);
			parseExpr();
			if(parseError) return;
		}
		cstIdentValues.pop();
	}
	
	private void parseStringExpr(){
		if(debug) System.out.println("PARSER: parseStringExpr()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<StringExpr>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.QUOTE){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_QUOTE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("[\"]", indentation+1);
		cstIdentValues.push(indentation+1);
		parseCharList();
		if(parseError) return;
		t = getNextToken();
		if(t.getType() != Token.Type.QUOTE){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_QUOTE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("[\"]", indentation+1);
		cstIdentValues.pop();
	}
	
	private void parseId(){
		if(debug) System.out.println("PARSER: parseId()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Id>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.ID){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_VARIABLE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.pop();
	}
	
	
	private void parseCharList(){
		if(debug) System.out.println("PARSER: parseCharList()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<CharList>", indentation);
		Token t = peekNextToken();
		if(t.getType() == Token.Type.CHAR){
			cstIdentValues.push(indentation+1);
			parseChar();
			if(parseError) return;
			cstIdentValues.push(indentation+1);
			parseCharList();
			if(parseError) return;
		}
		cstIdentValues.pop();
	}
	
	private void parseType(){
		if(debug) System.out.println("PARSER: parseType()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Type>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.TYPE){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_TYPE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.pop();
	}
	
	private void parseChar(){
		if(debug) System.out.println("PARSER: parseChar()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Char>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.CHAR){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_CHAR] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.pop();
	}
	
	private void parseSpace(){
		if(debug) System.out.println("PARSER: parseSpace()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Space>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.CHAR || t.getLexeme().equals(" ")){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_SPACE] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.pop();
	}
	
	private void parseDigit(){
		if(debug) System.out.println("PARSER: parseDigit()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<Digit>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.DIGIT){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_DIGIT] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("["+t.getLexeme()+"]", indentation+1);
		cstIdentValues.pop();
	}
	
	private void parseBoolOp(){
		if(debug) System.out.println("PARSER: parseBoolOp()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<BooleanOp>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.BOOLOP_EQUAL && t.getType() != Token.Type.BOOLOP_NOT_EQUAL){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_BOOL_OP] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		if(t.getType() == Token.Type.BOOLOP_EQUAL){
			appendCSTHeader("[==]", indentation+1);
		}
		else{
			appendCSTHeader("[!=]", indentation+1);
		}
		cstIdentValues.pop();
	}
	
	private void parseIntOp(){
		if(debug) System.out.println("PARSER: parseIntOp()");
		int indentation = cstIdentValues.peek();
		appendCSTHeader("<IntOp>", indentation);
		Token t = getNextToken();
		if(t.getType() != Token.Type.PLUS){
			if(debug){
				System.out.println("PARSER: ERROR: Expected [T_INT_OP] Got " + t.toString());
			}
			parseError = true;
			return;
		}
		appendCSTHeader("[+]", indentation+1);
		cstIdentValues.pop();
	}
	

	
