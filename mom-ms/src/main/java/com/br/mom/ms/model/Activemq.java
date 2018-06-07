package com.br.mom.ms.model;

import java.io.Serializable;

public class Activemq implements Serializable {
	/***/
	private static final long serialVersionUID = 7044081189842577027L;

	public Integer id;
	public String serverList;
	public String username;
	public String password;
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
	public String getServerList() {
		return serverList;
	}

	/**
	 * @param
	 */
	public void setServerList(String serverList) {
		this.serverList = serverList;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param
	 */
	public void setPassword(String password) {
		this.password = password;
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
