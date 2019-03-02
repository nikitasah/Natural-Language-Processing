import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Homework2 {
	Map<String, Integer> bigramMap;
	Map<String, Integer> unigramMap;

	enum bigramModel{
		NO_SMOOTHING, ADD_ONE_SMOOTHING
	}

	public static void main(String[] args) throws FileNotFoundException{
		Homework2 hw2 = new Homework2();
		hw2.readAndProcessFile(args[0]);
		String sentence1 = args[1];
		String sentence2 = args[2];
		hw2.displayTables(sentence1, sentence2);
	}


	/* Function to calculate sentence probability */
	private double calculateSentenceProb(String sentence, bigramModel type) {
		sentence = sentence.replaceAll("\\.", "").trim().toLowerCase();
		double probSentence = 1.00;
		String token1 = "", token2 = "";
		double conditionalProb;
		StringTokenizer tokenList = new StringTokenizer(sentence);
		String bigramWord = "";
		int bigramCount, unigramCount;
		token1 = tokenList.nextToken();
		while(tokenList.hasMoreTokens()) {
			token2 = tokenList.nextToken();
			bigramWord = token1 + " " + token2;
			bigramCount = bigramMap.containsKey(bigramWord)  ? bigramMap.get(bigramWord) : 0;
			unigramCount = unigramMap.containsKey(token1)  ? unigramMap.get(token1) : 0;
			if(type == bigramModel.NO_SMOOTHING) {
				if(bigramCount != 0) {
					conditionalProb = (double)bigramCount/unigramCount;
					probSentence = probSentence * conditionalProb;
				}
			}
			else {
				conditionalProb = (double)(bigramCount + 1)/(unigramCount + unigramMap.size());
				probSentence = probSentence * conditionalProb;
			}
			token1 = token2;
		}
		return probSentence;
	}


	/* Function to compare sentence probability */
	private void compareSentenceProbability(String sentence1, String sentence2,  bigramModel type) {
		if(type == bigramModel.NO_SMOOTHING) {
			double prob1 = calculateSentenceProb(sentence1, type);
			double prob2 = calculateSentenceProb(sentence1, type);
			System.out.println("Probabilities of sentences in case when No Smoothing is used :");
			System.out.println("Probability of Sentence 1" + prob1);
			System.out.println("Probability of Sentence 2" + prob2);
			String result = prob1 > prob2 ? "Sentence 1 is preferred as it has high probability ":
				"Sentence 2 is preferred as it has high probability ";
			System.out.println(result);
		}
		else {
			double prob1 = calculateSentenceProb(sentence1, type);
			double prob2 = calculateSentenceProb(sentence1, type);
			System.out.println("Probabilities of sentences in case when Add One Smoothing is used :");
			System.out.println("Probability of Sentence 1" + prob1);
			System.out.println("Probability of Sentence 2" + prob2);
			String result = prob1 > prob2 ? "Sentence 1 is preferred as it has high probability ":
				"Sentence 2 is preferred as it has high probability ";
			System.out.println(result);
		}

	}

	/* Function to display probability of No Smoothing */
	private void displayProbabilityNoSmoothing(String sentence) {
		sentence = sentence.replaceAll("\\.", "").trim().toLowerCase();
		DecimalFormat df = new DecimalFormat("#.####");
		String[] tokens = sentence.split(" ");
		for(String t : tokens) {
			System.out.printf("%-20.20s", t);
		}
		System.out.println("\n");
		for(int i = 0; i < tokens.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				String bigram = tokens[i] + " " + tokens[j];
				if(bigramMap.containsKey(bigram)) {
					double probability = (double)bigramMap.get(bigram)/unigramMap.get(tokens[i]);
					System.out.printf("%-20.20s", df.format(probability));
				}
				else {
					System.out.printf("%-20.20s", "0");
				}
			}
			System.out.println("\n");
		}
		System.out.println("***************************************************************************************");
	}


	/* Function to display probability of One Smoothing */
	private void displayProbabilityOneSmoothing(String sentence) {
		sentence = sentence.replaceAll("\\.", "").trim().toLowerCase();
		DecimalFormat df = new DecimalFormat("#.###");
		String[] tokens = sentence.split(" ");
		for(String t : tokens) {
			System.out.printf("%-20.20s", t);
		}
		System.out.println("\n");
		for(int i = 0; i < tokens.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				String bigram = tokens[i] + " " + tokens[j];
				if(bigramMap.containsKey(bigram)) {
					double probability = (double)(bigramMap.get(bigram) + 1)/(unigramMap.get(tokens[i]) + unigramMap.size());
					System.out.printf("%-20.20s", df.format(probability));
				}
				else {
					System.out.printf("%-20.20s", "0");
				}
			}
			System.out.println("\n");
		}
		System.out.println("*******************************************************************************");
	}

	/* Function to display frequency of Count No Smoothing */
	private void displayBiCountNoSmoothing(String sentence) {
		sentence = sentence.replaceAll("\\.", "").trim().toLowerCase();
		String[] tokens = sentence.split(" ");
		for(String t : tokens) {
			System.out.printf("%-20.20s", t);
		}
		System.out.println("\n");
		for(int i = 0; i < tokens.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				String bigram = tokens[i] + " " + tokens[j];
				if(bigramMap.containsKey(bigram)) {
					System.out.printf("%-20.20s", bigramMap.get(bigram));
				}
				else {
					System.out.printf("%-20.20s", "0");
				}
			}
			System.out.println("\n");
		}
		System.out.println("************************************************************************************");
	}


	/* Function to display frequency of Count One Smoothing */
	private void displayBiCountOneSmoothing(String sentence) {
		sentence = sentence.replaceAll("\\.", "").trim().toLowerCase();
		String[] tokens = sentence.split(" ");
		for(String t : tokens) {
			System.out.printf("%-20.20s", t);
		}
		System.out.println("\n");
		for(int i = 0; i < tokens.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				String bigram = tokens[i] + " " + tokens[j];
				if(bigramMap.containsKey(bigram)) {
					System.out.printf("%-20.20s", bigramMap.get(bigram) + 1);
				}
				else {
					System.out.printf("%-20.20s", "0");
				}
			}
			System.out.println("\n");
		}
		System.out.println("***************************************************************************************");
	}

	/* Function to display probability and frequency count tables */
	private void displayTables(String sentence1, String sentence2) {
		System.out.println("Displaying Bigram count without smoothing :");
		displayBiCountNoSmoothing(sentence1);
		displayBiCountNoSmoothing(sentence2);
		System.out.println("Displaying Bigram count with add one smoothing :");
		displayBiCountOneSmoothing(sentence1);
		displayBiCountOneSmoothing(sentence2);
		System.out.println("Displaying probability without smoothing :");
		displayProbabilityNoSmoothing(sentence1);
		displayProbabilityNoSmoothing(sentence2);
		System.out.println("Displaying probability with add one smoothing :");
		displayProbabilityOneSmoothing(sentence1);
		displayProbabilityOneSmoothing(sentence2);
		compareSentenceProbability(sentence1, sentence2, bigramModel.NO_SMOOTHING);
		compareSentenceProbability(sentence1, sentence2, bigramModel.ADD_ONE_SMOOTHING);
	}

	/* Function to read and process the corpus.txt */
	private void readAndProcessFile(String file) {	
		BufferedReader reader;
		String corpus = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			bigramMap = new HashMap<String, Integer>();
			unigramMap = new HashMap<String, Integer>();
			String line = reader.readLine();
			String token1 = "";
			String token2 = "";
			String bigramWord = "";
			while(line != null) {
				corpus = corpus + " " + line;
				line = reader.readLine();
			}
			corpus = corpus.replaceAll("[^a-zA-Z0-9. ]", "");
			corpus = corpus.replaceAll("\\s+", " ").toLowerCase();
			StringTokenizer tokenList = new StringTokenizer(corpus);
			token1 = tokenList.nextToken();
			while(tokenList.hasMoreTokens()) {
				token2 = tokenList.nextToken();
				bigramWord = token1 + " " + token2;
				if(!bigramMap.containsKey(bigramWord)) {
					bigramMap.put(bigramWord, 1);
				}
				else {
					bigramMap.put(bigramWord, bigramMap.get(bigramWord) + 1);
				}
				if(!unigramMap.containsKey(token1)) {
					unigramMap.put(token1, 1);
				}
				else {
					unigramMap.put(token1, unigramMap.get(token1) + 1);
				}
				token1 = token2;
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("File not found");
		}
	}
}
