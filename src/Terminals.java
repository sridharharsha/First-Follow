import java.util.Set;


public class Terminals {
	
	private String key;
	private Set<String> productions;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Set<String> getProductions() {
		return productions;
	}
	public void setProductions(Set<String> productions) {
		this.productions = productions;
	}
	public Terminals(String key, Set<String> productions) {
		super();
		this.key = key;
		this.productions = productions;
	}
	
	

}
