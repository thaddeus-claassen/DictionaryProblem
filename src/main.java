import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.util.*;

public class main {

	public static final int DICTIONARY_SIZE = 8, WORDHASH_SIZE = 10, NUM_ITERATIONS = 10, COMPARE_ARRAYS = 100, MAX_CYCLE_SIZE = 3;
	public Word[] dictionary;
	public ArrayList<HashMap<Word, Integer>> bestWordsFromCycles;

	public static void main(String[] args) {
		main main = new main(args);
	}//end of main

	public main(String args[]) {
		ArrayList<Word> wordList = null;
		dictionary = new Word[DICTIONARY_SIZE];
//		try {
//			wordList = readFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		dictionary = Utilities.copyArrayListIntoArray(wordList);
//		
//		System.out.println("Dictionary has " + dictionary.length + " words");
//		
//		bestWordsFromCycles = new ArrayList<HashMap<Word, Integer>>(100);
		
		createRandomDictionary(5);
		
		sortDictionaryAlphabetically();
			
		ArrayList<Word> potentialAxioms = createPotentialAxioms();
		System.out.println(potentialAxioms.toString());
		createFlippedDefinitions();	
		ArrayList<Cycle> cycles = getCycles(potentialAxioms);
		
		System.out.println("There are " + cycles.size() + " cycles");
		System.out.println(cycles.toString());
		
		ArrayList<Word> allWordsInCycles = getWordsInCycles(cycles);
		Cycle[] cycleArray = cycles.toArray(new Cycle[cycles.size()]); 
		HashMap<Word, Integer> wordHash = bruteForceCycles(allWordsInCycles, new HashMap<Word, Integer>(WORDHASH_SIZE), cycleArray, 0);
		
		System.out.println(wordHash);
		
//		ArrayList<Word> mustNonAxioms = findMustNonAxioms(potentialAxioms);
//
//		greedyWithCycleAndLocalSearch(potentialAxioms, wordHash, cycles);
//		greedyWithLocalSearch(potentialAxioms, wordHash);
//		
//		for (Word nonAxiom : mustNonAxioms) {
//			checkForNonAxiom(nonAxiom, wordHash);
//		}// end for
//		mustNonAxioms.clear();
//		
//		System.out.println("\n" + "Final hash has " + findNumAxioms(wordHash) + " axioms");
	}// end main
	
	public HashMap<Word, Integer> bruteForceCycles(ArrayList<Word> potentialAxioms, HashMap<Word, Integer> wordHash, Cycle[] cycles, int index) {
		HashMap<Word, Integer> bestAxioms = new HashMap<Word, Integer>(WORDHASH_SIZE);
		HashMap<Word, Integer> tempAxioms = new HashMap<Word, Integer>(WORDHASH_SIZE);
		tempAxioms.putAll(wordHash);
		System.out.println(index);
		if (index == cycles.length) {
			Cycle cycle;
			do {
				cycle = cycles[index];
				index++;
			} while (cycle.hasAnyWordInHash(wordHash));
			for (Word word : cycle.getWords()) {
				potentialAxioms.remove(word);
				tempAxioms.put(word, 0);
				tempAxioms = bruteForceCycles(potentialAxioms, tempAxioms, cycles, index + 1);
				if (bestAxioms.size() == 0) {
					bestAxioms.putAll(tempAxioms);
				} else {
					if (bestAxioms.size() > tempAxioms.size()) {
						bestAxioms.clear();
						bestAxioms.putAll(tempAxioms);
					}// end if
				}// end if-else
				tempAxioms.clear();
				potentialAxioms.add(word);
			}// end for
		} else {
			bestAxioms.putAll(wordHash);
		}
		System.out.println(bestAxioms);
		return bestAxioms;
	}// end bruteForceCycles
	
