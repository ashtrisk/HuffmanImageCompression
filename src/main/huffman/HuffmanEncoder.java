package main.huffman;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import main.Helper;

/**
 * Created by Vostro-Daily on 12/3/2017.
 * 
 * Steps :
 * 1. Read the file in byte array
 * 2. Convert each byte to int[0-255], build a frequency table for int-symbols. (we can alternately work with bytes too)
 * 3. Build Nodes list of size 256, since there will only be 256 different symbols viz [0,255]
 * 4. Sort Nodes list from least frequency to most frequent symbol.
 * 5. Build Huffman tree using these ordered nodes (adding internal nodes wherever required).
 * 6. Traverse huffman tree & assign binary codes to each leaf node (containg data)
 * 7. Build a bit-string for whole raw data using the generated code.
 * 8. Convert huffman tree & the symbol sequence to bit string
 * 9. The resulted bit-string will be of format
 * 		[1 byte - size of symbols in bytes][N bytes - for symbols sequenced as in huffman tree]
 * 		[1 byte - size of huffman tree][N bytes - for huffman tree stored in pre-order]
 * 		[Rest bytes - encoded image]
 * Note : Last step is skipped for now.
 */

public class HuffmanEncoder {

    public static final String TAG = HuffmanEncoder.class.getSimpleName();
    
    /* The different bytes converts to integers b/w 0-255 i.e. 256 diff symbols 
	 * Also, the frequency at nth index represent the frequency of symbol 'n' */
	private int [] frequencies = new int[256];
	
	/* contains the ints read from file in range [0-255] */
	private int [] rawData;
	
	/* List of all data or symbol nodes */
	private List<Node> nodeList;
	
//	private BitSet [] codes = new BitSet[256];
	/* String codes for each unique symbol. 
	 * Note : here the index represent the symbol 
	 * & the value represent the binary string code for the symbol.
	 * This is done for faster access of code */
	private String [] codes = new String[256];

	/* Byte Map that converts the binary string code to equivalent byte. 
	 * This map contains all the possible arrangements for a byte pattern of bits. */
	private HashMap <String, Byte> encodingByteMap = new HashMap<>();
	
	/* The root node of the huffman tree */
	private Node huffmanTreeRoot;
	
	public void encode(String inputFile, String outputFile){
		
		Helper.print("---- Encoding ----\n");
		
		/* read byte array from the input image/binary file */
		byte [] bytes = readBytes(inputFile);
		
		/* build frequency chart for each symbol. Also save all the unique symbols */
		buildFrequencyChart(bytes);
		
		buildNodeList();
		
		huffmanTreeRoot = buildHuffmanTree(nodeList);

		/* Here, the index represent the symbol [0-255] & its value represent the code.
		 *  */
		buildCodes(huffmanTreeRoot, new StringBuilder());
		
		String encodedStr = buildEncodedStr();
		
		// Making string multiple of 8 by adding truncating 0's
		int remainder = encodedStr.length() % 8;
	    for(int cnt = 0;cnt < (8 - remainder);cnt++){
	    	encodedStr += "0";
	    }
		
	    /* Build map to convert string to equivalent bits */
	    buildEncodingByteMap();
	    
	    List<Byte> binaryEncodedData = new ArrayList<>();
	    
		for(int i = 0; i < encodedStr.length(); i += 8){
			
			String strBits  = encodedStr.substring(i, i + 8);
			binaryEncodedData.add(encodingByteMap.get(strBits));
		}
        
		writeToFile(binaryEncodedData, outputFile);
	}

