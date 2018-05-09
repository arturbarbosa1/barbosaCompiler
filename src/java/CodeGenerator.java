
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
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
	
	/**
	 * Class constructor for code generator that takes an AST parameter.
	 * @param ast an instance of parsed AST 
	 */
	public CodeGenerator(AST ast) {
		this.ast = ast;
		nextAddress = 0;
		tempVarNo = 0;
		numErrors = 0;
		numWarnings = 0;
		tempVarAddrRefMap = new HashMap<String, List<Integer>>();
	}
	
	/**
	 * Generate object code for the parsed AST.
	 * 
	 * @return true if code generated without any error, else return false
	 */
	public boolean generateCode() {
		boolean ok = genCodeBlock((Block)ast);
				
		//code[nextAddress++] = "FF";
		code[nextAddress++] = "00";
		
		for(String tempVar : tempVarAddrRefMap.keySet()) {
			List<Integer> addrRefs = tempVarAddrRefMap.get(tempVar);
			for(int addr : addrRefs) {
				code[addr] = nextAddress+"";
				code[addr+1] = "00";				
			}
			nextAddress++;
		}
		return ok;
	}
	
	private boolean genCodeBlock(Block block) {
		globalStackTable.push(new StackTable());
		
		List<Statement> statements = block.getStatements();
		for(Statement stmt : statements) {
			if(!genCodeStatement(stmt))
				return false;
		}
				
		StackTable st = globalStackTable.pop();
		for(String tempVar : st.getTempVarPosMap().keySet()) {
			List<Integer> addrRefs = st.getTempVarPosMap().get(tempVar);
			tempVarAddrRefMap.put(tempVar, addrRefs);
		}		
		
		return true;
	}
	
	
	/**
	 * Generate object code for any Statement.
	 * 
	 * @param stmt an instance of subclass of Statement AST
	 * @return true if code generated without any error, else return false
	 */
	public boolean genCodeStatement(Statement stmt) {
		
		if(stmt instanceof PrintStatement)
			return genCodePrintStmt((PrintStatement)stmt);
		
		else if(stmt instanceof AssignmentStatement)
			return genCodeAssignStmt((AssignmentStatement)stmt);
		
		else if(stmt instanceof WhileStatement)			
			return genCodeWhileStmt((WhileStatement)stmt);
		
		else if(stmt instanceof IfStatement)			
			return genCodeIfStmt((IfStatement)stmt);
		
		else if(stmt instanceof VariableDeclaration)
			return genCodeVarDeclStmt((VariableDeclaration)stmt);
		
		else if(stmt instanceof Block)
			return genCodeBlock((Block)stmt);		
		else
			return false;
	}
	
	public boolean genCodePrintStmt(PrintStatement stmt) {
		//StackTable currStackTable = globalStackTable.peek();
		Expr expr = stmt.getExpr();
		if(expr instanceof Id) {
			Id id = (Id)expr;
			String idName = id.getToken().getLexeme();
			StackTable st = getStackTableForId(idName);
			String idTempVar = st.getIdTempVarMapping(idName);
			
			if(id.getTypeString().equals("int")) {				
				//if(!tempVarPosMap.containsKey(idName)) {
				//	tempVarPosMap.put(idTempVar, new ArrayList<Integer>());
				//}
				code[nextAddress++] = "AC";
				st.addTempVarAddressRef(idTempVar, nextAddress);
				//tempVarPosMap.get(idTempVar).add(nextAddress);
				code[nextAddress++] = idTempVar;
				code[nextAddress++] = "XX";
				code[nextAddress++] = "A2";
				code[nextAddress++] = "01";
				code[nextAddress++] = "FF";
			}
			else if(id.getTypeString().equals("string")) {				
//				if(!tempVarPosMap.containsKey(idName)) {
//					tempVarPosMap.put(idTempVar, new ArrayList<Integer>());
//				}
				code[nextAddress++] = "AC";
				st.addTempVarAddressRef(idTempVar, nextAddress);
				//tempVarPosMap.get(idTempVar).add(nextAddress);
				code[nextAddress++] = idTempVar;
				code[nextAddress++] = "XX";
				code[nextAddress++] = "A2";
				code[nextAddress++] = "02";
				code[nextAddress++] = "FF";
			}
			else if(id.getTypeString().equals("boolean")) {
				
			}
		}
		return true;
	}
	
	public boolean genCodeAssignStmt(AssignmentStatement stmt) {
		
		String idName = stmt.getId().getLexeme();
		Expr expr = stmt.getExpr();
		StackTable st = getStackTableForId(idName);
		String tempVar = st.getIdTempVarMapping(idName);
		
		Digit d = (Digit)expr;
		int num = Integer.parseInt(d.getToken().getLexeme());
		code[nextAddress++] = "A9";
		code[nextAddress++] = toHex(num);
		code[nextAddress++] = "8D";
		st.addTempVarAddressRef(tempVar, nextAddress);
		code[nextAddress++] = tempVar;
		code[nextAddress++] = "XX";
