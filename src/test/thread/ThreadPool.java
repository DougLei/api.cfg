package test.thread;


public class ThreadPool {
	
	 private static final int COUNT_BITS = Integer.SIZE - 3;

	    // runState is stored in the high-order bits
	    private static final int RUNNING    = -1 << COUNT_BITS;
	    private static final int SHUTDOWN   =  0 << COUNT_BITS;
	    private static final int STOP       =  1 << COUNT_BITS;
	    private static final int TIDYING    =  2 << COUNT_BITS;
	    private static final int TERMINATED =  3 << COUNT_BITS;
	
	public static void main(String[] args) {
		

		
		System.out.println(RUNNING);
		System.out.println(SHUTDOWN);
		System.out.println(STOP);
		System.out.println(TIDYING);
		System.out.println(TERMINATED);
		
	}
}
