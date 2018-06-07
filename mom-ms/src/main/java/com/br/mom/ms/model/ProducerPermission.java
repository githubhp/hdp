package com.br.mom.ms.model;

import java.io.Serializable;

public class ProducerPermission implements Serializable {
	/***/
	private static final long serialVersionUID = 3449220793111263650L;
	public int status;
	public boolean isActiveMqOk;
	public boolean isKafkaOk;
	public boolean isRedisOk;
	public boolean isAlarmOk;

	public ProducerPermission() {
	}

	/**
	 * constructor description
	 * 
	 * @param status
	 * @param isActiveMqOk
	 * @param isKafkaOk
	 * @param isRedisOk
	 * @param isAlarmOk
	 */
	public ProducerPermission(int status, boolean isActiveMqOk, boolean isKafkaOk, boolean isRedisOk,
			boolean isAlarmOk) {
		this.status = status;
		this.isActiveMqOk = isActiveMqOk;
		this.isKafkaOk = isKafkaOk;
		this.isRedisOk = isRedisOk;
		this.isAlarmOk = isAlarmOk;
	}

	/**
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return
	 */
	public boolean isActiveMqOk() {
		return isActiveMqOk;
	}

	/**
	 * @param
	 */
	public void setActiveMqOk(boolean isActiveMqOk) {
		this.isActiveMqOk = isActiveMqOk;
	}

	/**
	 * @return
	 */
	public boolean isKafkaOk() {
		return isKafkaOk;
	}

	/**
	 * @param
	 */
	public void setKafkaOk(boolean isKafkaOk) {
		this.isKafkaOk = isKafkaOk;
	}
	/**
	 * @param
	 */
	public void setKafkaOk2(boolean isKafkaOk2) {
		this.isKafkaOk = isKafkaOk2;
	}

	/**
	 * @return
	 */
	public boolean isRedisOk() {
		return isRedisOk;
	}

	/**
	 * @param
	 */
	public void setRedisOk(boolean isRedisOk) {
		this.isRedisOk = isRedisOk;
	}

	/**
	 * @return
	 */
	public boolean isAlarmOk() {
		return isAlarmOk;
	}

	/**
	 * @param
	 */
	public void setAlarmOk(boolean isAlarmOk) {
		this.isAlarmOk = isAlarmOk;
	}

}
