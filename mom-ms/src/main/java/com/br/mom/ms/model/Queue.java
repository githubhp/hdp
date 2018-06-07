package com.br.mom.ms.model;

import java.io.Serializable;

public class Queue implements Serializable {

	private static final long serialVersionUID = -4913716464566626879L;
	public Integer id;
	public String name;
	public Integer contentFormat;
	public Integer momType;
	public Integer status;
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

	public Integer getContentFormat() {
		return contentFormat;
	}

	public void setContentFormat(Integer contentFormat) {
		this.contentFormat = contentFormat;
	}

	public Integer getMomType() {
		return momType;
	}

	public void setMomType(Integer momType) {
		this.momType = momType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
