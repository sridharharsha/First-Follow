import java.util.*;
import java.io.*;

public class FirstFollow{

	static List<String> nonTermList = new ArrayList<String>();
	static List<String> termList = new ArrayList<String>();
	static HashMap<String, Set<String>> productionMap = new HashMap<String, Set<String>>();
	static HashMap<String, Set<String>> firstMap = new HashMap<String, Set<String>>();
	static Set<Productions> allProductions = new HashSet<Productions>();
	static List<First> allFirstItems = new ArrayList<First>();
	static List<NonTerminals> nonTermClassList = new ArrayList<NonTerminals>();
	static List<Terminals> termClassList = new ArrayList<Terminals>();
	static HashMap<Integer, String> productionOrder = new HashMap<Integer, String>();
	static HashMap<String,Set<String>> followMap = new HashMap<String, Set<String>>();
	static HashMap<String, Set<String>> tempNonTermFollowMap = new HashMap<String, Set<String>>();
	static Set<String> allRhsProductions = new HashSet<String>();
	static Set<String> emptyProductions = new HashSet<String>();
	
	public static void main(String args[]) throws IOException{
		
		
		BufferedReader br = new BufferedReader(new FileReader(new File("GrammarList.txt").getAbsoluteFile()));
			
		String line;
		int orderNum = 1;	
		while ((line = br.readLine()) != null) {
				
			int index = line.indexOf("::");
			String lhs = line.substring(0, index).trim();
			productionOrder.put(orderNum, lhs);
			orderNum++;
			String rhs = line.substring(index+2).trim();
			Set<String> allRhsList = new HashSet<String>();
			if(!productionMap.isEmpty()){
				for(Map.Entry<String, Set<String>> entry : productionMap.entrySet()){
					String mapKey = entry.getKey();
					if(lhs.equals(mapKey)){
						allRhsList.addAll(entry.getValue());
						allRhsList.add(rhs);
						allRhsProductions.add(rhs);
						productionMap.put(mapKey, allRhsList);
					}
				}
			}
			allRhsList.add(rhs);
			productionMap.put(lhs, allRhsList);
			
			}
			
			BufferedReader brNonTerm = new BufferedReader(new FileReader(new File("nonTerminalList.txt").getAbsoluteFile()));
			while((line = brNonTerm.readLine()) != null){
				nonTermList.add(line);
			}
			
			BufferedReader brTerm = new BufferedReader(new FileReader(new File("terminalList.txt").getAbsoluteFile()));
			while((line = brTerm.readLine()) != null){
				termList.add(line);
			}
			br.close();
			brNonTerm.close();
			brTerm.close();
			
			for(Map.Entry<String,Set<String>> productions : productionMap.entrySet()){
				
			String key = productions.getKey();
			Set<String> nonTermProd = new HashSet<String>();
			Set<String> termProd = new HashSet<String>();
			for(String value : productions.getValue()){
				String[] splitArr = value.split("\\s+");
				if(nonTermList.contains(splitArr[0])){
					nonTermProd.add(value);
				}
				if(termList.contains(splitArr[0])){
					termProd.add(value);
				}
				if(splitArr[0].equals("empty")){
					emptyProductions.add(key);
					termProd.add("empty");
				}
			}
			if(nonTermProd.size() > 0){
				NonTerminals nonTerminals = new NonTerminals(key, nonTermProd);
				nonTermClassList.add(nonTerminals);
			}
			if(termProd.size() > 0){
				Terminals terminals = new Terminals(key, termProd);
				termClassList.add(terminals);
			}
		}
			
			
			getFirstForAllNonTerminals();
			getFirstForEmptyProductions();
			getFollowForAllNonTerminals();
//			getAllRemainingFollows();
//			if(followMap.size() != 29){
//				getAllRemainingFollows();
//			}
			
			
			for(Terminals terminals : termClassList){
				if(terminals.getKey().equals("Sl")){
					for(String str : terminals.getProductions()){
						System.out.println(str);
					}
				}
			}
			

			
			
			File fout = new File("First.txt").getAbsoluteFile();
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for(Map.Entry<String,Set<String>> productions : firstMap.entrySet()){
				
				bw.write(productions.getKey()+" "+" :");
				for(String str : productions.getValue()){
					bw.write(" "+str+" ");
				}
				bw.newLine();
			}
			bw.close();
			
//			for(Map.Entry<String, Set<String>> temp : tempNonTermFollowMap.entrySet()){
//				System.out.println(temp.getKey());
//				for(String str2 : temp.getValue()){
//					System.out.println(str2);
//				}
//				System.out.println("------------------");
//			}
			
			for(Map.Entry<String, Set<String>> temp : tempNonTermFollowMap.entrySet()){
//				System.out.println(temp.getKey());
				if(!temp.getKey().equals("P") ){
					for(String str2 : temp.getValue()){
						Set<String> tempFoll = followMap.get(str2);
						if(tempFoll != null && tempFoll.size() > 0){
							if(followMap.containsKey(temp.getKey())){
								Set<String> followKeyVal = followMap.get(temp.getKey());
								followMap.remove(temp.getKey());
								followKeyVal.addAll(tempFoll);
								followMap.put(temp.getKey(), followKeyVal);
							}else{
								followMap.put(temp.getKey(), tempFoll);
							}
						}
						//System.out.println(str2);
					}

				}
								//System.out.println("-------------");
			}
			
			
//			getAllRemainingFollows();
			
//			Integer keyCnt = 1;
//			for(Map.Entry<String,Set<String>> follow : followMap.entrySet()){
//				System.out.println(keyCnt+" "+follow.getKey());
//				for(String str1 : follow.getValue()){
//					System.out.println(str1);
//				}
//				keyCnt++;
//				System.out.println("-------------------------");
//			}
			
			File folOut = new File("Follow.txt").getAbsoluteFile();
			FileOutputStream fos1 = new FileOutputStream(folOut);
		 
			BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(fos1));