	public ArrayList<Word> getWordsInCycles(ArrayList<Cycle> cycles) {
		ArrayList<Word> words = new ArrayList<Word>();
		for (Cycle cycle : cycles) {
			Word[] cycleWords = cycle.getWords();
			for (Word word : cycleWords) {
				if (!words.contains(word)) {
					words.add(word);
				}// end if
			}// end for
		}// end for
		return words;
	}// end getWordsInCycles()
	
//	public void bruteForceCycleAxioms(ArrayList<Word> potentialAxioms, HashMap<Word, Integer> wordHash, ArrayList<Cycle> cycles) {
//		System.out.println(cycles);
//		if (cycles.size() > 0) {
//			Cycle nextCycle = cycles.remove(0);
//			for (int i = 0; i < nextCycle.size(); i++) {
//				Word newAxiom = nextCycle.get(i);
//				potentialAxioms.remove(newAxiom);
//				wordHash.put(newAxiom, 0);
//				ArrayList<Cycle> oldCycles = removeCycles(wordHash, cycles);
//				bruteForceCycleAxioms(potentialAxioms, wordHash, cycles);
//				cycles.addAll(oldCycles);
//				wordHash.remove(newAxiom);
//				potentialAxioms.add(newAxiom);
//			}// end for
//			cycles.add(0, nextCycle);
//		} else {
//			if (bestWordsFromCycles.size() > 0) {
//				int size = bestWordsFromCycles.get(0).size();
//				if (wordHash.size() <= size && !bestWordsFromCycles.contains(wordHash)) {
//					System.out.println("Size is " + size);
//					HashMap<Word, Integer> copy = new HashMap<Word, Integer>(WORDHASH_SIZE);
//					if (wordHash.size() < size) {
//						bestWordsFromCycles.clear();
//					}// end if
//					bestWordsFromCycles.add(copy);
//				}// end if
//			} else {
//				HashMap<Word, Integer> copy = new HashMap<Word, Integer>(WORDHASH_SIZE);
//				bestWordsFromCycles.add(copy);
//			}// end if-else
//		}// end if-else
//	}// end bruteForceCycleAxioms
//	
//	public ArrayList<Cycle> removeCycles(HashMap<Word, Integer> wordHash, ArrayList<Cycle> cycles) {
//		ArrayList<Cycle> oldCycles = new ArrayList<Cycle>();
//		Iterator<Cycle> cycleIterator = cycles.iterator();
//		while (cycleIterator.hasNext()) {
//			Cycle nextCycle = cycleIterator.next();
//			for (int i = 0; i < nextCycle.size(); i++) {
//				Word nextWord = nextCycle.get(i);
//				if (wordHash.containsKey(nextWord)) {
//					cycleIterator.remove();
//					break;
//				}// end if
//			}// end for
//		}// end while
//		return oldCycles;
//	}// end cleanCycles()
	
	public void greedyWithCycleAndLocalSearch(ArrayList<Word> potentialAxioms, HashMap<Word,Integer> wordHash, ArrayList<Cycle> cycleList) {
		int[] bestValues = initializeBestValues();
		HashMap<Word, Integer> initialHash = new HashMap<Word, Integer> (WORDHASH_SIZE);
		ArrayList<Word> initialWords = new ArrayList<Word>();
		initialHash.putAll(wordHash);
		initialWords.addAll(potentialAxioms);
		HashMap<Word, Integer> tempHash = new HashMap<Word, Integer>(WORDHASH_SIZE);
		ArrayList<Word> tempAxioms = new ArrayList<Word>();
		for (int i = 0; i < NUM_ITERATIONS; i++) {
			System.out.println("Cycle is at " + i);
			tempHash.putAll(initialHash);
			tempAxioms.addAll(initialWords);
			Iterator<Cycle> cycleIterator = cycleList.iterator();
			while (cycleIterator.hasNext()) {
				Cycle nextCycle = cycleIterator.next();
				double random = Math.random();
				if (random > i/NUM_ITERATIONS) {
					chooseBestAxiom(nextCycle, tempAxioms, tempHash);
				} else {
					chooseRandomAxiom(nextCycle, tempAxioms, tempHash);
				}// end if-else
			}// end while
			int[] temp = getIntegerArray(tempHash);
			if (compareIntArrays(bestValues, temp) == -1) {
				bestValues = temp;
				wordHash.clear();
				potentialAxioms.clear();
				wordHash.putAll(tempHash);
				potentialAxioms.addAll(tempAxioms);
			}// end if
			tempHash.clear();
			tempAxioms.clear();
		}// end for
	}// end greedyWithCycleAndLocalSearch()
	
