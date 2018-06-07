package com.br.mom.ms.bean;

/**
 * 分布查询Bean
 * 
 * @author shupeng
 * @date 2014-3-10
 * @version 1.0
 */
public class PageBean {
	private int DEF_PAGE_VIEW_SIZE = 20;
	private int pageNo;
	private int pageSize;
	/** 取得查询总记录数 */
	private int count;
	/**
	 * 动作类型 <li>0：无动作</li> <li>1：首页</li> <li>2：前一页</li> <li>3：后一页</li> <li>4：末页</li>
	 * <li>5：跳转页</li> <li>6：重新设定每页记录数</li>
	 */
	private int actionType;

	/**
	 * (空)
	 */
	public PageBean() {
	}

	/**
	 * 根据当前显示页与每页显示记录数设置查询信息初始对象
	 * 
	 * @param page
	 *            当前显示页号
	 * @param pageSize
	 *            当前页显示记录条数
	 */
	public PageBean(int pageNo, int pageSize) {
		this.pageNo = (pageNo <= 0) ? 1 : pageNo;
		this.pageSize = (pageSize <= 0) ? DEF_PAGE_VIEW_SIZE : pageSize;
	}

	/**
	 * 取得动作类型
	 * 
	 * @return 动作类型
	 */
	public int getActionType() {
		return actionType;
	}

	/**
	 * 设置动作类型
	 * 
	 * @param actionType
	 *            动作类型
	 */

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return
	 */
	public int getStartNo() {
		return ((getPageNo() - 1) * getPageSize());
	}

	/**
	 * @return
	 */
	public int getPageNo() {
		return (pageNo <= 0) ? 1 : pageNo;
	}

	/**
	 * @param
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return
	 */
	public int getPageSize() {
		return (pageSize <= 0) ? DEF_PAGE_VIEW_SIZE : pageSize;
	}

	/**
	 * @param
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 取得当前查询总页数
	 * 
	 * @return 当前查询总页数
	 */
	public int getPages() {
		return (count + getPageSize() - 1) / getPageSize();
	}

	/**
	 * @param
	 */
	public void setCount(int count) {
		this.count = (count < 0) ? 0 : count;
		if (this.count == 0) {
			this.pageNo = 0;
			return;
		}
		switch (actionType) {
		case 1: // 第一页
			this.pageNo = 1;
			break;
		case 2: // 前一页
			this.pageNo = Math.max(1, this.pageNo - 1);
			break;
		case 3: // 后一页
			this.pageNo = Math.min(getPages(), this.pageNo + 1);
			break;
		case 4: // 最末页
			this.pageNo = getPages();
			break;
		case 5: // 指定页
		case 6: // 重新设定每页显示条数时
		case 0: // 无设定时
		default:
			this.pageNo = Math.min(getPages(), getPageNo());
		}
	}

	/**
	 * 取得前一显示页码
	 * 
	 * @return 前一显示页码
	 */
	public int getPrePageNo() {
		return Math.max(getPageNo() - 1, 1);
	}

	/**
	 * 取得后一显示页码
	 * 
	 * @return 后一显示页码
	 */
	public int getNextPageNo() {
		return Math.min(getPageNo() + 1, getPages());
	}

}
