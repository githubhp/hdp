package com.br.mom.ms.model;

import java.io.Serializable;

public class ConsumerBroker implements Serializable {
	/***/
	private static final long serialVersionUID = -1148005485159356764L;
	public Integer id;
	public String ip;
	public String port;
	public Integer status;
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
	public String getIp() {
		return ip;
	}

	/**
	 * @param
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
