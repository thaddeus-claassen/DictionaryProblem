import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {
	
	public static int binarySearch(Word[] theArray, int start, int end, String theKey) {
	    int mid=(int)(start+end)/2;
	    if (end>=start) {
	      if (theArray[mid].getWordString().compareTo(theKey)>0) {
	        mid = binarySearch(theArray, start, mid-1,theKey);
	      } else if (theArray[mid].getWordString().compareTo(theKey)<0) {
	        mid = binarySearch(theArray, mid+1, end,theKey);
	      }// end if
	    } else {
	    	mid = -1;
	    }// end if-else
	    return mid; 
	  }//end of binarySearch() 
	
	  public static int linearSearch(Word[] theArray, int start, int end, String key) {
		int index = -1;
	    for (int i=start; i<=end; i++) {
	      if (theArray[i].getWordString().equals(key)) index = i;
	    }// end for
	    return index;
	  }// end linearSearch()
	  
	  public static int linearSearch(String[] theArray, int start, int end, String key) {
		  int index = -1;
		  for (int i=start; i<=end; i++) {
			  if (theArray[i].equals(key)) index = i;
		  }// end for
		  return index;
	  }// end linearSearch()
	  
	  public static String arrayToString(Word[] array) {
		  String string = "";
		  for (int i=0; i<array.length; i++) {
			  String word = array[i].getWordString();
			  string += word + "\n";
		  }// end for
		  return string;
	  }// end arrayToString()
	  
	  public static String arrayToString(Word[] array, int end) {
		  String string = "";
		  for (int i=0; i<end; i++) {
			  String word = array[i].getWordString();
			  string += word + "\n";
		  }// end for
		  return string;
	  }// end arrayToString()
	  
	  public static String arrayToString(Word[] array, int start, int end) {
		  String string = "";
		  for (int i=start; i<end; i++) {
			  String word = array[i].getWordString();
			  string += word + "\n";
		  }// end for
		  return string;
	  }// end arrayToString()
	  
	  public static String arrayToString(String[] array) {
		  String string = "";
		  for (int i=0; i<array.length; i++) {
			  string += array[i] + "\n";
		  }// end for
		  return string;
	  }// end arrayToString()
	  
	  public static String arrayToString(Word[] array, boolean includeDefinitions) {
		  String string;
		  if (includeDefinitions) {
			  string = "";
			  for (int i = 0; i < array.length; i++) {
				  string += array[i] + ": ";
				  String[] definitions = array[i].getDefList();
				  for (int j=0; j<definitions.length; j++) {
					  string += definitions[j] + " ";
				  }// end for
				  string += "\n";
			  }// end for
		  } else string = arrayToString(array);
		  return string;
	  }// end arrayToString()
	  
	  public static Word[] copyArrayListIntoArray(ArrayList<Word> arrayList) {
		  Word[] array = new Word[arrayList.size()];
		  for (int i=0; i<arrayList.size(); i++) {
			  array[i] = arrayList.get(i);
		  }// end for
		  return array;
	  }// end copyArryaListIntoArray()
	  
	  public static boolean commonElements(ArrayList<Word> arrayList, HashMap<Word, Integer> hashMap) {
		  boolean commonElements = false;
		  for (Object theObject: arrayList) {
			  if (hashMap.containsKey(theObject)) {
				  commonElements = true;
				  break;
			  }// end if
		  }// end for
		  return commonElements;
	  }// end commonElements()
	  
	  public static boolean containsDuplicates(ArrayList<Word> arrayList) {
		  boolean containsDuplicates = false;
		  for (int i=0; i<arrayList.size(); i++) {
			  Word firstWord = arrayList.get(i);
			  for (int j=i+1; j<arrayList.size(); j++) {
				  Word secondWord = arrayList.get(j);
				  if (firstWord.equals(secondWord)) {
					  containsDuplicates = true;
					  break;
				  }// end if
			  }// end for
		  }// end for
		  return containsDuplicates;
	  }// end containsDuplicates()
	  
	  public static int findSum(int[] numbers) {
		  int sum = 0;
		  for (int i=0; i<numbers.length; i++) {
			  sum += numbers[i];
		  }// end for
		  return sum;
	  }// end findSum
	  
	  public static int[] removeMax(int[] array) {
		  int indexOfMax = -1;
		  int max = Integer.MIN_VALUE;
		  for (int i=0; i<array.length; i++) {
			  if (max < array[i]) {
				  max = array[i];
				  indexOfMax = i;
			  }// end if
		  }// end for
		  int[] newArray = new int[array.length-1];
		  int oldIndex = 0;
		  int newIndex = 0;
		  while (oldIndex < array.length) {
			  if (oldIndex != indexOfMax) {
				  newArray[newIndex] = array[oldIndex];
			  }// end if
			  oldIndex++;
		  }// end while
		  return newArray;
	  }// end removeMax()
	  
}// end class