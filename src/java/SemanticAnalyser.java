/**
   * file: SemanticAnalyser.java
   * author: Artur Barbosa
   * course: CMPT 432
   * assignment: project 3  
   * due date: 4/3/2018
   * Version 3
   * 
   * This file contains the Semantic Analyser for project 3.
   *
   */
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
	private int numWarnings;
	SymbolTable globalSymTable = new SymbolTable();
	SymbolTable currSymTable = new SymbolTable();
	Stack<Integer> scopeStack = new Stack<Integer>();
	Stack<SymbolTable> stackSymTable = new Stack<SymbolTable>();
	List<Entry> entries = new ArrayList<Entry>();
	
	/**
	 * Class constructor that takes an abstract syntax tree as
	 * parameter to process.
	 * 
	 * @param ast An AST object
	 */
	public SemanticAnalyser(AST ast) {
		this.ast = ast;
		numErrors = 0;
		numWarnings = 0;
		stackSymTable.push(currSymTable);
	}
	
	/**
	 * Process the input AST for validating if its semantically correct or not.
	 * 
	 * @return true if AST confirms to semantically correct program, else return false
	 */
	public boolean validate() {		
		Block block = (Block)ast;
		validateBlock(block);
		return (numErrors == 0);
	}
	
	/**
	 * Validate a Statement AST.
	 * 
	 * @param stmt a Statement AST
	 */
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
	
	/**
	 * Validate a VariableDeclaration AST.
	 * 
	 * @param stmt a instance of VariableDeclaration AST
	 */
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
	
	/**
	 * Validate a PrintStatement AST.
	 * 
	 * @param stmt a instance of PrintStatement AST
	 */
	public void validatePrintStatement(PrintStatement stmt) {
		Expr expr = stmt.getExpr();
		validateExpr(expr);
	}
	
	/**
	 * Validate a WhileStatement AST.
	 * 
	 * @param stmt a instance of WhileStatement AST
	 */
	public void validateWhileStatement(WhileStatement stmt) {
		BooleanExpr expr = stmt.getBoolExpr();
		validateExpr(expr);
		Block block = stmt.getBlock();
		validateBlock(block);
	}
	
	/**
	 * Validate a IfStatement AST.
	 * 
	 * @param stmt a instance of IfStatement AST
	 */
	public void validateIfStatement(IfStatement stmt) {
		BooleanExpr expr = stmt.getBoolExpr();
		validateExpr(expr);
		Block block = stmt.getBlock();
		validateBlock(block);
	}
	
	/**
	 * Validate a AssignmentStatement AST.
	 * 
	 * @param stmt a instance of AssignmentStatement AST
	 */
	public void validateAssignmentStatement(AssignmentStatement stmt) {
		currSymTable = stackSymTable.peek();
	
		String idName = stmt.getId().getLexeme();
		Token idType=new Token("$", Token.Type.EOF,1);
		//System.out.println(currSymTable);
		Entry e = currSymTable.getEntry(idName);
		if(e == null)
			e = getEntryFromImmediateOuterScope(idName);
		int lineNo=0;
		if(e != null) {
			e.setInitialised(true);
			idType = e.getType();
			lineNo = e.getType().getLineNo();
		}
		
		if(!currSymTable.contains(idName) && !isIdDeclaredInOuterScope(idName)) {
			System.out.println("Error: The id " + idName + " on line " + stmt.getId().getLineNo() + " was used before being declared");
			numErrors++;
		}
		
		Expr expr = stmt.getExpr();
		validateExpr(expr);		
		if(!idType.getLexeme().equals(expr.getTypeString())) {
			numErrors++;
			System.out.println("Error: Variable [ " + idName + " ] on line " +  stmt.getId().getLineNo() + " has type [ " + idType.getLexeme() + 
					" ] and is assigned the wrong type [ " + expr.getTypeString()+ " ]...");
		}
	}

	/**
	 * Validate a Block AST.
	 * 
	 * @param stmt a instance of Block AST
	 */
	public void validateBlock(Block block) {
		stackSymTable.push(new SymbolTable());
		if(scopeStack.isEmpty())
			scopeStack.push(0);
		else
			scopeStack.push(scopeStack.peek()+1);
		
		List<Statement> statements = block.getStatements();
		
		for(Statement stmt : statements)
			validateStatement(stmt);
		
		SymbolTable symTop = stackSymTable.pop();
		checkVarsInitAndUsage(symTop);
		scopeStack.pop();
	}
	
	private void checkVarsInitAndUsage(SymbolTable symTabl) {
		for(Entry e : symTabl.getEntries()) {
			if(!e.isInitialised())
				numWarnings++;
			if(!e.isUsed())
				numWarnings++;
		}
	}
	
	/**
	 * Validate a Expr AST.
	 * 
	 * @param stmt a instance of Expr AST
	 */
	public void validateExpr(Expr expr) {
		if(expr instanceof AddExpr)
			validateAddExpr((AddExpr)expr);
		else if(expr instanceof Id)
			validateId((Id)expr);
		else if(expr instanceof BooleanExpr)
			validateBooleanExpr((BooleanExpr)expr);
	}
	
	/**
	 * Validate a BooleanExpr AST.
	 * 
	 * @param stmt a instance of BooleanExpr AST
	 */
	public void validateBooleanExpr(BooleanExpr expr) {
		if(expr instanceof BooleanOp) {
			Expr expr1 =  ((BooleanOp)expr).getExpr1();
			Expr expr2 =  ((BooleanOp)expr).getExpr2();
			validateExpr(expr1);
			validateExpr(expr2);
		}
	}
	
	/**
	 * Validate a AddExpr AST.
	 * 
	 * @param stmt a instance of AddExpr AST
	 */
	public void validateAddExpr(AddExpr expr) {
		Expr expr2 = expr.getExpr();
		validateExpr(expr2);
		Digit digit = (Digit)expr.getDigit();
		int lineNo = digit.getToken().getLineNo();
		if(!"int".equals(expr2.getTypeString())) {
			numErrors++;
			System.out.println("Error: Expr on line " + lineNo + " has type [  int  ] and is added the wrong type [ " + expr2.getTypeString() + " ]...");
		}
	}
	
	/**
	 * Validate a Id AST.
	 * 
	 * @param stmt a instance of Id AST
	 */
	public void validateId(Id id) {
		currSymTable = stackSymTable.peek();
		String idName = id.getToken().getLexeme();
		Entry e = currSymTable.getEntry(idName);
		if(e == null)
			e = getEntryFromImmediateOuterScope(idName);
		
		if(e != null) {
			e.setUsed(true);
			id.setType(e.getType());
		}
		
		if(!currSymTable.contains(idName) && !isIdDeclaredInOuterScope(idName)) {
			System.out.println("Error: The id " + idName + " on line " + id.getToken().getLineNo() + " was used before being declared");
			numErrors++;
		}
	}
	
	/**
	 * Get the number of errors encountered while checking semantic analysis.
	 * 
	 * @return the number of errors
	 */
	public int getNumErrors() {
		return numErrors;
	}

	
	public int getNumWarnings() {
		return numWarnings;
	}

	public List<Entry> getEntries() {
		return entries;
	}
		
	/**
	 * Check whether an id name is already declared in the outer scope of 
	 * current scope.
	 * 
	 * @param id the name of the id
	 * @return true if id is already in outer scope, else return false
	 */
	private boolean isIdDeclaredInOuterScope(String id) {
		int size = stackSymTable.size();
		for(int i=0; i < size-1; i++) {
			SymbolTable symtbl = stackSymTable.get(i);
			if(symtbl.contains(id))
				return true;
		}
		return false;
	}
	
	private Entry getEntryFromImmediateOuterScope(String id) {
		int size = stackSymTable.size();
		for(int i=size-1; i >=0 ; i--) {
			SymbolTable symtbl = stackSymTable.get(i);
			if(symtbl.contains(id))
				return symtbl.getEntry(id);
		}
		return null;
	}
}
