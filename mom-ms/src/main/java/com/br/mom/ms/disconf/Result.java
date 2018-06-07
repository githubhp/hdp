package com.br.mom.ms.disconf;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum Result {

	SUCCESS(0, "成功"), LOGIN_FAILURE(1, "登陆失败"), CREATE_FAILURE(2, "创建失败"), CHANGE_FAILURE(3, "修改失败");
	private int no;
	private String message;

	private Result(int no, String message) {
		this.no = no;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public int getKey() {
		return no;
	}
}
