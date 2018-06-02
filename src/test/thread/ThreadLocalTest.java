package test.thread;

public class ThreadLocalTest {
	private static final ThreadLocal<String> currentThread = new ThreadLocal<String>();
	
	
	public static void setContent(String content){
		currentThread.set(content);
	}
	public static String getContent(){
		return currentThread.get();
	}
}
