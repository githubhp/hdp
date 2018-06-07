package com.br.mom.ms.model;

import java.io.Serializable;

public class User implements Serializable {
	/***/
	private static final long serialVersionUID = 8436692819733625597L;
	public Integer id;
	public String account;
	public String passwd;
	public String email;
	public Integer userRole;
	public Integer valied;
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
	public String getAccount() {
		return account;
	}

	/**
	 * @param
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @param
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return
	 */
	public Integer getUserRole() {
		return userRole;
	}

	/**
	 * @param
	 */
	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return
	 */
	public Integer getValied() {
		return valied;
	}

	/**
	 * @param
	 */
	public void setValied(Integer valied) {
		this.valied = valied;
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
