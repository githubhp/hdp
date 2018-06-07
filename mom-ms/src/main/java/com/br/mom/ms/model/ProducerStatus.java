package com.br.mom.ms.model;

import java.io.Serializable;

public class ProducerStatus implements Serializable {
	/***/
	private static final long serialVersionUID = 6072798802691212715L;
	public Integer id;
	public Integer producerId;
	public Long enqueue;
	public String partitionData;
	public String createTime;
	public String updateTime;

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
	public Long getEnqueue() {
		return enqueue;
	}

	/**
	 * @param
	 */
	public void setEnqueue(Long enqueue) {
		this.enqueue = enqueue;
	}

	/**
	 * @return
	 */
	public String getPartitionData() {
		return partitionData;
	}

	/**
	 * @param
	 */
	public void setPartitionData(String partitionData) {
		this.partitionData = partitionData;
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

	/**
	 * @return
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
