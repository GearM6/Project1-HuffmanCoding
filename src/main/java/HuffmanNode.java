public class HuffmanNode implements Comparable<HuffmanNode>{
    private int count;

    private char character;

    private HuffmanNode left;

    private HuffmanNode right;
    public HuffmanNode(char character, int value){
        this.character = character;
        this.count = value;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public boolean isLeaf(){
        return (this.left == null && this.right == null);
    }

    public HuffmanNode(HuffmanNode lowerNode, HuffmanNode higherNode){
        this.left = lowerNode;
        this.right = higherNode;
        this.count = lowerNode.count + higherNode.count;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public int compareTo(HuffmanNode node) {
        if(this.count >= node.count){
            return 1;
        }
        return -1;
    }

    public int getCount() {
        return count;
    }
}
