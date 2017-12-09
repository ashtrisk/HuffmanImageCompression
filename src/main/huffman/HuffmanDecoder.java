package main.huffman;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.Helper;

/**
 * Created by Vostro-Daily on 12/3/2017.
 *
 * Decodes an encoded bit array to its original symbols.
 * Requires the file to read these bits, Huffman tree to decode the bits to their original symbols.
 * 
 * 
 */

public class HuffmanDecoder {

    private static final String TAG = HuffmanDecoder.class.getSimpleName();
    
    private String encodedStr;
    private Node rootNodeHuffTree;

    private int currIdx;

    private ArrayList<Byte> decodedBytes = new ArrayList<Byte>();
	private Map<Byte, String> decodingByteMap = new HashMap<>();
    
    public void setHuffmanRoot(Node huffmanRoot) {
		this.rootNodeHuffTree = huffmanRoot;
	}
    
    public void decode(String encodedFile, String uncompressedFile){
    	
    	Helper.print("--- Decoding ----\n");
    	
    	byte [] bytes = readBytes(encodedFile);
    	
    	Helper.print("Compressed size : " + (bytes.length / 1024) + " KB\n");
    	
    	/* Build map to decode bytes to string */
    	buildDecodingByteMap();
    	
    	/* convet the bit array of 1010.. to equivalent string array of "1010.." */
    	encodedStr = convertBitsToString(bytes);
    	
    	/* Decode each symbol one by one as it comes in the encodedStr */
    	while(currIdx < encodedStr.length()){
            decodeNextSymbol(this.rootNodeHuffTree);
        }
    	
    	Helper.print("Uncompressed size : " + (decodedBytes.size() / 1024) + " KB\n");
    	
    	writeToFile(uncompressedFile);

    }
    
    /* Reads bytes from the input file */
	private byte[] readBytes(String encodedFile) {
    	File f = null;
		InputStream is = null;
		byte[] bytes = null;
		
		try {
			f = new File(encodedFile);
			bytes = new byte[(int)f.length()];
			is = new FileInputStream(f);
			
			int size;
			while((size = is.read(bytes)) != -1){
				// Do nothing
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

    private void writeToFile(String uncompressedFile) {
    	File file = null;
		OutputStream os = null;
		
		try{
			file = new File(uncompressedFile);
			os = new FileOutputStream(file);
			
			byte [] bytes = new byte[decodedBytes.size()];
			for(int i = 0; i < decodedBytes.size(); i++){
				bytes[i] = (byte)decodedBytes.get(i);
			}
			
			os.write(bytes);
			
			Helper.print("---- Decoding finish ----\n");
			
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

	private String convertBitsToString(byte [] bytes) {
    	StringBuilder builder = new StringBuilder();

    	for(int i = 0; i < bytes.length; i++){
    		builder.append(decodingByteMap.get((Byte)bytes[i]));
    	}

		return builder.toString();
	}


    /* Decodes the bit array for a single symbol.
     * Also increase the current index to point to next bit & ultimately to the next symbol */
    private void decodeNextSymbol(Node node)
    {
        if(isLeafNode(node)){
        	decodedBytes.add((byte)node.symbol);
        } else {
            if(currIdx < encodedStr.length()){
            	/* Not a leaf node, traverse to right or left of node */
                if(encodedStr.charAt(currIdx) == '0'){
                    currIdx++;
                    // Move to left node
                    decodeNextSymbol(node.left);
                } else {
                    currIdx++;
                    // code must be '1', thus move to right node
                    decodeNextSymbol(node.right);
                }
            }
        }
    }

    private boolean isLeafNode(Node node) {
        if(node.left == null && node.right == null){
            return true;
        } else {
            return false;
        }
    }
    
    void buildDecodingByteMap(){
        for(int count = 0; count <= 255;count++){
          StringBuilder builder = new StringBuilder();
          
          if((count & 128) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 64) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 32) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 16) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 8) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 4) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 2) > 0){builder.append("1");
            }else {builder.append("0");};
          if((count & 1) > 0){builder.append("1");
            }else {builder.append("0");};
            
          decodingByteMap.put((byte)(count), builder.toString());
        }
    }

}
