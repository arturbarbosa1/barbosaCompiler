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