	public void chooseBestAxiom(Cycle nextCycle, ArrayList<Word> tempAxioms, HashMap<Word, Integer> tempHash) {
		int maxCFD = -1;
		int indexForMax = -1;
		for (int i = 0; i < nextCycle.size(); i++) {
			Word nextWord = nextCycle.get(i);
			if (tempAxioms.contains(nextWord)) {
				int nextCFD = nextWord.getCFD();
				if (nextCFD > maxCFD) {
					maxCFD = nextCFD;
					indexForMax = i;
				}// end if
				if (i == nextCycle.size()-1) {
					Word theWord = nextCycle.get(indexForMax);
					tempAxioms.remove(theWord);
					tempHash.put(theWord,0);
				}// end if
			} else {
				break;
			}// end if-else
		}// end for
	}// end chooseBestAxiom()
	
	public void chooseRandomAxiom(Cycle nextCycle, ArrayList<Word> tempAxioms, HashMap<Word, Integer> tempHash) {
		boolean hasAxiom = false;
		for (int i = 0; i < nextCycle.size() && !hasAxiom; i++) {
			Word nextWord = nextCycle.get(i);
			if (tempAxioms.contains(nextWord)) {
				if (i == nextCycle.size()) {
					int index = (int)(Math.random()*nextCycle.size());
					Word theWord = nextCycle.get(index);
					tempHash.put(theWord,0);
				}// end if
			} else {
				break;
			}// end if-else
		}// end for
	}// end chooseRandomAxiom()
	
	public void greedyWithLocalSearch(ArrayList<Word> potentialAxioms, HashMap<Word,Integer> wordHash) {
		HashMap<Word,Integer> tempHash = new HashMap<Word,Integer>(WORDHASH_SIZE);
		ArrayList<Word> initialAxioms = new ArrayList<Word>();
		HashMap<Word, Integer> initialHash = new HashMap<Word, Integer>(wordHash.size());
		initialAxioms.addAll(potentialAxioms);
		initialHash.putAll(wordHash);
		wordHash.clear();
		potentialAxioms.clear();
		int[] bestValues = initializeBestValues();
		for (int i = 0; i < NUM_ITERATIONS; i++) {
			if (i%1 == 0) {
				System.out.println("i is " + i);
			}
			potentialAxioms.addAll(initialAxioms);
			tempHash.putAll(initialHash);
			swapPotentialAxioms(potentialAxioms, i);
			basicGreedy(potentialAxioms, tempHash);
			int[] temp = getIntegerArray(tempHash);
			if (compareIntArrays(bestValues, temp) == -1) {
				bestValues = temp;
				wordHash.clear();
				wordHash.putAll(tempHash);
			}// end if
			tempHash.clear();
		}// end for
	}// end greedyWithLocalSearch()
	
	public void basicGreedy(ArrayList<Word> wordList, HashMap<Word,Integer> wordHash) {
		while (wordList.size() > 0) {
			Word theWord = wordList.remove(0);
			wordHash.put(theWord, 0);
			Iterator<Word> wordIterator = wordList.iterator();
			while (wordIterator.hasNext()) {
				Word nextWord = wordIterator.next();
				checkForNonAxiom(nextWord, wordHash);
				if (wordHash.containsKey(nextWord)) {
					wordIterator.remove();
				}// end if
			}// end for
		}// end while
	}// end basicGreedy()
	
	public void checkForNonAxiom(Word newWord, HashMap<Word, Integer> wordHash) {
		String[] defList = newWord.getDefList();
		boolean isAxiom = false;
		int maxLevel = 0;
		for (int j = 0; j < defList.length && !isAxiom; j++) {
			int defIndex = Utilities.binarySearch(dictionary, 0, dictionary.length-1, defList[j]);
			if (defIndex != -1) {
				Word currDef = dictionary[defIndex];
				if (wordHash.containsKey(currDef)) {
					int key = wordHash.get(currDef);
					maxLevel = Math.max(maxLevel, key);
					if (j == defList.length-1) {
						wordHash.put(newWord,maxLevel+1);
					}// end if
				} else {
					break;
				}// end if-else
			}// end if;
		}// end for
	}// end checkForNonAxiom()
	
