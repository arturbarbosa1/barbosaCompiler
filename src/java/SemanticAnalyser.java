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
