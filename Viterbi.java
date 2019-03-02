import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Viterbi {

	private final String initial = "S";
	private final List<String> states;
	private final Map<String, Map<String, Double>> transistionProb;
	private final Map<String, Map<String, Double>> emissionProb;
	
	//Default Constructor 	
	Viterbi(){
		this.states = new ArrayList<String>();
		this.transistionProb = new HashMap<>();
		this.emissionProb = new HashMap<>();
	}
	
	
	/*
	 *  Main function
	 */
	
	public static void main(String[] args) {
		if(args.length > 1 || args.length < 1) {
			System.out.println("Atleast one argument needed");
			return;
		}
		else {
			Viterbi hmm = initialSetup();
			String input = args[0];
			hmm.predict(input);			
		}
	}
	
	/*
	 *  Function to predict the result
	 */

	private void predict(String input) {
		char[] inputArray = input.toCharArray();
		List<String> result = new ArrayList<>();
		int index = -1;
		Map<Integer, Map<String, String>> backPointer = new HashMap<>();
		Map<Integer, Map<String, Double>> totalProb = new HashMap<>();
		totalProb.putIfAbsent(index, new HashMap<>());
		totalProb.get(index).put(initial, 1.0);
		
		for(char observation : inputArray) {
			index++;
			for(String state : states) {
				Map<String, Double> pathProbability = calculateTransitionProb(Character.toString(observation), state, totalProb, index);
				findStateWithMaxProb(pathProbability, index, backPointer, totalProb, state);
			}
		}
		
		//Get the final state
		Map<String, Double> finalStateProb = totalProb.get(index);
		String finalState = "";
		Double maxProb = -Double.MAX_VALUE;
		for(String key : finalStateProb.keySet()) {
			if(maxProb < finalStateProb.get(key)) {
				maxProb = finalStateProb.get(key);
				finalState = key;
			}
		}
		
		//Get the sequence of states from the back pointers
		while(index > -1) {
			result.add(finalState);
			finalState = backPointer.get(index).get(finalState);
			index--;
		}
		
		//Result List should be reverse as we want the states from beginning to end
		Collections.reverse(result);
		
		//Print the result
		System.out.println("Input   :  " + input);
		System.out.print("Output  :  ");
		for(String s : result) {
			System.out.print(s);
		}
		System.out.println();
		System.out.println("F - Fever, H - Healthy ");
	}
	
	/*
	 *  Function to calculate the state with max Probability among previous states
	 */
	private void findStateWithMaxProb(Map<String, Double> pathProbability, int index,
			Map<Integer, Map<String, String>> backPointer, Map<Integer, Map<String, Double>> totalProb, String state) {
			Double maxProb = -Double.MAX_VALUE;
			String maxProbState = "";
			for(String key : pathProbability.keySet()) {
				if(maxProb < pathProbability.get(key)) {
					maxProb = pathProbability.get(key);
					maxProbState = key;
				}
			}
			if(!totalProb.containsKey(index)) {
				totalProb.put(index, new HashMap<>());
			}
			totalProb.get(index).put(state, pathProbability.get(maxProbState));
			if(!backPointer.containsKey(index)) {
				backPointer.put(index, new HashMap<>());
			}
			backPointer.get(index).put(state, maxProbState);		
	}


	/*
	 *  Function to calculate transition probabilities from previous state to current state and emission probability of current state
	 */
	
	private Map<String, Double> calculateTransitionProb(String observation, String state, 
			Map<Integer, Map<String, Double>> prob, int index) {
		Map<String, Double> pathProbability = new HashMap<>();
		for(String prev : states) {
			pathProbability.put(prev, prob.getOrDefault(index - 1, new HashMap<>()).getOrDefault(prev, 0.0) * 
								transistionProb.get(prev).getOrDefault(state, 0.0)*
								emissionProb.getOrDefault(state, new HashMap<>()).getOrDefault(observation, 0.0));
		}
		return pathProbability;
	}

	/*
	 *  Function for initial set up configuration
	 */
	
	private static Viterbi initialSetup() {
		Viterbi hmm = new Viterbi();
		Map<String, Double> transMap = new HashMap<>();
		Map<String, Double> emmisionMap = new HashMap<>();
		transMap.put("H", 0.6);
		transMap.put("F", 0.4);
		//Add initial transitions from start state
		hmm.add(hmm.initial, transMap, new HashMap<>());
		
		transMap.clear();
		transMap.put("H", 0.7);
		transMap.put("F", 0.3);
		emmisionMap.put("N", 0.1);
		emmisionMap.put("C", 0.4);
		emmisionMap.put("D", 0.5);
		
		//Add all the transitions from state Healthy
		hmm.add("H", transMap, emmisionMap);
		
		transMap.clear();
		emmisionMap.clear();
		transMap.put("H", 0.5);
		transMap.put("F", 0.5);
		emmisionMap.put("N", 0.6);
		emmisionMap.put("C", 0.3);
		emmisionMap.put("D", 0.1);
		
		//Add all the transitions from state Healthy
		hmm.add("F", transMap, emmisionMap);
		
		return hmm;
		
	}

	/*
	 *  Function to make transition probability and emission probability matrix
	 */
	private void add(String state, Map<String, Double> transMap, Map<String, Double> emmisionMap) {
		states.add(state);
		
		//Transition probability matrix
		for(Map.Entry<String, Double> entry : transMap.entrySet()) {
			if(!transistionProb.containsKey(state)) {
				transistionProb.put(state, new HashMap<>());
			}
			transistionProb.get(state).put(entry.getKey(), entry.getValue());
		}
		
		//Emission probability matrix
		for(Map.Entry<String, Double> entry : emmisionMap.entrySet()) {
			if(!emissionProb.containsKey(state)) {
				emissionProb.put(state, new HashMap<>());
			}
			emissionProb.get(state).put(entry.getKey(), entry.getValue());
		}
		
	}

}
