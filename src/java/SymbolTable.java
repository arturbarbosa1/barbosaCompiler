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
