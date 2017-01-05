package com.cds.learn.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/4 16:08
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/4 16:08
 * @modify by reason:{方法名}:{原因}
 */
public class LogModel {
	//主ID
	private long id;
	//次ID
	private int subId;
	/**
	 * 系统名称
	 */
	private String systemName;
	private String host;

	//日志描述
	private String desc;
	private List<Integer> catIds;

	public LogModel() {
		Random random = new Random();
		this.id = Math.abs(random.nextLong());
		int subId = Math.abs(random.nextInt());
		this.subId = subId;
		List<Integer> list = new ArrayList<Integer>(5);
		for (int i = 0; i < 5; i++) {
			list.add(Math.abs(random.nextInt()));
		}
		this.catIds = list;
		this.systemName = subId % 1 == 0 ? "oa" : "cms";
		this.host = subId % 1 == 0 ? "10.0.0.1" : "10.2.0.1";
		this.desc = "中文" + UUID.randomUUID().toString();
	}

	public LogModel(long id, int subId, String sysName, String host, String desc, List<Integer> catIds) {
		this.id = id;
		this.subId = subId;
		this.systemName = sysName;
		this.host = host;
		this.desc = desc;
		this.catIds = catIds;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Integer> getCatIds() {
		return catIds;
	}

	public void setCatIds(List<Integer> catIds) {
		this.catIds = catIds;
	}
}
