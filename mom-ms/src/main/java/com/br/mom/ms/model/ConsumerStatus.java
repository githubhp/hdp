package com.br.mom.ms.model;

import java.io.Serializable;

public class ConsumerStatus implements Serializable {
	/***/
	private static final long serialVersionUID = 3345248790141381102L;
	private Integer id;
	private Integer consumerId;
	private Long dequeue;
	private String partitionData;
	private String createTime;
	private String updateTime;

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
	public Integer getConsumerId() {
		return consumerId;
	}

	/**
	 * @param
	 */
	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}

	/**
	 * @return
	 */
	public Long getDequeue() {
		return dequeue;
	}

	/**
	 * @param
	 */
	public void setDequeue(Long dequeue) {
		this.dequeue = dequeue;
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

	@Override
	public String toString() {
		return "ConsumerStatus [id=" + id + ", consumerId=" + consumerId + ", dequeue=" + dequeue + ", partitionData="
				+ partitionData + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
