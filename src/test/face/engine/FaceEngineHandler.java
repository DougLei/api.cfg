package test.face.engine;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;

public class FaceEngineHandler {
	private FaceEngine faceEngine;
	private Map<String, FaceFeature> defaultFaceFeatureMap = new HashMap<String, FaceFeature>();
	
	public FaceEngineHandler() {
		buildFaceEngine();
		setDefaultFaceFeatureMap();
	}
	
	// 构建FaceEngine
	private void buildFaceEngine(){
		//引擎配置
		EngineConfiguration engineConfiguration = new EngineConfiguration();
		engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
		engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
		engineConfiguration.setDetectFaceMaxNum(10);
		engineConfiguration.setDetectFaceScaleVal(16);
		//功能配置
		FunctionConfiguration functionConfiguration = new FunctionConfiguration();
		functionConfiguration.setSupportAge(true);
		functionConfiguration.setSupportFace3dAngle(true);
		functionConfiguration.setSupportFaceDetect(true);
		functionConfiguration.setSupportFaceRecognition(true);
		functionConfiguration.setSupportGender(true);
		functionConfiguration.setSupportLiveness(true);
		functionConfiguration.setSupportIRLiveness(true);
		engineConfiguration.setFunctionConfiguration(functionConfiguration);

		//初始化引擎
		this.faceEngine = new FaceEngine("D:\\workspace3\\api.cfg\\resources\\dll\\face");
		this.faceEngine.init(engineConfiguration);
	}
	
	// 设置默认的面部特征Map集合
	private void setDefaultFaceFeatureMap() {
		// 设置默认的面部特征Map集合
		File[] defaultFacePictures = new File("D:\\workspace3\\api.cfg\\WebContent\\files\\upload\\faceLogin").listFiles();
		for (File facePicture : defaultFacePictures) {
			FaceFeature faceFeature = extractFaceFeature(facePicture);
			defaultFaceFeatureMap.put(facePicture.getAbsolutePath(), faceFeature);
		}
	}
	
	/**
	 * 从指定文件提取面部特征
	 * @param file
	 * @return 返回null则表示提取失败
	 */
	public synchronized FaceFeature extractFaceFeature(File file){
		ImageInfo imageInfo = getRGBData(file);
		List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>(1);
		
		faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
		if(faceInfoList.isEmpty())
			return null;
		
		FaceFeature faceFeature = new FaceFeature();
		faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature );
		return faceFeature;
	}
	
	/**
	 * 与默认的面部特征Map集合比较
	 * @param current
	 * @return 返回null表示没有匹配到合适的; 否则返回对应面部特征图片文件的绝对路径
	 */
	public String compareWithDefaultFaceFeatureMap(FaceFeature current){
		FaceSimilar faceSimilar = new FaceSimilar();
		for (Entry<String, FaceFeature> entity : defaultFaceFeatureMap.entrySet()) {
			faceEngine.compareFaceFeature(entity.getValue(), current, faceSimilar);
			if(faceSimilar.getScore() >= 0.9)
				return entity.getKey();
		}
		return null;
	}
}
