package test.face.engine;

import java.io.File;

/**
 * 面部特征比较线程
 * @author DougLei
 */
public class FaceFeatureCompareThread extends Thread {
	private FaceEngineHandler handler;
	private File file = new File("C:\\Users\\Administrator.USER-20190410XF\\Desktop\\test.png");
	
	public FaceFeatureCompareThread(String name, FaceEngineHandler handler) {
		super(name);
		this.handler = handler;
	}

	@Override
	public void run() {
		while (true) {
			String result = handler.compareWithDefaultFaceFeatureMap(handler.extractFaceFeature(file));
			System.out.println("线程["+getName()+"], 比较面部特征的结果为: " + result);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
