
/**
   * file: SymbolTable.java
   * author: Artur Barbosa
   * course: CMPT 432
   * assignment: project 3 
   * due date: 4/3/2018
   * Version:2
   * 
   * This file contains the SymbolTable for project 3.
   *
   */
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

	private Map<String, Entry> symbols;
	
	public SymbolTable() {
		symbols = new HashMap<String, Entry>();
	}
	
	public void addEntry(Entry entry) {
		symbols.put(entry.name, entry);
	}
	
	public boolean contains(String entryName) {
		return symbols.containsKey(entryName);
	}
	//table
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

class Entry{
	String name;
	Token type;
	int scope;
	
	public Entry(String name, Token type, int scope) {
		this.name = name;
		this.type = type;
		this.scope = scope;
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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Entry))
			return false;
		return name.equals(((Entry)obj).name);
	}
}



