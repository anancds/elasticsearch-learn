package com.cds.learn.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/4 16:37
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/4 16:37
 * @modify by reason:{方法名}:{原因}
 */
public class ESUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static String toJson(Object o){
		try {
			return objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
