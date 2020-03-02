import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class MultithreadedHuffman {

    static class FrequencyThread extends Thread {
        volatile private SortedMultiset<Character> multiset;
        BufferedReader bufferedReader;

        public FrequencyThread(SortedMultiset<Character> multiset, BufferedReader bufferedReader) {
            this.multiset = multiset;
            this.bufferedReader = bufferedReader;
        }

        @Override
        public void run() {
            int character = 0;
            while(true){
                try {
                    if ((character = bufferedReader.read()) == -1) break;
                    synchronized (multiset){
                        multiset.add((char) character);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SortedMultiset<Character> multiset = TreeMultiset.create();
        System.out.println("Enter number of threads.");
        Scanner in = new Scanner(System.in);
        int numThreads = in.nextInt();
        File file = new File("./USConstitution.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<FrequencyThread> threadList = new ArrayList<>();
        for(int i = 0; i < numThreads; i++){
            threadList.add(new FrequencyThread(multiset, bufferedReader));
        }
        long starttime = System.nanoTime();
        for(FrequencyThread thread: threadList){
            thread.start();
        }
        for(FrequencyThread thread: threadList){
            thread.join();
        }
        long endtime = System.nanoTime();
        System.out.println(endtime - starttime);
        multiset = multiset.descendingMultiset();
        List<Thread> newThreads = new ArrayList<>();
        PriorityQueue<HuffmanNode> queue = new PriorityQueue();
        System.out.println("Creating PQ");
        for(int i = 0; i < numThreads; i++){
            SortedMultiset<Character> finalMultiset = multiset;
            newThreads.add(new Thread(() -> {
                synchronized (queue){
                    while(!finalMultiset.isEmpty()){
                        Multiset.Entry entry = finalMultiset.pollFirstEntry();
                        queue.add(new HuffmanNode(entry.toString().charAt(0), entry.getCount()));
                    }
                }
            }));
        }
        starttime = System.nanoTime();
        for(Thread thread : newThreads){
            thread.start();
        }
        for(Thread thread : newThreads){
            thread.join();
        }
        endtime = System.nanoTime();
        System.out.println(endtime - starttime);
        newThreads.clear();
        System.out.println("Building Tree");
        for(int i = 0; i < numThreads; i++){
            newThreads.add(new Thread(() -> {
                synchronized (queue){
                    while(queue.size() >1 ){
                        HuffmanNode lowerNode = queue.poll();
                        HuffmanNode higherNode = queue.poll();
                        queue.add(new HuffmanNode(lowerNode, higherNode));
                    }
                }
            }) );
        }
    starttime = System.nanoTime();
    for(Thread thread : newThreads){
        thread.start();
    }
    for(Thread thread : newThreads){
        thread.join();
    }
    HuffmanNode root = queue.peek();
    endtime = System.nanoTime();
    System.out.println(endtime - starttime);

    Map<Character, String> encodingMap = new HashMap<>();
    Huffman.getEncoding(root, "", encodingMap);
    starttime = System.nanoTime();
    System.out.println("Encoding File.");
    Huffman.encodeFile(file, encodingMap);
    endtime = System.nanoTime();
    System.out.println(endtime - starttime);

    }
}
