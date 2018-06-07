package com.br.mom.ms.model;

public class ConsumerThreshold {
	private Integer id;
	private String groupName;
	private String queueName;
	private String port;
	private String consumerNumber;
	private String consumerLive;
	private String status;
	private String threshold;
	private String createTime;
	private String updateTime;

	public Integer getId() {
		return id;
	}

	public String getQueueName() {
		return queueName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public String getStatus() {
		return status;
	}

	public String getThreshold() {
		return threshold;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getConsumerLive() {
		return consumerLive;
	}

	public void setConsumerLive(String consumerLive) {
		this.consumerLive = consumerLive;
	}

	@Override
	public String toString() {
		return "ConsumerThreshold [id=" + id + ", groupName=" + groupName + ", queueName=" + queueName + ", port="
				+ port + ", consumerNumber=" + consumerNumber + ", consumerLive=" + consumerLive + ", status=" + status
				+ ", threshold=" + threshold + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