	public int findNumAxioms(HashMap<Word,Integer> wordHash) {
		int numAxioms = 0;
		for (int i=0; i<DICTIONARY_SIZE; i++) {
			int value = -1;
			if (wordHash.containsKey(dictionary[i])) {
				value = wordHash.get(dictionary[i]);
				if (value == 0) {
					numAxioms++;
				}// end if
			}// end if
		}// end for
		return numAxioms;
	}// end findNumPotentialAxioms()
	
	public void findMustAxioms(ArrayList<Cycle> cycles, ArrayList<Word> potentialAxioms, HashMap<Word,Integer> wordHash) {
		Iterator<Cycle> cycleIterator = cycles.iterator();
		while (cycleIterator.hasNext()) {
			Cycle nextCycle = cycleIterator.next();
			if (nextCycle.size() == 1) {
				wordHash.put(nextCycle.get(0),0);
				potentialAxioms.remove(nextCycle.get(0));
				cycleIterator.remove();
			} else break;
		}// end while()
	}// end cycles()

	public void sortDictionaryAlphabetically() {
		for (int i=0; i<dictionary.length; i++) {
			dictionary[i].setSortBy(Word.ALPHABET);
		}// end for
		Arrays.sort(dictionary);
	}// end sortWordsByAlphabet()

	public void sortWordsByCFD(ArrayList<Word> words) {
		for (Word theWord : words) {
			theWord.setSortBy(Word.CFD);
		}// end for
		Collections.sort(words);
	}// end sortWordsByCFD()

	public void createFlippedDefinitions() {
		for (int j=0; j<dictionary.length; j++) {
			Word currWord=dictionary[j];
			for (int k=0; k<currWord.getDefList().length; k++) {
				String currDef=currWord.getDefWord(k);
				int bSResult = Utilities.binarySearch(dictionary,0,dictionary.length-1, currDef);
				if (bSResult > -1) dictionary[bSResult].addWordFlipped(currWord);
				else {
					System.out.println("Bad word is " + currDef);
				}// end if-else
			}// end for
		}// end for
	}// end createFlippedDefinitions()

	public ArrayList<Word> createPotentialAxioms() {
		ArrayList<Word> potentialAxiomList = new ArrayList<Word>();
		for (int i=0; i<dictionary.length;i++) {
			potentialAxiomList.add(dictionary[i]);
		}// end for
		sortWordsByCFD(potentialAxiomList);
		return potentialAxiomList;
	}// end createPotentialAxioms()
	
	public void swapPotentialAxioms(ArrayList<Word> potentialAxioms, double iteration) {
		for (int i=0; i<potentialAxioms.size(); i++) {
			double random = Math.random()*2;
			if (random < iteration / NUM_ITERATIONS) {
				Collections.swap(potentialAxioms, i, (int) (Math.random() * potentialAxioms.size()));
			}// end if
		}// end for
	}// end alterPotentialAxioms()

	public ArrayList<Word> findMustNonAxioms(ArrayList<Word> potentialAxiomList) {
		ArrayList<Word> mustNonAxioms = new ArrayList<Word>();
		Iterator<Word> axiomIterator = potentialAxiomList.iterator();
		while (axiomIterator.hasNext()) {
			Word theWord = axiomIterator.next();
			if (theWord.getCFD() == 0) {
				mustNonAxioms.add(theWord);
				axiomIterator.remove();
			}// end if
		}// end while
		return mustNonAxioms;
	}// end createMustNonAxioms()

	public ArrayList<Cycle> getCycles(ArrayList<Word> potentialAxioms) {
		ArrayList<Cycle> cycles = new ArrayList<Cycle>(); 
		for (int i=0; i<dictionary.length; i++) {
			ArrayList<Word> startingList = new ArrayList<Word>();
			startingList.add(dictionary[i]);
			ArrayList<Cycle> newCycles = depthFirstSearch(startingList, MAX_CYCLE_SIZE);
			cycles.addAll(newCycles);
			startingList.clear();
		}// end for
		Collections.sort(cycles);
		return cycles;
	}// end getCycles()
	
