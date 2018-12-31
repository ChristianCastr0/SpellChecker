import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SpellChecker {
    
	public static void main(String[] args) throws IOException {
		RedBlackTree<String> dictionary=new RedBlackTree<String>();
		long startTime, endTime;
		
		//Add words to Tree to create dictionary
		BufferedReader list=new BufferedReader(new FileReader("Dictionary.txt"));
		String word;
		startTime=System.currentTimeMillis();	//Start time
		while((word=list.readLine())!=null) {	//Add each from file into tree
			dictionary.addNode(word);
		}
		endTime=System.currentTimeMillis();	//End time
		System.out.print("Time to create dictionary: "+(endTime-startTime)+"ms\n");
		list.close();
		
		//Check poem for spelling errors
		BufferedReader poem=new BufferedReader(new FileReader("Poem.txt"));
		String line;
		String mispelledWords="";
		startTime=System.currentTimeMillis();
		while((line=poem.readLine())!=null) {
			String[] wordArray = line.toLowerCase().replaceAll("[^a-zA-Z ]", "").split(" ");	//Remove any punctuation and split words into an array
			//For each word on the line if something is mispelled add word to list of mispelled words
			for(String s:wordArray) {
				if(dictionary.lookup(s.trim())==null&&!s.equals("")) {
					mispelledWords=mispelledWords+s+", ";
				}
			}
		}
		endTime=System.currentTimeMillis();
		
		System.out.println("Words that were mispelled: {"+mispelledWords+"}");
		System.out.print("Time to spell check poem: "+(endTime-startTime)+"ms\n");
		poem.close();
	}
}
