package test.face.engine;



/**
 * 
 * @author DougLei
 */
public class FaceEngineTest {
	public static void main(String[] args) {
		FaceEngineHandler handler = new FaceEngineHandler();
		System.out.println(handler);
		
		new FaceFeatureCompareThread("A", handler).start();
		new FaceFeatureCompareThread("B", handler).start();
		new FaceFeatureCompareThread("C", handler).start();
		new FaceFeatureCompareThread("D", handler).start();
		new FaceFeatureCompareThread("E", handler).start();
		new FaceFeatureCompareThread("F", handler).start();
		new FaceFeatureCompareThread("G", handler).start();
		new FaceFeatureCompareThread("H", handler).start();
		new FaceFeatureCompareThread("I", handler).start();
		new FaceFeatureCompareThread("J", handler).start();
	}
}
