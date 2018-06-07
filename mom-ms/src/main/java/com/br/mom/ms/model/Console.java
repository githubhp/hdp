package com.br.mom.ms.model;

import java.io.Serializable;

public class Console implements Serializable {
	/***/
	private static final long serialVersionUID = 7839284895868469403L;

	public Integer id;
	public Integer queueId;
	public String content;
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
	public Integer getQueueId() {
		return queueId;
	}

	/**
	 * @param
	 */
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	/**
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param
	 */
	public void setContent(String content) {
		this.content = content;
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
