
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

	final int CODE_MEMORY_SIZE = 256;
	
	private AST ast;
	private String[] code = new String[CODE_MEMORY_SIZE];
	private int nextAddress;
	private int tempVarNo;
	private int numErrors;
	private int numWarnings;
	
	private HashMap<String, List<Integer>> tempVarAddrRefMap;
	
	private Stack<StackTable> globalStackTable = new Stack<StackTable>();	
	
	private int heapEndAddr;
	
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
		heapEndAddr = CODE_MEMORY_SIZE - 1;
		
		for(int i= 0; i < CODE_MEMORY_SIZE; i++) {
			code[i] = "00";
		}
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
		
//		Digit d = (Digit)expr;
//		int num = Integer.parseInt(d.getToken().getLexeme());
//		code[nextAddress++] = "A9";
//		code[nextAddress++] = toHex(num);
//		code[nextAddress++] = "8D";
//		st.addTempVarAddressRef(tempVar, nextAddress);
//		code[nextAddress++] = tempVar;
//		code[nextAddress++] = "XX";
		
		
		if(expr instanceof Digit) {
			Digit d = (Digit)expr;
			int num = Integer.parseInt(d.getToken().getLexeme());
			code[nextAddress++] = "A9";
			code[nextAddress++] = toHex(num);
			code[nextAddress++] = "8D";
			st.addTempVarAddressRef(tempVar, nextAddress);
			code[nextAddress++] = tempVar;
			code[nextAddress++] = "XX";
		}
		else if(expr instanceof StringExpr) {
			String s = ((StringExpr)expr).getString();
			code[heapEndAddr--] = "00";
			for(int i = s.length()-1; i >= 0; i--) {
				int c = (int)s.charAt(i);
				String hex = toHex(c);
				code[heapEndAddr--] = hex;
			}
			code[nextAddress++] = "A9";
			code[nextAddress++] = toHex(heapEndAddr);
			code[nextAddress++] = "8D";
			st.addTempVarAddressRef(tempVar, nextAddress);
			code[nextAddress++] = tempVar;
			code[nextAddress++] = "XX";
		}
		else if(expr instanceof Id) {
			String idName2 = ((Id)expr).getToken().getLexeme();
			String tempVar2 = st.getIdTempVarMapping(idName2);
			code[nextAddress++] = "AD";
			st.addTempVarAddressRef(tempVar2, nextAddress);
			code[nextAddress++] = tempVar2;
			code[nextAddress++] = "XX";
			code[nextAddress++] = "8D";	
			st.addTempVarAddressRef(tempVar, nextAddress);
			code[nextAddress++] = tempVar;
			code[nextAddress++] = "XX";
		}
		
		return true;
	}
	
	
	public boolean genCodeVarDeclStmt(VariableDeclaration stmt) {
		StackTable currStackTable = globalStackTable.peek();
		
		String idName = stmt.getId().getLexeme();
		currStackTable.addIdTempVarMapping(idName, tempVarNo);
		String tempVar = currStackTable.getIdTempVarMapping(idName);
		tempVarNo++;
		
		if(stmt.getType().getLexeme().equals("int")) {
			code[nextAddress++] = "A9";
			code[nextAddress++] = "00";		
			code[nextAddress++] = "8D";
			currStackTable.addTempVarAddressRef(tempVar, nextAddress);
			code[nextAddress++] = tempVar;
			code[nextAddress++] = "XX";
		}			
		return true;
	}
	
	public boolean genCodeWhileStmt(WhileStatement stmt) {
		BooleanExpr boolExpr = stmt.getBoolExpr();
		if(boolExpr instanceof BooleanOp) {
			BooleanOp op = (BooleanOp)boolExpr;
			Expr expr1 = op.getExpr1();
			Expr expr2 = op.getExpr2();
			Token boolOpToken = op.getBoolOpToken();
			int lineNo = boolOpToken.getLineNo();
			
			if(!expr1.getTypeString().equals(expr2.getTypeString())) {
				System.out.println("C.GEN --> Error! Comparison between incompatible expressions in line " + lineNo);
				numErrors++;
				return false;
			}
			if((expr1 instanceof StringExpr) && (expr2 instanceof Id)) {
				System.out.println("C.GEN --> Error! Variable to string comparison is not allowed in line  " + lineNo);
				numErrors++;
				return false;
			}
			if((expr2 instanceof StringExpr) && (expr1 instanceof Id)) {
				System.out.println("C.GEN --> Error! Variable to string comparison is not allowed in line  " + lineNo);
				numErrors++;
				return false;
			}
			
		}
		else {
			BooleanValue boolVal = (BooleanValue)boolExpr;
		}
		return true;
	}
	
	public boolean genCodeIfStmt(IfStatement stmt) {
		return true;
	}
	
	/**
	 * Get the generated object code.
	 * 
	 * @return the object as a string
	 */
	public String getCode() {
		String s = "";
		int k = 0;
		for(int i= 0; i < CODE_MEMORY_SIZE; i++) {
			k++;
			if(k % 8 == 0) {
				s += code[i]+"\n";
				k = 0;
			}
			else {
				s += code[i]+" ";
			}
		}
		return s;
	}
	
	public int getNumErrors() {
		return numErrors;
	}
	
	public int getNumWarnings() {
		return numWarnings;
	}
	
	
	private String toHex(int dec) {
		return String.format("%02x", dec);
	}
	
	private StackTable getStackTableForId(String idName) {
		int scopeLevels = globalStackTable.size();
		for(int i = scopeLevels-1; i >= 0; i--) {
			StackTable st = globalStackTable.get(i);
			String tempVar = st.getIdTempVarMapping(idName);
			if(tempVar != null)
				return st;
		}
		return null;
	}
	
	
	class StackTable{
		private HashMap<String, String> idToTempVarMap;
		private HashMap<String, List<Integer>> tempVarPosMap;
		
		public StackTable() {
			idToTempVarMap = new HashMap<String, String>();
			tempVarPosMap = new HashMap<String, List<Integer>>();
		}
		
		
		public HashMap<String, List<Integer>> getTempVarPosMap() {
			return tempVarPosMap;
		}


		public void addIdTempVarMapping(String idName, int tempVarNo) {
			idToTempVarMap.put(idName, "t"+tempVarNo);
		}
		
		public void addTempVarAddressRef(String tempVar, int address) {
			if(!tempVarPosMap.containsKey(tempVar)) {
				tempVarPosMap.put(tempVar, new ArrayList<Integer>());
			}
			tempVarPosMap.get(tempVar).add(address);
		}
		
		public String getIdTempVarMapping(String idName) {
			if(idToTempVarMap.containsKey(idName))
				return idToTempVarMap.get(idName);
			else
				return null;
		}
		
		public List<Integer> getTempVarAddrRefList(String tempVar){
			if(tempVarPosMap.containsKey(tempVar))
				return tempVarPosMap.get(tempVar);
			else
				return null;
		}
	}
	
}



