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


 

