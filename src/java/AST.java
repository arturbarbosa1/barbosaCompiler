import java.util.ArrayList;
import java.util.List;
/*
 * Abstract Syntax Tree produced by parser
 */

//Display AST with given idenatation to console @param identation
abstract public class AST {	
	abstract void print(int indentation);
}

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

abstract class Statement extends AST{		
}

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

abstract class Expr extends AST {	
}

abstract class IntExpr extends Expr {
}

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

abstract class BooleanExpr extends Expr {
}

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

class Id extends Expr {
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
	


 

