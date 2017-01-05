package com.cds.learn.jacksonParse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/5 10:59
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/5 10:59
 * @modify by reason:{方法名}:{原因}
 */
public class JackParser {
	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Map<String, Object> res = mapper.readValue(new File("src/main/resources/conf.yaml"), HashMap.class);
		System.out.println(res);

		for (Map.Entry<String, Object> entry : res.entrySet()) {

		}
		System.out.println(((HashMap)res.get("spring")).get("application"));
	}
}
