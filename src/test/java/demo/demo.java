package demo;

public class demo {

	public static void main(String[] args){
		
		StringBuffer buffer = new StringBuffer("helloworld");
		String end = "hello";
		System.out.println(buffer.substring(end.length(), buffer.length()));
	}
}
