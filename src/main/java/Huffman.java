import com.google.common.collect.*;

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
            System.out.println(entry.toString());
        }
    }

    public static void getEncoding(HuffmanNode root, String s, Map<Character, String> map){
        if(root.isLeaf()){
            map.put(root.getCharacter(), s);
            System.out.println(root.getCharacter() + ":" + s);
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

    public static void main(String[] args) throws IOException {
        File file = new File("./USConstitution.txt");
        SortedMultiset characters = getValues(file);
        characters = characters.descendingMultiset(); //Reverse the set
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        Map<Character, String> characterMap = new HashMap<>();

        //Add nodes to priority queue
        populatePriorityQueue(characters, queue);
        HuffmanNode root = populateTree(queue);
        getEncoding(root, "", characterMap);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        int character;
        StringBuilder encodedText = new StringBuilder();

        while((character = reader.read()) != -1){
            encodedText.append(characterMap.get((char) character));
        }
        System.out.println(encodedText.toString());
        System.out.println(encodedText.length() + "Bytes");
        File compressedFile = new File("./USCompressditution.txt");
        BitSet bitCode = new BitSet(encodedText.toString().length());
        BitOutputStream outputStream = new BitOutputStream(new FileOutputStream(compressedFile));
        outputStream.write();
//        for(int i = 0; i < bitCode.length(); i++){
//            if(encodedText.charAt(i) == '1'){
//                bitCode.set(i);
//            }
//        }
//        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(compressedFile));
//        outputStream.writeObject(bitCode);
    }
}