	/* reads the input file to bytes */
	private byte[] readBytes(String inputFile) {
		File f = null;
		InputStream is = null;
		byte[] bytes = null;
		
		try {
//			bytes = Files.readAllBytes(Paths.get(fileName));
			f = new File(inputFile);
			bytes = new byte[(int)f.length()];
			is = new FileInputStream(f);
			
			int size;
			while((size = is.read(bytes)) != -1){
				Helper.print("Original file size : " + (size / 1024) + "KB \n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}

	private void buildFrequencyChart(byte[] bytes) {
		
		rawData = new int[bytes.length];
		
		for(int i = 0; i < bytes.length; i++){
			/* convert the signed byte to int between [0-255] */
			int symbol = bytes[i] & 0xFF;
			/* Note : symbol also represent the index of itself */
			frequencies[symbol]++;
			rawData[i] = symbol;
		}
		
	}

	private void buildNodeList() {
		nodeList = new ArrayList<>(frequencies.length);
		
		for(int i = 0; i < frequencies.length; i++){
			/* index is the symbol */
			int symbol = i;
			nodeList.add(new Node(symbol, frequencies[i]));
		}
	}

	/* Builds the huffman tree using the input Nodes list.
	 * Sort the input nodes first based on frequency. 
	 * Taking the least frequent symbols create internal nodes, adding these internal nodes in the node list,
	 * same process is iteratively executed till there remains a single node in the list which is the root node.
	 * Using this, the most frequent symbol gets the smallest huffman code & vice-versa. */
    private Node buildHuffmanTree(List<Node> inputNodes) {
        sortNodes(inputNodes);
        
        int size = inputNodes.size();

        // continue till there are more than 1 item in the list
        while(size > 1){
            // Removing the first two nodes, i.e. nodes with least frequencies.
            Node n1 = inputNodes.remove(0);
            Node n2 = inputNodes.remove(0);

            // Internal node, parent of n1 & n2 with added up frequency. Internal nodes have data '-1'
            Node n3 = new Node(-1, n1.frequency + n2.frequency, n1, n2);

            addNode(n3, inputNodes);

            size = inputNodes.size();
        }

        return inputNodes.get(0);
    }
    
    /* Traverses the huffman code and assigns binary codes to each symbol till all the leaf nodes are traversed. */
    private void buildCodes(Node node, StringBuilder strCode) {
    	if(node == null){
    		return;
    	}
    	
    	if(isLeafNode(node)){
    		codes[node.symbol] = strCode.toString();
    		
    		Helper.print("symbol = " + node.symbol + " : freq: " + node.frequency + " : " + strCode.toString() + "\n");
    	} else {
    		/* Move left of tree - set bit value = 0 */
    		buildCodes(node.left, new StringBuilder(strCode).append("0"));
    		
    		/* Move right of tree set bit value = 1 */
    		buildCodes(node.right, new StringBuilder(strCode).append("1"));
    	}
    }

    /* Builds the binary encoded string by replacing each integer in the raw data with its equivalent huffman code. */
    private String buildEncodedStr() {
		StringBuilder encodedStr = new StringBuilder();
		for(int i = 0; i < rawData.length; i++){
			encodedStr.append(codes[rawData[i]]);
		}
		
		Helper.print("Size : " + (encodedStr.length() / 1024 / 8)  + "KB");
		
		return encodedStr.toString();
	}

    /* Adds node in the list keeping the list sorted.
     * Note : It assumes the list is already sorted. */
    private void addNode(Node node, List<Node> nodeList) {
        int idxWhereAdd = nodeList.size();
        for(int i = nodeList.size() - 1; i > -1; i--){
            if(nodeList.get(i).frequency > node.frequency){
                idxWhereAdd = i;
            } else {
                break;
            }
        }

        nodeList.add(idxWhereAdd, node);
    }

    private void sortNodes(List<Node> nodeList) {
        // Sort nodes on the basis of frequency from the least frequent node to the most frequent
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                if(n1.frequency > n2.frequency){
                    return 1;
                } else if(n1.frequency < n2.frequency){
                    return -1;
                }
                return 0;
            }
        });
    }

    private boolean isLeafNode(Node node) {
        if(node.left == null && node.right == null){
            return true;
        } else {
            return false;
        }
    }
    
    /* builds an encoding string to byte map, i.e. converts a bit string array to its equivalent bit array or byte. */
    private void buildEncodingByteMap(){

        for(int count = 0; count <= 255;count++){
          StringBuilder string = new StringBuilder();
          
          if((count & 128) > 0){string.append("1");
            }else{string.append("0");};
          if((count & 64) > 0){string.append("1");
            }else {string.append("0");};
          if((count & 32) > 0){string.append("1");
            }else {string.append("0");};
          if((count & 16) > 0){string.append("1");
            }else {string.append("0");};
          if((count & 8) > 0){string.append("1");
            }else {string.append("0");};
          if((count & 4) > 0){string.append("1");
            }else {string.append("0");};
          if((count & 2) > 0){string.append("1");
            }else {string.append("0");};
          if((count & 1) > 0){string.append("1");
            }else {string.append("0");};
            
          encodingByteMap.put(string.toString(), (byte)(count));
        }
    }
    
    /* writes the output bytes to file */
	private void writeToFile(List<Byte> binaryBytes, String outputFile) {
		File f2 = null;
		OutputStream os = null;
		
		try{
			f2 = new File(outputFile);
			os = new FileOutputStream(f2);
			
			byte [] bytes = new byte[binaryBytes.size()];
			for(int i = 0; i < binaryBytes.size(); i++){
				bytes[i] = (byte)binaryBytes.get(i);
			}
			
			os.write(bytes);
			
			Helper.print("---- Encoding finish ----\n\n");
			
		}catch(IOException e){
			e.printStackTrace();
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
    
    public Node getHuffManTree(){
    	return huffmanTreeRoot;
    }
}
