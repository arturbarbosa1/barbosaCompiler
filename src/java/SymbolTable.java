
/**
   * file: SymbolTable.java
   * author: Artur Barbosa
   * course: CMPT 432
   * assignment: project 4
   * due date: 5/8/2018
   * Version:4
   * 
   * This file contains the SymbolTable for project 4.
   *
   */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the symbol table for performing semantic analysis
 * of input program written as per given grammar.
 */
public class SymbolTable {

	/** hash table to store all symbol table entries keyed with symbol name */
	private Map<String, Entry> symbols;
	
	public SymbolTable() {
		symbols = new HashMap<String, Entry>();
	}
	
	/**
	 * Add a new entry to the hash table.
	 * 
	 * @param entry an Entry object
	 */
	public void addEntry(Entry entry) {
		symbols.put(entry.name, entry);
	}
	
	/**
	 * Check whether hash table contains an entry with given name
	 * @param entryName name of the entry
	 * @return true if hash table contains an entry with name, else return false
	 */
	public boolean contains(String entryName) {
		return symbols.containsKey(entryName);
	}
	
	public Entry getEntry(String name) {
		if(contains(name))
			return symbols.get(name);
		else
			return null;
	}
	
	public List<Entry> getEntries(){
		List<Entry> entries = new ArrayList<Entry>();
		for(String name : symbols.keySet()) {
			entries.add(symbols.get(name));
		}
		return entries;
	}
	
	/**
	 * Display the symbol table to console screen
	 */
	public void print() {
		System.out.println("--------------------------");
		System.out.println("Name Type      Scope  Line");
		System.out.println("--------------------------");
		for(String name : symbols.keySet()) {
			Entry e = symbols.get(name);
			System.out.println(name+"    " + e.getType().getLexeme()+"     " + e.getScope() + "  " + e.getType().getLineNo());
		}
	}
}

/**
 * This class represents an entry in the symbol table and store 
 * information of an id with its name, scope and type.
 *
 */
class Entry{
	String name;
	Token type;
	int scope;
	boolean initialised;
	boolean used;
	
	public Entry(String name, Token type, int scope) {
		this.name = name;
		this.type = type;
		this.scope = scope;
		initialised = false;
		used = false;
	}

	public String getName() {
		return name;
	}

	public Token getType() {
		return type;
	}

	public int getScope() {
		return scope;
	}
		
	public boolean isInitialised() {
		return initialised;
	}

	public void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}

	
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Entry))
			return false;
		return name.equals(((Entry)obj).name);
	}
}
