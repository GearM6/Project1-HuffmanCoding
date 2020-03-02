import com.google.common.collect.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

    public static SortedMultiset<Character> getValues(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        SortedMultiset<Character> multiset = TreeMultiset.create();
        int character = 0;
        while((character = reader.read()) != -1){
                multiset.add((char) character);
        }
        return multiset;
    }
    private static HuffmanNode populateTree(PriorityQueue<HuffmanNode> queue) {
        while(queue.size() > 1){
            HuffmanNode lowerNode = queue.poll();
            HuffmanNode higherNode = queue.poll();
            queue.add(new HuffmanNode(lowerNode, higherNode));
        }
        return queue.peek();
    }

    private static void populatePriorityQueue(SortedMultiset characters, PriorityQueue<HuffmanNode> queue) {
        while(characters.size() > 0){
            Multiset.Entry entry = characters.pollFirstEntry();
            queue.add(new HuffmanNode(entry.toString().charAt(0), entry.getCount()));
        }
    }

    public static void getEncoding(HuffmanNode root, String s, Map<Character, String> map){
        if(root.isLeaf()){
            map.put(root.getCharacter(), s);
            return;
        }
        else {
            if(root.getRight() != null){
                getEncoding(root.getLeft(), s+"0", map);
            }
            if(root.getLeft() != null){
                getEncoding(root.getRight(), s+"1", map);
            }
        }
    }

    public static void encodeFile(File file, Map<Character, String> characterMap) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int character;
        StringBuilder encodedText = new StringBuilder();

        while((character = reader.read()) != -1){
            encodedText.append(characterMap.get((char) character));
        }
        File compressedFile = new File("./USCompresseditution.txt");
        BitSet bitCode = new BitSet(encodedText.toString().length());

        for(int i = 0; i < bitCode.length(); i++){
            if(encodedText.charAt(i) == '1'){
                bitCode.set(i);
            }
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(compressedFile));
        outputStream.writeObject(bitCode);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("./USConstitution.txt");
        SortedMultiset characters = getValues(file);
        characters = characters.descendingMultiset(); //Reverse the set
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        Map<Character, String> characterMap = new HashMap<>();

        //Add nodes to priority queue
        populatePriorityQueue(characters, queue);
        System.out.println("Building tree.");
        long startTime = System.nanoTime();
        HuffmanNode root = populateTree(queue);
        long endTime = System.nanoTime();
        System.out.println("Time elapsed: " + (endTime - startTime));


        System.out.println("Encoding File.");
        startTime = System.nanoTime();
        getEncoding(root, "", characterMap);
        encodeFile(file, characterMap);
        endTime = System.nanoTime();
        System.out.println("Time elapsed: " + (endTime - startTime));

        return;
    }
}
