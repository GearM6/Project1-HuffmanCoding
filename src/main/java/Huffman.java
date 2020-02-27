import com.google.common.collect.*;

import java.io.*;
import java.util.Arrays;

public class Huffman {
    public static SortedMultiset<Character> getValues(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        SortedMultiset<Character> multiset = TreeMultiset.create();
        int character;
        while((character = reader.read()) != -1){
                multiset.add((char) character);
        }
        return multiset;
    }
    public static void main(String[] args) throws IOException {
        SortedMultiset characters = getValues(new File("main/resources/USConstitution.txt"));
        System.out.println(Arrays.toString(characters.toArray()));
    }
}
