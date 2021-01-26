package com.api.cache;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.listener.InitSysDataListener;
import com.api.util.hibernate.HibernateUtil;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;

/**
 * 
 * @author DougLei
 */
public class FaceEngineContext {
	private static final Logger logger = LoggerFactory.getLogger(FaceEngineContext.class);
	public static float similarScore;
	private static Map<String, FaceFeature> faceFeatureMap;
	private static FaceEngine faceEngine;
	
	@SuppressWarnings("unchecked")
	public static void setFaceEngine(FaceEngine faceEngine) {
		logger.info("加载FaceEngine成功");
		FaceEngineContext.faceEngine = faceEngine;
		
//		int errorCode = FaceEngineContext.faceEngine.activeOnline(SysContext.getSystemConfig("face.app.id"), SysContext.getSystemConfig("face.sdk.key"));
		int errorCode;
		try {
			logger.info("从tomcat的目录上, 加载FaceEngine授权文件: {}", SysContext.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar + "FaceEngine.dat");
			errorCode = faceEngine.activeOffline(SysContext.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar + "FaceEngine.dat");
		} catch (Throwable e) {
			logger.info("从tomcat路径加载FaceEngine授权文件出现异常: {}", InitSysDataListener.getExceptionDetailMessage(e));
			try {
				errorCode = faceEngine.activeOffline("D:\\workspace3\\api.cfg\\resources\\FaceEngine.dat");
			} catch (Throwable e1) {
				logger.info("从eclipse中测试用, 使用project路径加载FaceEngine授权: D:\\workspace3\\api.cfg\\resources\\FaceEngine.dat, 仍然出现异常, 所以目前系统不支持使用人脸认证功能");
				return;
			}
		}
		
        if(errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue())
        	throw new RuntimeException("刷脸登录用FaceEngine激活失败, 请联系开发人员, errorCode="+errorCode);
        logger.info("加载FaceEngine授权文件成功, errorCode={}", errorCode);
        
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
        errorCode = FaceEngineContext.faceEngine.init(engineConfiguration);
        if (errorCode != ErrorInfo.MOK.getValue()) 
        	throw new RuntimeException("刷脸登录用FaceEngine初始化失败, 请联系开发人员, errorCode="+errorCode);
        logger.info("初始化FaceEngine, errorCode={}", errorCode);
        
        logger.info("初始化已存在的面部特征集合");
        HibernateUtil.openSessionToCurrentThread();
        List<Object[]> faces = HibernateUtil.executeListQueryBySqlArr("select ref_data_id, path, LOGIN_NAME from SYS_FACE_IMAGE f left join sys_account a on (f.REF_DATA_ID = a.ID)");
        if(faces != null && !faces.isEmpty()){
        	File file;
        	List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>(1);
        	ImageInfo imageInfo;
        	FaceFeature faceFeature;
        	for (Object[] objects : faces) {
        		if(objects[0] == null || objects[1] == null){
        			logger.info("ref_data_id=[{}], path=[{}]", objects[0], objects[1]);
        			continue;
        		}
        			
        		file = new File(SysContext.WEB_SYSTEM_CONTEXT_REALPATH + objects[1].toString());
        		if(file.exists()){
        			logger.info("加载userId={}, loginName={}, file={} 的FaceImage", objects[0], objects[2], file);
        			imageInfo = getRGBData(new File(SysContext.WEB_SYSTEM_CONTEXT_REALPATH + objects[1].toString()));
            		faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            		
            		faceFeature = new FaceFeature();
            		faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
            		
            		if(faceFeatureMap == null)
            			faceFeatureMap = new HashMap<String, FaceFeature>();
            		faceFeatureMap.put(objects[0].toString(), faceFeature);
        		}
			}
        }
       
    	FaceEngineContext.similarScore = Float.parseFloat(SysContext.getSystemConfig("face.similar.score"));
        logger.info("成功完成FaceEngine的初始化, 系统设置的相似度阈值为: {}", FaceEngineContext.similarScore);
	}
	
	/**
	 * 提取面部特征
	 * @param input
	 * @return
	 */
	public static FaceFeature extractFaceFeature(InputStream input){
		System.out.println("synchronized =========================================> synchronized");
		ImageInfo imageInfo = getRGBData(input);
		List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>(1);
		synchronized (faceEngine) {
			faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
		}
		
		if(faceInfoList.isEmpty())
			return null;
		
		FaceFeature faceFeature = new FaceFeature();
		faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature );
		return faceFeature;
	}
	
	/**
	 * 将请求的面部特征, 与面部特征集合进行比对
	 * @param requestFaceFeature
	 * @return 如果满足相似度的值, 返回对应特征值的code
	 */
	public static String compare(FaceFeature requestFaceFeature){
		if(faceFeatureMap != null && requestFaceFeature != null){
			FaceSimilar faceSimilar = new FaceSimilar();
			for (Entry<String, FaceFeature> entity : faceFeatureMap.entrySet()) {
				faceEngine.compareFaceFeature(entity.getValue(), requestFaceFeature, faceSimilar);
				logger.info("使用用户id为[{}]的面部特征, 与当前用户的面部特征比较, 相似度值为:[{}]", entity.getKey(), faceSimilar.getScore());
				if(faceSimilar.getScore() >= similarScore)
					return entity.getKey();
			}
		}
        return null;
	}
	
	/**
	 * 更新指定用户的面部特征
	 * @param userId
	 * @param faceImage 面部照片
	 */
	public static void updateFaceFeature(String userId, File faceImage){
		try {
			FaceFeature faceFeature = extractFaceFeature(new FileInputStream(faceImage));
			if(faceFeature != null){
				if(faceFeatureMap == null)
					faceFeatureMap = new HashMap<String, FaceFeature>();
				faceFeatureMap.put(userId, faceFeature);
			}
		} catch (FileNotFoundException e) {}
	}
	
	/**
	 * 移除指定用户的面部特征
	 * @param userId
	 */
	public static void removeFaceFeature(String userId) {
		if(faceFeatureMap != null && !faceFeatureMap.isEmpty())
			faceFeatureMap.remove(userId);
	}
}
