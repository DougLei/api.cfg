package com.king.tooth.websocket.pushmessage.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.annotation.WebApi;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.websocket.AbstractWebApi;
import com.king.tooth.websocket.pushmessage.PushMessageWebSocketServer;
import com.king.tooth.websocket.pushmessage.entity.PushMessageDataEntity;
import com.king.tooth.websocket.pushmessage.enums.PushMessageReturnCodeEnum;

/**
 * 消息推送的webapi
 * @author DougLei
 */
@WebApi
public class PushMessageWebApi extends AbstractWebApi{

	/**
	 * 给指定用户推送消息
	 * <p>请求方式：POST</p>
	 * @return 推送结果的编码
	 */
	@RequestMapping
	public Integer pushMessage(HttpServletRequest request, IJson ijson, Map<String, String> urlParams) {
		try {
			PushMessageDataEntity pushMessageDataEntity = getDataInstance(ijson, PushMessageDataEntity.class, true);
			if(pushMessageDataEntity == null){
				return PushMessageReturnCodeEnum.NOTNULL.getCode();
			}
			
			PushMessageReturnCodeEnum result = pushMessageDataEntity.analysisResourceProp();
			if(result != null){
				return result.getCode();
			}
			return PushMessageWebSocketServer.sendMessage(pushMessageDataEntity.getToUserId(), pushMessageDataEntity.getMessage());
		} catch (Exception e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg("PushMessageWebApi", "pushMessage", e));
			return PushMessageReturnCodeEnum.EXCEPTION.getCode();
		}
	}
	
	/**
	 * 批量给用户推送消息
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Integer[] batchPushMessage(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		try {
			PushMessageDataEntity pushMessageDataEntity = getDataInstance(ijson, PushMessageDataEntity.class, true);
			return batchPushMessage(pushMessageDataEntity);
		} catch (Exception e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg("PushMessageWebApi", "batchPushMessage", e));
			return new Integer[]{PushMessageReturnCodeEnum.EXCEPTION.getCode()};
		}
	}
	
	/**
	 * 批量给用户推送个性消息
	 * <p>即一个用户一个消息，如果对应不上，则抛出异常</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public List<Integer[]> batchPushIndividualityMessage(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<Integer[]> list = null;
		List<PushMessageDataEntity> pushMessageDataEntities = null;
		int pushMessageDataEntitiesSize = 0;
		
		try {
			pushMessageDataEntities = getDataInstanceList(ijson, PushMessageDataEntity.class, true);
			if(pushMessageDataEntities == null){
				return getBatchPushIndividualityMessageErrorResult(PushMessageReturnCodeEnum.NOTNULL);
			}
			
			pushMessageDataEntitiesSize = pushMessageDataEntities.size();
			list = new ArrayList<Integer[]>(pushMessageDataEntitiesSize); 
			for (PushMessageDataEntity pushMessageDataEntity : pushMessageDataEntities) {
				list.add(batchPushMessage(pushMessageDataEntity));
			}
			return list;
		} catch (Exception e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg("PushMessageWebApi", "batchPushIndividualityMessage", e));
			return getBatchPushIndividualityMessageErrorResult(PushMessageReturnCodeEnum.EXCEPTION);
		} finally{
			if(pushMessageDataEntitiesSize > 0){
				pushMessageDataEntities.clear();
			}
		}
	}
	private List<Integer[]> getBatchPushIndividualityMessageErrorResult(PushMessageReturnCodeEnum pushMessageReturnCodeEnum){
		List<Integer[]> errorResult = new ArrayList<Integer[]>(1);
		errorResult.add(new Integer[]{pushMessageReturnCodeEnum.getCode()});
		return errorResult;
	}
	
	/**
	 * 批量发送短信
	 * @param pushMessageDataEntity
	 * @return
	 */
	private Integer[] batchPushMessage(PushMessageDataEntity pushMessageDataEntity){
		if(pushMessageDataEntity == null){
			return new Integer[]{PushMessageReturnCodeEnum.NOTNULL.getCode()};
		}
		
		PushMessageReturnCodeEnum result = pushMessageDataEntity.analysisResourceProp();
		if(result != null){
			return new Integer[]{result.getCode()};
		}
		
		String message = pushMessageDataEntity.getMessage();
		String[] toUserIdArr = pushMessageDataEntity.getToUserId().split(",");
		int toUserIdArrLength = toUserIdArr.length;
		Integer[] pushResult = new Integer[toUserIdArrLength];
		
		for(int i=0;i<toUserIdArrLength;i++){
			pushResult[i] = PushMessageWebSocketServer.sendMessage(toUserIdArr[i], message);
		}
		return pushResult;
	}
}
