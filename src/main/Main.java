package main;

import main.huffman.HuffmanDecoder;
import main.huffman.HuffmanEncoder;
import main.huffman.Node;


public class Main {

	public static void main(String [] args){
		
		String origFile = "E:\\test\\branding.jpg";
		String compressedFile = "E:\\test\\branding_compressed.jpg";
		String uncompressedFile = "E:\\test\\branding_uncompressed.jpg";
		
		/* Encode File */
		HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
		huffmanEncoder.encode(origFile, compressedFile);


		/* For now using the same huffman tree generated by the encoder. 
		 * Though, this should be saved & restored from the image file itself or some other file. */
		Node huffmanRoot = huffmanEncoder.getHuffManTree();
		
		
		/* Decode File */
		HuffmanDecoder huffmanDecoder = new HuffmanDecoder();
		huffmanDecoder.setHuffmanRoot(huffmanRoot);
		huffmanDecoder.decode(compressedFile, uncompressedFile);
	}
	
	
	/* Test code below */
//	private void readWriteImg(){
//		File f = null;
//		InputStream is = null;
//		File f2 = null;
//		OutputStream os = null;
//		
//		try{
//			f = new File("E:\\test\\branding.jpg");
//			is = new FileInputStream(f);
//			f2 = new File("E:\\test\\branding_out.jpg");
//			os = new FileOutputStream(f2);
//			
//			Helper.print("--start--\n");
//			
//			byte[] bytes = Files.readAllBytes(Paths.get("E:\\test\\branding.jpg"));
//			
//			int k[] = new int[bytes.length];
//			for(int i = 0; i < bytes.length; i++){
//				k[i] = bytes[i] & 0xFF;
////				Helper.print("k = " + k[i] + "\n");
//			}
//			
//			byte [] bytes2 = new byte[k.length];
//			for(int i = 0; i < k.length; i++){
//				bytes2[i] = (byte)k[i];
//			}
//			
//			os.write(bytes2);
//			
////			BitSet bitSet = new BitSet();
////			bitSet.set(0);
////			bitSet.set(1);
////			bitSet.set(9);
////			
////			byte [] bs = bitSet.toByteArray();
////			
////			Helper.print("bs = " + bs.length + " : " + ((int)bs[0]) + bs[1] + "\n");
//			
//			os.close();
//			
////			Helper.print("Done: c1 : c2 : c3 : c4 = " + c1 + " : " + c2 + " : " + c3 + " : " + c4);
//			Helper.print("Done");
//			
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//	}
	
//	private void readWriteImage(){
//        BufferedImage img = null;
//        File f = null;
//
//        //read image
//        try{
//        	f = new File("E:\\test\\branding.jpg");
//            img = ImageIO.read(f);
//            
//            Helper.print("Image : " + img.getHeight() + " : " + img.getWidth());
//            
//        }catch(IOException e){
//            Helper.print("Error :" + e.getMessage());
//        }
//        
//        List<Integer> pixelsList = new ArrayList<>();
//        
//        if(img != null){
////        	for(int x = 0; x < img.getWidth(); x++){
////        		for(int y = 0; y < img.getHeight(); y++){
////        			int p = img.getRGB(x, y);
////        			pixelsList.add(p);
//////        			Helper.print("pixel = " + p + "\n");
////        		}
////        		
////        		Helper.print("x = " + x + "\n");
////        	}
//        	
//        	Helper.print("Pixels = " + pixelsList.size());
//        	
//        	final byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
//            final int width = img.getWidth();
//            final int height = img.getHeight();
//            final boolean hasAlphaChannel = img.getAlphaRaster() != null;
//
//            int[][] result = new int[height][width];
//            if (hasAlphaChannel) {
//            	Helper.print("Alpha channel");
//               final int pixelLength = 4;
//               for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
//                  int argb = 0;
//                  argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
//                  argb += ((int) pixels[pixel + 1] & 0xff); // blue
//                  argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
//                  argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
//                  result[row][col] = argb;
//                  pixelsList.add(argb);
//                  col++;
//                  if (col == width) {
//                     col = 0;
//                     row++;
//                  }
//               }
//            } else {
//               final int pixelLength = 3;
//               Helper.print("No Alpha channel");
//               for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
//                  int argb = 0;
//                  argb += -16777216; // 255 alpha
//                  argb += ((int) pixels[pixel] & 0xff); // blue
//                  argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
//                  argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
//                  result[row][col] = argb;
//                  pixelsList.add(argb);
//                  col++;
//                  if (col == width) {
//                     col = 0;
//                     row++;
//                  }
//               }
//            }
//        	
//        	
//        	int k = 0;
//        	for(int x = 0; x < img.getWidth(); x++){
//        		for(int y = 0; y < img.getHeight(); y++){
//        			if(k < pixelsList.size()){
//        				int p = pixelsList.get(k);
//            			img.setRGB(x, y, p);
//            			k++;
//        			}
//        		}
//        	}
//        	
//        	//write image
//            try{
//              f = new File("E:\\test\\branding_output.jpg");
//              ImageIO.write(img, "jpg", f);
//              Helper.print("done");
//            }catch(IOException e){
//              System.out.println(e);
//            }
//        }
//
//
//    }
}