			for(Map.Entry<String,Set<String>> productions : followMap.entrySet()){
				bw1.write(productions.getKey()+" "+" :");
				for(String str : productions.getValue()){
					bw1.write(" "+str+" ");
				}
				bw1.newLine();
			}
			bw1.close();

			
		}	
	
	public static void getFirstForEmptyProductions(){
		for(String empstr : emptyProductions){
			Set<String> prod = productionMap.get(empstr);
			for(String prod1 : prod){
				if(!prod1.equals("empty")){
					String[] splitEm = prod1.split("\\s+");
					if(splitEm[0].equals(empstr)){
						if(firstMap.containsKey(empstr)){
							firstMap.remove(empstr);
							Set<String> prodVal = firstMap.get(splitEm[1]);
							Set<String> emptyVal = new HashSet<String>();
							emptyVal.add("empty");
							prodVal.addAll(emptyVal);
							firstMap.put(empstr, prodVal);
							
						}else{
							Set<String> prodVal = firstMap.get(splitEm[1]);
							Set<String> emptyVal = new HashSet<String>();
							emptyVal.add("empty");
							prodVal.addAll(emptyVal);

							firstMap.put(empstr, prodVal);

						}
						
					}
				}
			}
		}
	}


	public static void getFirstForAllNonTerminals(){
		Set<String> startTerminalsKeySet = productionMap.keySet();
		
		for(String firstKey : startTerminalsKeySet){
			Set<String> firstKeyNonTermSet = getKeyNonTerminals(firstKey);
			Set<String> firstKeyTermSet = getKeyTerminals(firstKey);
			
			if(firstKeyNonTermSet.size() > 0){
				for(String str : firstKeyNonTermSet){
					getFirstKeyTerminals(firstKey, str);
				}
			}
			
			if(firstKeyTermSet.size() > 0){
				if(firstMap.containsKey(firstKey)){
					
					Set<String> existingVal = firstMap.get(firstKey);
					existingVal.addAll(firstKeyTermSet);
					firstMap.remove(firstKey);
					
//					for(String empStr : emptyProductions){
//						if(empStr.equals(firstKey)){
//							existingVal.add("empty");
//						}
//					}
					firstMap.put(firstKey, existingVal);
				}
				else{
					
//					for(String empStr : emptyProductions){
//						if(empStr.equals(firstKey)){
//							firstKeyTermSet.add("empty");
//						}
//					}

					firstMap.put(firstKey, firstKeyTermSet);
					
				}
				
			}
		}
	}
	
	
	public static Set<String> getKeyNonTerminals(String key){
		Set<String> keyNonTerminalSet = new HashSet<String>();
			for(NonTerminals nonTermList1 : nonTermClassList){
				if(nonTermList1.getKey().equals(key)){
					for(String prod : nonTermList1.getProductions()){
						String[] splitArr = prod.split("\\s+");
						if(!splitArr[0].equals(key)){
							keyNonTerminalSet.add(splitArr[0]);
						}
						
						
					}
				}
			}
		
		return keyNonTerminalSet;
	}
	
	public static Set<String> getKeyTerminals(String key){
		Set<String> keyTerminalSet = new HashSet<String>();
		
			for(Terminals termList1 : termClassList){
				if(termList1.getKey().equals(key)){
					for(String prod : termList1.getProductions()){
						String[] splitArr = prod.split("\\s+");
						if(!splitArr[0].equals(key)){
							keyTerminalSet.add(splitArr[0]);
						}
					}
				}
			}
		
		
		return keyTerminalSet;
	}
	
	public static void getFirstKeyTerminals(String firstTerm , String firstKeyNonTerm){
		
		
		Set<String> nonTermSet = new HashSet<String>();
		Set<String> termSet = new HashSet<String>();
		for(NonTerminals nonTerms : nonTermClassList){
			if(nonTerms.getKey().equals(firstKeyNonTerm)){
				for(String prod : nonTerms.getProductions()){
					String[] splitArr = prod.split("\\s+");
					if(!splitArr[0].equals(firstTerm) && !splitArr[0].equals(firstKeyNonTerm)){
						nonTermSet.add(splitArr[0]);
						
					}
						
				}
			}
				
		}
		for(Terminals terminals : termClassList){
			if(terminals.getKey().equals(firstKeyNonTerm)){
				for(String prod : terminals.getProductions()){
					String[] splitArr = prod.split("\\s+");
		
					if(!splitArr[0].equals(firstTerm) && !splitArr[0].equals(firstKeyNonTerm)){
						termSet.add(splitArr[0]);
					}
				}
			}
		}
			
		if(termSet.size() > 0){
			
			if(firstMap.containsKey(firstTerm)){
				Set<String> existingVal = firstMap.get(firstTerm);
				firstMap.remove(firstTerm);
//				for(String empStr : emptyProductions){
//					if(empStr.equals(firstTerm)){
//						existingVal.add("empty");
//					}
//				}

				existingVal.addAll(termSet);
				firstMap.put(firstTerm, existingVal);
			}else{
//				for(String empStr : emptyProductions){
//					if(empStr.equals(firstTerm)){
//						termSet.add("empty");
//					}
//				}

				firstMap.put(firstTerm, termSet);
			}
		}
		
		if(nonTermSet.size() > 0){
			getFirstNewTerminals(firstTerm, nonTermSet);
		}
	}
	
	public static void getFirstNewTerminals(String firstTerm,Set<String> nonTerminals){
		
		Set<String> nonTermSet = new HashSet<String>();
		Set<String> termSet = new HashSet<String>();
		
			for(NonTerminals nonTermsList : nonTermClassList){
				for(String str : nonTerminals){
					if(nonTermsList.getKey().equals(str)){
						for(String prod : nonTermsList.getProductions()){
							String[] splitArr = prod.split("\\s+");
							if(!splitArr[0].equals(firstTerm) && !splitArr[0].equals(str)){
							//	temporaryNonTermSet.add(splitArr[0]);
								nonTermSet.add(splitArr[0]);
								
							}
							
						}
					}

				}
				
			}
			for(Terminals terminals : termClassList){
				for(String str : nonTerminals){
					if(terminals.getKey().equals(str)){
						for(String prod : terminals.getProductions()){
							String[] splitArr = prod.split("\\s+");
							if(!splitArr[0].equals(firstTerm) && !splitArr[0].equals(str)){
								termSet.add(splitArr[0]);
							}
						}
					}
				}
				
			}
		if(termSet.size() > 0){
			if(firstMap.containsKey(firstTerm)){
				Set<String> existingVal = firstMap.get(firstTerm);
				existingVal.addAll(termSet);
				firstMap.remove(firstTerm);
				for(String empStr : emptyProductions){
					if(empStr.equals(firstTerm)){
						existingVal.add("empty");
					}
				}

				firstMap.put(firstTerm, existingVal);
			}else{
				for(String empStr : emptyProductions){
					if(empStr.equals(firstTerm)){
						termSet.add("empty");
					}
				}

				firstMap.put(firstTerm, termSet);
			}
		}
		if(nonTermSet.size() > 0){
			getFirstNewTerminals(firstTerm, nonTermSet);
		}
	}
	
	public static void getFollowForAllNonTerminals(){
		for(Map.Entry<Integer, String> order : productionOrder.entrySet()){
			if(order.getKey().equals(1)){
				Set<String> addingDollar = new HashSet<String>();
				addingDollar.add("$");
				followMap.put(order.getValue(), addingDollar);
			}
		}
		for(Map.Entry<String, Set<String>> productions : productionMap.entrySet()){
			for(String followTerm : nonTermList){
				Set<String> rhs = productions.getValue();
				for(String rhsVar : rhs){
					if(rhsVar.contains(followTerm)){
						Integer followTermIndex = rhsVar.indexOf(followTerm);
						
						String reducedStr = rhsVar.substring(followTermIndex);
						
						
						String[] followSplitArr = reducedStr.split("\\s+");
						getTermsForFollow(followTerm,followSplitArr,productions.getKey());

					}
				}
			}
		}
		
		for(Map.Entry<String, Set<String>> productions : productionMap.entrySet()){
			for(String followTerm : nonTermList){
				Set<String> rhs = productions.getValue();
				for(String rhsVar : rhs){
					if(rhsVar.endsWith(followTerm)){

						Integer followTermIndex = rhsVar.lastIndexOf(followTerm);
							
						String reducedStr = rhsVar.substring(followTermIndex);
							
						String[] followSplitArr = reducedStr.split("\\s+");
						getNullableTermsForFollow(followTerm,followSplitArr,productions.getKey());
					}
				}
			}
		}
	}
	
	public static void getNullableTermsForFollow(String follTerm,String[] followedTerms,String prodKey){
		for(Map.Entry<Integer, String> order : productionOrder.entrySet()){
			if( order.getValue().equals(prodKey) && !follTerm.equals(prodKey)){
				if(tempNonTermFollowMap.containsKey(follTerm)){
					Set<String> existTemp = tempNonTermFollowMap.get(follTerm);
					tempNonTermFollowMap.remove(follTerm);
					existTemp.add(prodKey);
					tempNonTermFollowMap.put(follTerm, existTemp);
				}else{
					Set<String> newExistTemp = new HashSet<String>();
					newExistTemp.add(prodKey);
					tempNonTermFollowMap.put(follTerm, newExistTemp);
				}
			}
		}

		
	}
	
	public static void getTermsForFollow(String follTerm,String[] followedTerms,String prodKey){
		
//		for(String emp : emptyProductions){
//			if(emp.equals(follTerm)){
//				if(tempNonTermFollowMap.containsKey(follTerm)){
//					Set<String> existTemp = tempNonTermFollowMap.get(follTerm);
//					tempNonTermFollowMap.remove(follTerm);
//					existTemp.add(prodKey);
//					tempNonTermFollowMap.put(follTerm, existTemp);
//				}else{
//					Set<String> newExistTemp = new HashSet<String>();
//					newExistTemp.add(prodKey);
//					tempNonTermFollowMap.put(follTerm, newExistTemp);
//				}
//			}
//		}
		
		
		if(followedTerms.length > 1){
			
			
			if(followedTerms[0].contentEquals(follTerm)){
				if(!followedTerms[1].isEmpty() && !followedTerms[1].equals(" ")){
					checkFollowedTermVar(follTerm,followedTerms[1]);
				}
			}
			
			if(followedTerms.length > 2){
				
				if(followedTerms[1].contentEquals(follTerm)){
					if(!followedTerms[2].isEmpty() && !followedTerms[2].equals(" ")){
						checkFollowedTermVar(follTerm,followedTerms[2]);
					}
				}
				
				
			}

		}
	}
	
	
	public static void checkFollowedTermVar(String fol , String folldTerm){
		
		if(nonTermList.contains(folldTerm)){
			if(followMap.containsKey(fol)){
				
				Set<String> existingFollTerms = followMap.get(fol);
				followMap.remove(fol);
				Set<String> firstTerms = firstMap.get(folldTerm);
				if(firstTerms.contains("empty")){
					firstTerms.remove("empty");
				}
				existingFollTerms.addAll(firstTerms);
				followMap.put(fol, existingFollTerms);
			}else{
				Set<String> firstTerms = firstMap.get(folldTerm);
				if(firstTerms.contains("empty")){
					firstTerms.remove("empty");
				}
				followMap.put(fol, firstTerms);
			}
		}
		if(termList.contains(folldTerm) && !folldTerm.equals("empty")){
			if(followMap.containsKey(fol)){
				
				Set<String> existingFollTerms = followMap.get(fol);
				followMap.remove(fol);
				existingFollTerms.add(folldTerm);
				followMap.put(fol, existingFollTerms);
			}else{
				Set<String> firstTerms = new HashSet<String>();
				firstTerms.add(folldTerm);
				followMap.put(fol, firstTerms);
			}
		}

	}
	
	public static void getAllRemainingFollows(){
		if(!tempNonTermFollowMap.isEmpty()){
			for(Map.Entry<String, Set<String>> tempFoll : tempNonTermFollowMap.entrySet()){
				for(String follVar : tempFoll.getValue()){
					Set<String> finalFoll = followMap.get(follVar);
					if(finalFoll != null && finalFoll.size() > 0){
						if(followMap.containsKey(tempFoll.getKey())){
							Set<String> existTermFollFinal = followMap.get(tempFoll.getKey());
							if(existTermFollFinal != null && existTermFollFinal.size() > 0){
								followMap.remove(tempFoll.getKey());
								existTermFollFinal.addAll(finalFoll);
								followMap.put(tempFoll.getKey(), existTermFollFinal);
							}

						}else{
							followMap.put(tempFoll.getKey(), finalFoll);
						}

					}
					
				}
			}
		}
		
	}
	
} 