package main.huffman;

/**
 * Created by Vostro-Daily on 12/3/2017.
 */

public class Node {

    public int frequency;
    public int symbol;
    public Node left, right;
    
    public Node(int symbol, int frequency){
    	this.symbol = symbol;
    	this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public Node(int symbol, int frequency, Node left, Node right) {
    	this.symbol = symbol;
    	this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}
