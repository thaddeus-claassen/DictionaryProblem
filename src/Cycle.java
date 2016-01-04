import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Cycle implements Comparable {

	Word[] words;
	
	Cycle(Word[] words) {
		this.words = words;
	}// end constructor
	
	public int compareTo(Object arg0) {
		int value;
		if (words.length < ((Cycle)arg0).size()) {
			value = -1;
		} else if (words.length > ((Cycle)arg0).size()) {
			value = 1;
		} else {
			int CFDForThisCycle = 0;
			for (int i=0; i<words.length; i++) {
				CFDForThisCycle += words[i].getCFD();
			}// end for
			int CFDForOtherCycle = 0;
			Word[] otherWords = ((Cycle)arg0).getWords();
			for (int i=0; i<otherWords.length; i++) {
				CFDForOtherCycle += otherWords[i].getCFD();
			}// end for
			if (CFDForThisCycle < CFDForOtherCycle) {
				value = -1;
			} else if (CFDForThisCycle > CFDForOtherCycle) {
				value = 1;
			} else {
				value = 0;
			}// end if-else
		}// end if-else
		return value;
	}// end compareTo()
	
	public Word[] getWords() {
		return words;
	}// end getWords()
	
	public Word get(int index) {
		return words[index]; 
	}// end get()
	
	public int size() {
		return words.length;
	}// end getCycleSize()
	
	public int[] getCFDArray() {
		int[] CFDArray = new int[words.length];
		for (int i=0; i<words.length; i++) {
			CFDArray[i] = words[i].getCFD();
		}// end for
		return CFDArray;
	}// end getCFDArray()
	
	public boolean hasWord(Word word) {
		boolean hasWord = false;
		for (int i = 0; i < words.length; i++) {
			Word nextWord = words[i];
			if (word.equals(nextWord)) {
				hasWord = true;
				break;
			}// end if
		}// end for
		return hasWord;
	}// end hasWord()
	
	public boolean hasAnyWordInHash(HashMap<Word, Integer> words) {
		boolean hasWord = false;
		for (Word word : words.keySet()) {
			if (hasWord(word)) {
				hasWord = true;
				break;
			}// end if
		}// end for
		return hasWord;
	}// end haveAnyWordInHash()
	
	public Word getMaxWord() {
		Word theWord = null;
		int min = words.length;
		for (int i = 0; i < words.length; i++) {
			Word nextWord = words[i];
			if (min > nextWord.getCFD()) {
				min = nextWord.getCFD();
				theWord = nextWord;
			}// end if
		}// end for
		return theWord;
	}// end getMaxWord()
	
	public String toString() {
		String string = "";
		for (int i=0; i<words.length; i++) {
			string += words[i].getWordString();
			string += " ";
		}// end for
		return string;
	}// end toString()
	
}// end class
