
/**
   * file: AST.java
   * author: Artur Barbosa
   * course: CMPT 432
   * assignment: project 3  
   * due date: 4/3/2018
   * Version:3
   * 
   * This file contains the AST for project 3.
   *
   */

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Syntax Tree produced by parsing <Program>
 */
abstract public class AST {	
	/*
	 * Display the AST with given indentation to console
	 * @param indentation
	 */
	abstract void print(int indentation);
}

/**
 * Abstract Syntax Tree for parsing <Block>
 */
class Block extends Statement{		
	private List<Statement> statements;
	
	public Block() {
		statements = new ArrayList<Statement>();
	}
	public void addStatement(Statement stmt) {
		statements.add(stmt);
	}	
	public List<Statement> getStatements() {
		return statements;
	}
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("< BLOCK >");
		for(Statement stmt : statements)
			stmt.print(indentation + 1);
	}	
}

/**
 * Abstract Syntax Tree for parsing <Statement>
 */
abstract class Statement extends AST{		
}
 
/**
 * Abstract Syntax Tree for parsing <PrintStatement>
 */
class PrintStatement extends Statement{
	private Expr expr;
	public PrintStatement(Expr expr) {
		this.expr = expr;
	}	
	public Expr getExpr() {
		return expr;
	}
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("< Print Statement >");
		expr.print(indentation+1);
	}	
}

/**
 * Abstract Syntax Tree for parsing <AssignmentStatement>
 */
class AssignmentStatement extends Statement{
	private Token id;
	private Expr expr;
	public AssignmentStatement(Token id, Expr expr) {
		this.expr = expr;
		this.id = id;
	}	
	public Token getId() {
		return id;
	}
	public Expr getExpr() {
		return expr;
	}

	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("< Assignment Statement >");
		for(int i=0; i < indentation+1; i++)
			System.out.print("-");
		System.out.println("[ " + id.getLexeme() + " ]");
		expr.print(indentation+1);
	}	
}

/**
 * Abstract Syntax Tree for parsing <VariableDeclaration>
 */
class VariableDeclaration extends Statement {
	private Token type;
	private Token id;
	public VariableDeclaration(Token type, Token id) {
		this.type = type;
		this.id = id;
	}	
	public Token getType() {
		return type;
	}
	public Token getId() {
		return id;
	}

	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("< Variable Declaration >");
		for(int i=0; i < indentation+1; i++)
			System.out.print("-");
		System.out.println("[ " + type.getLexeme()+" ]");
		for(int i=0; i < indentation+1; i++)
			System.out.print("-");
		System.out.println("[ " + id.getLexeme()+" ]");
	}	
}

/**
 * Abstract Syntax Tree for parsing <WhileStatement>
 */
class WhileStatement extends Statement{
	private BooleanExpr boolExpr;
	private Block block;
	public WhileStatement(BooleanExpr boolExpr, Block block) {
		this.block = block;
		this.boolExpr = boolExpr;
	}	
	public BooleanExpr getBoolExpr() {
		return boolExpr;
	}
	public Block getBlock() {
		return block;
	}

	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("< While Statement >");
		boolExpr.print(indentation+1);
		block.print(indentation+1);
	}	
}

/**
 * Abstract Syntax Tree for parsing <IfStatement>
 */
class IfStatement extends Statement{
	private BooleanExpr boolExpr;
	private Block block;
	public IfStatement(BooleanExpr boolExpr, Block block) {
		this.block = block;
		this.boolExpr = boolExpr;
	}
	public BooleanExpr getBoolExpr() {
		return boolExpr;
	}
	public Block getBlock() {
		return block;
	}
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("< If Statement >");
		boolExpr.print(indentation+1);
		block.print(indentation+1);
	}	
}

/**
 * Abstract Syntax Tree for parsing <Expr>
 */
abstract class Expr extends AST {	
}

/**
 * Abstract Syntax Tree for parsing <IntExpr>
 */
abstract class IntExpr extends Expr {
}

/**
 * Abstract Syntax Tree for parsing <Digit>
 */
class Digit extends IntExpr{
	private Token digit;
	public Digit(Token digit) {
		this.digit = digit;
	}
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("[ " + digit.getLexeme()+" ]");
	}
	
}

/**
 * Abstract Syntax Tree for parsing <Digit> + <IntExpr>
 */
class AddExpr extends IntExpr{
	private IntExpr digit;
	private IntExpr expr;
	public AddExpr(IntExpr digit, IntExpr expr) {
		this.digit = digit;
		this.expr = expr;
	}	
	public IntExpr getExpr() {
		return expr;
	}
	@Override
	void print(int indentation) {
		digit.print(indentation);
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("[ + ]");
		expr.print(indentation);
	}	
}

/**
 * Abstract Syntax Tree for parsing <StringExpr>
 */
class StringExpr extends Expr {
	private String s;
	public StringExpr(String s) {
		this.s = s;
	}	
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("[ "+s+" ]");
	}	
}

/**
 * Abstract Syntax Tree for parsing <BooleanExpr>
 */
abstract class BooleanExpr extends Expr {
}

/**
 * Abstract Syntax Tree for parsing <BooleanValue> [ true/false ]
 */
class BooleanValue extends BooleanExpr{
	private Token boolVal;
	public BooleanValue(Token boolVal) {
		this.boolVal = boolVal;
	}
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("[ "+boolVal.getLexeme()+" ]");
	}	
}

/**
 * Abstract Syntax Tree for parsing <Expr> boolop <Expr>
 */
class BooleanOp extends BooleanExpr{
	private BooleanExpr expr1;
	private BooleanExpr expr2;
	private Token boolOp;
	public BooleanOp(BooleanExpr expr1, BooleanExpr expr2, Token boolOp) {
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.boolOp = boolOp;
	}	
	public BooleanExpr getExpr1() {
		return expr1;
	}
	public BooleanExpr getExpr2() {
		return expr2;
	}
	@Override
	void print(int indentation) {
		expr1.print(indentation);
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		
		System.out.println("[ "+boolOp.getLexeme()+" ]");
		expr2.print(indentation);
	}	
}

/**
 * Abstract Syntax Tree for parsing <Id>
 */
class Id extends IntExpr {
	private Token id;
	public Id(Token id) {
		this.id = id;
	}	
	public Token getToken() {
		return id;
	}
	
	@Override
	void print(int indentation) {
		for(int i=0; i < indentation; i++)
			System.out.print("-");
		System.out.println("[ " + id.getLexeme() + " ]");
	}
}