	public ArrayList<Cycle> checkForCycle(ArrayList<Word> currWords, int depth) {
		ArrayList<Cycle> cycleList = new ArrayList<Cycle>();
		if (currWords.get(0).getWordString().equals(currWords.get(currWords.size()-1).getWordString())) {
			Word[] wordArrayForCycle = new Word[currWords.size()-1];
			for (int i=0; i<wordArrayForCycle.length; i++) {
				wordArrayForCycle[i] = currWords.get(i);
			}// end for
			Cycle newCycle = new Cycle(wordArrayForCycle);
			cycleList.add(newCycle);
		} else if (depth > 0) {
			cycleList.addAll(depthFirstSearch(currWords, depth));
		}// end if
		return cycleList;
	}// end checkForCycle()
	
	public ArrayList<Cycle> depthFirstSearch(ArrayList<Word> currWords, int depth) {
		ArrayList<Cycle> cycleList = new ArrayList<Cycle>();
		String[] defList = currWords.get(currWords.size()-1).getDefList();
		for (int i=0; i<defList.length; i++) {
			String nextWordString = defList[i];
			int nextWordIndex = Utilities.binarySearch(dictionary, 0, dictionary.length-1, nextWordString);
			Word nextWord = dictionary[nextWordIndex];
			currWords.add(nextWord);
			cycleList.addAll(checkForCycle(currWords, depth-1));
			currWords.remove(currWords.size()-1);
		}// end for
		return cycleList;
	}// end depthFirstSearch()
	
	public ArrayList<Word> readFile() throws IOException {
		ArrayList<Word> wordList=new ArrayList<Word>();
		try {
			BufferedReader bf=new BufferedReader(new FileReader(new File ("DictionaryAugFile.txt")));
			System.out.println("File open successful");
			String line;
			while ((line = bf.readLine()) != null) {
				if (Math.random() > .9999) {
					System.out.println("Checking next word");
				}
				String[] lineSplit = line.split(":");
				boolean hasWord = false;
				for (Word theWord : wordList) {
					if (theWord.getWordString() == lineSplit[0]) {
						hasWord = true;
						break;
					}// end if
				}// end for
				if (!hasWord) {
					Word word = new Word(lineSplit[0], lineSplit[1]);
					wordList.add(word);
				}// end if
			}// end while
			System.out.println("ArrayList Filled Successfully");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}// end try-catch
		return wordList;
	}// end readFile()

	public void createRandomDictionary(int wordLength) {
		String[] stringDictionary = new String[DICTIONARY_SIZE];
		Random random = new Random();
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for (int i=0; i<DICTIONARY_SIZE; i++) {
			String newWord = "";
			for (int j=0; j<wordLength; j++) {
				newWord += alphabet.charAt(random.nextInt(26));
			}// end for
			stringDictionary[i] = newWord;
		}// end for
		for (int i=0; i<DICTIONARY_SIZE; i++) {
			String def = "";
			for (int j=0; j<((int)(Math.random()*10))+1; j++) {
				def += stringDictionary[(int) (Math.random()*DICTIONARY_SIZE)];
				def += " ";
			}// end for
			dictionary[i] = new Word(stringDictionary[i],def);
		}// end for
	}// end createRandomDictionary()
	
	public int[] getIntegerArray(HashMap<Word, Integer> tempHash) {
		int[] tempArray = new int[COMPARE_ARRAYS];
		for (int i = 0; i < dictionary.length; i++) {
			Word nextWord = dictionary[i];
			if (tempHash.get(nextWord) != null) {
				int value = tempHash.get(nextWord);
				tempArray[value]++;
			}// end if
		}// end for
		return tempArray;
	}// end findNumWords()
	
	public int compareIntArrays(int[] array1, int[] array2) {
		int value = 0;//Value of 1 means array1 is better, value of -1 means array2 is better, value of 0 means they are the same
		for (int i = 0; i < Math.max(array1.length, array2.length); i++) {
			if (array1[i] != array2[i]) {
				if (array1[i] > array2[i]) {
					value = -1;
				} else {
					value = 1;
				}// end if-else
				break;
			}// end if
		}// end for
		if (array1.length != array2.length) {
			System.out.println("Arrays are not the same length");
			Word w = dictionary[-1];
		}// end if
		return value;
	}// end compareIntArrays()
	
	public int[] initializeBestValues() {
		int[] bestValues = new int[COMPARE_ARRAYS];
		for (int i = 0; i < bestValues.length; i++) {
			bestValues[i] = Integer.MAX_VALUE;
		}// end for
		return bestValues;
	}// end initializeBestValues()
	
}// end class