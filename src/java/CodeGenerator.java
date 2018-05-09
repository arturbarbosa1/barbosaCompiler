
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
	

	public boolean generateCode() {
		boolean ok = genCodeBlock((Block)ast);
				
		//code[nextAddress++] = "FF";
		//code[nextAddress++] = "00";
		
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
	
