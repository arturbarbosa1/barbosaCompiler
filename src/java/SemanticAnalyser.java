import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * This class implements the semantic analysis based on abstract syntax tree
 *
 */
public class SemanticAnalyser {
  
 	private AST ast;
	private int numErrors;
	SymbolTable globalSymTable = new SymbolTable();
	SymbolTable currSymTable = new SymbolTable();
	Stack<Integer> scopeStack = new Stack<Integer>();
	Stack<SymbolTable> stackSymTable = new Stack<SymbolTable>();
	List<Entry> entries = new ArrayList<Entry>();
	
	public SemanticAnalyser(AST ast) {
		this.ast = ast;
		numErrors = 0;
		stackSymTable.push(currSymTable);
	}

	public boolean validate() {		
		Block block = (Block)ast;
		validateBlock(block);
		return (numErrors == 0);
	}
	
	public void validateStatement(Statement stmt) {
		if(stmt instanceof VariableDeclaration)
			validateVarDeclaration((VariableDeclaration)stmt);
		else if(stmt instanceof PrintStatement)
			validatePrintStatement((PrintStatement)stmt);
		else if(stmt instanceof WhileStatement)
			validateWhileStatement((WhileStatement)stmt);
		else if(stmt instanceof IfStatement)
			validateIfStatement((IfStatement)stmt);
		else if(stmt instanceof AssignmentStatement)
			validateAssignmentStatement((AssignmentStatement)stmt);
		else if(stmt instanceof Block)
			validateBlock((Block)stmt);
		
	}
	
	public void validateVarDeclaration(VariableDeclaration stmt) {
		currSymTable = stackSymTable.peek();
		String idName = stmt.getId().getLexeme();
		if(currSymTable.contains(idName)) {
			System.out.println("Error: duplicate declaration id " + idName + " on line " + stmt.getId().getLineNo());
			numErrors++;
		}
		else {
			int scope = scopeStack.peek();
			Entry e = new Entry(idName, stmt.getType(), scope);
			currSymTable.addEntry(e);
			entries.add(e);
		}
	}
	
	public void validatePrintStatement(PrintStatement stmt) {
		Expr expr = stmt.getExpr();
		validateExpr(expr);
	}
	
	public void validateWhileStatement(WhileStatement stmt) {
		BooleanExpr expr = stmt.getBoolExpr();
		validateExpr(expr);
		Block block = stmt.getBlock();
		validateBlock(block);
	}
	
	public void validateIfStatement(IfStatement stmt) {
		BooleanExpr expr = stmt.getBoolExpr();
		validateExpr(expr);
		Block block = stmt.getBlock();
		validateBlock(block);
	}
	
	public void validateAssignmentStatement(AssignmentStatement stmt) {
		currSymTable = stackSymTable.peek();
		String idName = stmt.getId().getLexeme();
		if(!currSymTable.contains(idName) && !isIdDeclaredInOuterScope(idName)) {
			System.out.println("Error: The id " + idName + " on line " + stmt.getId().getLineNo() + " was used before being declared");
			numErrors++;
		}
		Expr expr = stmt.getExpr();
		validateExpr(expr);
	}
	
	public void validateBlock(Block block) {
		stackSymTable.push(new SymbolTable());
		if(scopeStack.isEmpty())
			scopeStack.push(0);
		else
			scopeStack.push(scopeStack.peek()+1);
		
		List<Statement> statements = block.getStatements();
		
		for(Statement stmt : statements)
			validateStatement(stmt);
		
		stackSymTable.pop();
		scopeStack.pop();
	}
	
	public void validateExpr(Expr expr) {
		if(expr instanceof AddExpr)
			validateAddExpr((AddExpr)expr);
		else if(expr instanceof Id)
			validateId((Id)expr);
		else if(expr instanceof BooleanExpr)
			validateBooleanExpr((BooleanExpr)expr);
	}
	
	public void validateBooleanExpr(BooleanExpr expr) {
		if(expr instanceof BooleanOp) {
			BooleanExpr expr1 =  ((BooleanOp)expr).getExpr1();
			BooleanExpr expr2 =  ((BooleanOp)expr).getExpr2();
			validateBooleanExpr(expr1);
			validateBooleanExpr(expr2);
		}
	}
	
	public void validateAddExpr(AddExpr expr) {
		IntExpr expr2 = expr.getExpr();
		validateExpr(expr2);
	}
