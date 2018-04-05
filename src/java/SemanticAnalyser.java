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
