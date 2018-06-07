package com.br.mom.ms.model;

import java.io.Serializable;

public class UserProducer implements Serializable {
	/***/
	private static final long serialVersionUID = -644227354577898597L;
	public Integer id;
	public Integer userId;
	public Integer producerId;
	public String createTime;

	/**
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return
	 */
	public Integer getProducerId() {
		return producerId;
	}

	/**
	 * @param
	 */
	public void setProducerId(Integer producerId) {
		this.producerId = producerId;
	}

	/**
	 * @return
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
