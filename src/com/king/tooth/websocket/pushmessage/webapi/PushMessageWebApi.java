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
import com.king.tooth.util.StrUtils;
import com.king.tooth.websocket.AbstractWebApi;
import com.king.tooth.websocket.pushmessage.PushMessageWebSocketServer;
import com.king.tooth.websocket.pushmessage.entity.PushMessageDataEntity;
import com.king.tooth.websocket.pushmessage.enums.PushMessageReturnCodeEnum;
import com.king.tooth.websocket.pushmessage.mapping.CustomerMapping;

/**
 * 消息推送的webapi
 * @author DougLei
 */
@WebApi
public class PushMessageWebApi extends AbstractWebApi{

	/**
	 * 验证请求的客户信息是否有效
	 * @param customerToken
	 * @return
	 */
	private PushMessageReturnCodeEnum customerIsValid(HttpServletRequest request){
		String customerToken = request.getHeader("customerToken");
		if(StrUtils.isEmpty(customerToken)){
			return PushMessageReturnCodeEnum.CUSTOMER_NOTNULL;
		}
		if(CustomerMapping.customerIsExists(customerToken)){
			return PushMessageReturnCodeEnum.CUSTOMER_INVALID;
		}
		return null;
	}
	
	/**
	 * 给指定用户推送消息
	 * <p>请求方式：POST</p>
	 * @return 推送结果的编码
	 */
	@RequestMapping
	public Integer pushMessage(HttpServletRequest request, IJson ijson, Map<String, String> urlParams) {
		try {
			PushMessageReturnCodeEnum result = customerIsValid(request);
			if(result != null){
				return result.getCode();
			}
			
			PushMessageDataEntity pushMessageDataEntity = getDataInstance(ijson, PushMessageDataEntity.class, true);
			if(pushMessageDataEntity == null){
				return PushMessageReturnCodeEnum.NOTNULL.getCode();
			}
			
			result = pushMessageDataEntity.analysisResourceProp();
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
			PushMessageReturnCodeEnum result = customerIsValid(request);
			if(result != null){
				return new Integer[]{result.getCode()};
			}
			
			PushMessageDataEntity pushMessageDataEntity = getDataInstance(ijson, PushMessageDataEntity.class, true);
			return batchPushMessage(pushMessageDataEntity);
		} catch (Exception e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg("PushMessageWebApi", "batchPushMessage", e));
			return new Integer[]{PushMessageReturnCodeEnum.EXCEPTION.getCode()};
		}
	}
	
	/**
	 * 批量给用户推送个性消息
	 * <p>即同时给多个用户推送多条消息</p>
	 * <p>例如前两个用户推送同一条消息，后三个用户推送同一条消息</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public List<Integer[]> batchPushIndividualityMessage(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		PushMessageReturnCodeEnum result = customerIsValid(request);
		if(result != null){
			return getBatchPushIndividualityMessageErrorResult(result);
		}
		
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
