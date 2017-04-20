package toolbox;

public class ToolBox {

	public static String rollLetter(){
		// ASCII 65-90 refers to A-Z
		char randomFactor = (char) (26 * Math.random() -1);
		char letter = (char) (65 + randomFactor);
		return String.valueOf(letter);
	}
	
	public int parseFourDigitHex(String str) {
		return 0;
		
	}
	
	public static void main(String[] args){
		System.out.println(rollLetter());
	}
}
