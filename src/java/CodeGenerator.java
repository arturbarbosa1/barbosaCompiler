
/**
   * file: CodeGenerator.java
   * author: Artur Barbosa
   * course: CMPT 432
   * assignment: project 4  
   * due date: 5/8/2018
   * Version:4
   * 
   * This file contains the Code Generator for project 4.
   *
   */
   /**
 * This class generates machine code based on parsed AST.
 *
 */
public class CodeGenerator {

	final int CODE_MEMORY_SIZE = 100;
	
	private AST ast;
	private String[] code = new String[CODE_MEMORY_SIZE];
	private int nextAddress;
	private int tempVarNo;
	private int numErrors;
	private int numWarnings;
	
	private HashMap<String, List<Integer>> tempVarAddrRefMap;
	
	private Stack<StackTable> globalStackTable = new Stack<StackTable>();	
	
