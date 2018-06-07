package com.br.mom.ms.model;

import java.io.Serializable;

public class Redis implements Serializable {
	/***/
	private static final long serialVersionUID = 7701551436505409466L;
	public Integer id;
	public String ip;
	public String port;
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
