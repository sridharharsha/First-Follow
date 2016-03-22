import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class First {
	
	private String firstTerm;
	private Set<String> productions;
	private List<String> nonTerminals;
	private List<String> terminals;
	private Set<String> varNonTerminals;
	private Set<String> varTerminals;
	
	
	
	public First(String firstTerm, Set<String> productions,
			List<String> nonTerminals, List<String> terminals) {
		super();
		this.firstTerm = firstTerm;
		this.productions = productions;
		this.nonTerminals = nonTerminals;
		this.terminals = terminals;
		varNonTerminals = new HashSet<String>();
		varTerminals = new HashSet<String>();
		for(String nonTerm : productions){
			String[] splitArr = nonTerm.split("\\s+");
			if(nonTerminals.contains(splitArr[0]) && !firstTerm.equals(splitArr[0])){
				varNonTerminals.add(splitArr[0]);
			}
		}
		for(String nonTerm : productions){
			String[] splitArr = nonTerm.split("\\s+");
			if(terminals.contains(splitArr[0])){
				varTerminals.add(splitArr[0]);
			}
			if(splitArr[0].equals("empty")){
				varTerminals.add("empty");
			}
		}


	}
	
//	   public Application()
//	    {
//	        degrees = new ArrayList<Degree>();
//	        rounds = new ArrayList<Round>();
//	        for( int i = 0; i < 3; ++i )
//	            rounds.add( new Round( this ) );
//	    }


	

	public String getFirstTerm() {
		return firstTerm;
	}
	public void setFirstTerm(String firstTerm) {
		this.firstTerm = firstTerm;
	}
	public Set<String> getProductions() {
		return productions;
	}
	public void setProductions(Set<String> productions) {
		this.productions = productions;
	}
	public Set<String> getVarNonTerminals() {
		return varNonTerminals;
	}

	public List<String> getNonTerminals() {
		return nonTerminals;
	}

	public void setNonTerminals(List<String> nonTerminals) {
		this.nonTerminals = nonTerminals;
	}

	public List<String> getTerminals() {
		return terminals;
	}

	public void setTerminals(List<String> terminals) {
		this.terminals = terminals;
	}
	
	public Set<String> getVarTerminals() {
		return varTerminals;
	}

	public void setVarNonTerminals(Set<String> varNonTerminals) {
		this.varNonTerminals = varNonTerminals;
	}
	public void setVarTerminals(Set<String> varTerminals) {
		this.varTerminals = varTerminals;
	}


}
