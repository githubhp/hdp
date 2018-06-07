package com.br.mom.ms.model;

import java.io.Serializable;

public class Consumer implements Serializable {

	private static final long serialVersionUID = 3014606359576343118L;
	public Integer id;
	public String name;
	public Integer queueId;
	public Integer momType;
	public Integer persistentType;
	public String params;
	public String createTime;
	public Integer flag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public Integer getMomType() {
		return momType;
	}

	public void setMomType(Integer momType) {
		this.momType = momType;
	}

	public Integer getPersistentType() {
		return persistentType;
	}

	public void setPersistentType(Integer persistentType) {
		this.persistentType = persistentType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}
