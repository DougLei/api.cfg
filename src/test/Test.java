package test;



public class Test {
	public static void main(String[] args) {
		StringBuilder idBuffer = new StringBuilder();
		
		System.out.println(dob(idBuffer));;
		
		System.out.println(idBuffer);
	}

	private static String dob(StringBuilder idBuffer) {
		try {
			idBuffer.append("dddd");
			return idBuffer.toString();
		} finally{
			idBuffer.setLength(0);
			idBuffer.append("1");
		}
		
	}
}
 