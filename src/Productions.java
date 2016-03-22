
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Productions {
	
	private Integer number;
	private String lhs;
	private Set<String> rhs;
	
	private static int count = 1;
	
	public Productions(){
		
	}
	
	
	public Productions(String lhs, Set<String> rhs) {
		
		this.number = count++;
		this.lhs = lhs;
		this.rhs = rhs;
	}


	public Integer getNumber() {
		return number;
	}


	public String getLhs() {
		return lhs;
	}


	public void setLhs(String lhs) {
		this.lhs = lhs;
	}


	public Set<String> getRhs() {
		return rhs;
	}


	public void setRhs(Set<String> rhs) {
		this.rhs = rhs;
	}
	

}
