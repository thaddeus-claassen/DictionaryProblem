import java.util.ArrayList;

public class Word implements Comparable {

	public static final int CFD = 0, ALPHABET = 1;
	public int sortBy;
	private String wordString;
	private String[] definitionWord;
	private ArrayList <Word> flippedDefinition;
	int countFlippedDefinition;

	Word(String w, String def) {
		wordString=w;
		definitionWord = def.split(" ");
		flippedDefinition=new ArrayList<Word>();
		countFlippedDefinition=0;
		sortBy = 0;
	}// end constructor

	public String[] getDefList() {
		return definitionWord;
	}// end getDefList()

	public String getDefWord(int idx) {
		return definitionWord[idx];
	}// end getDefWord()

	public ArrayList<Word> getflippedDefinition() {
		return flippedDefinition;
	}// end getFlippedDefinition()

	public int getSizeDef() {
		return definitionWord.length;
	}// end getSizeDef()

	public String toString() {
		return wordString;
	}// end toString()
	
	public String toStringDef() {
		String string = wordString + " : ";
		for (int i = 0; i < definitionWord.length; i++) {
			string += definitionWord[i] + "\n";
		}// end for
		return string;
	}// end toStringDef()

	public void addWordFlipped(Word w) {
		flippedDefinition.add(w);
		countFlippedDefinition++;
	}// end addWordFlipped()

	public int getCFD() {
		return countFlippedDefinition;
	}// end getCFD()

	public String getWordString() {
		return wordString;
	}// end getWordName()

	public void setSortBy(int sort) {
		sortBy = sort;
	}// end
	
	public int compareTo(Object arg0) {
		int value;
		if (sortBy == CFD) {
			if (countFlippedDefinition < ((Word)arg0).getCFD()) {
				value = 1;
			} else if (countFlippedDefinition > ((Word)arg0).getCFD()) {
				value = -1;
			} else {
				value = 0;
			}// end if-else
		} else {
			if (wordString.compareTo(((Word)arg0).getWordString()) > 0) {
				value = 1;
			} else if (wordString.compareTo(((Word)arg0).getWordString()) < 0) {
				value = -1;
			} else {
				value = 0;
			}// end if-else
		}// end if
		return value;
	}// end compareTo()

}// end class
