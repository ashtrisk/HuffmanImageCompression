package main;
import java.util.Scanner;


public class Helper {
	
	public static void print(String str){
		System.out.print(str);
	}
	
	public static void println(String str){
		System.out.println(str);
	}
	
	public static String getUserString(){
		Scanner sc = new Scanner(System.in);
		
		print("Enter a string : ");
		
		String str = sc.nextLine();		// get the user string line
		
		return str;
	}
	
}