package com.br.mom.ms.bean;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private int pageNow = 1;// 当前页
	private int pageSum = 0; // 页面总个数
	private int pageSize = 20;// 每页显示记录条数
	private int totalRecords = 0;// 总的记录条数
	private List<T> list;// 封装对象数据
	private int customPageSize = 0; // 自定义 单页面的数据个数

	public Page() {
		super();
	}

	public Page(int pageNow, int pageSize) {
		super();
		this.pageNow = pageNow;
		this.pageSize = pageSize;
	}

	public Page(int pageNow, int pageSize, int totalRecords, List<T> list, int customPageSize) {
		super();
		this.list = list;
		this.pageNow = pageNow;
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
		this.customPageSize = customPageSize;
	}

	// 获取总页数
	public int getTotalPages() {
		return this.pageSum;
	}

	// 获取首页
	public int getFirstPage() {
		return 1;
	}

	// 获取尾页
	public int getEndPage() {
		return getTotalPages();
	}

	// 判断是否存在上一页
	public boolean isHasLastPage() {
		if (pageNow > 1) {
			return true;
		} else {
			return false;
		}
	}

	// 获取上一页
	public int getLastpage() {
		if (isHasLastPage()) {
			return (pageNow - 1);
		} else {
			return 1;
		}
	}

	// 判断是否存在下一页
	public boolean isHasNextPage() {
		if (pageNow < getTotalPages()) {
			return true;
		} else {
			return false;
		}
	}

	// 获取下一页
	public int getNextPage() {
		if (isHasNextPage()) {
			return pageNow + 1;
		} else {
			return getTotalPages();
		}
	}

	// 获取页面展示区间的开始坐标,现在没用这种模式,备用
	public int getBeginIndex() {
		if (getTotalPages() <= 10) {
			return 1;
		} else {
			if (pageNow > 4) {
				return pageNow - 4;
			} else {
				return 1;
			}
		}
	}

	// 获取页面展示区间的结束坐标,现在没用这种模式,备用
	public int getEndIndex() {
		if (getTotalPages() <= 10) {
			return getTotalPages();
		} else {
			if (pageNow > 4 && getTotalPages() > pageNow + 5) {
				return pageNow + 5;
			} else if (pageNow > 4 && getTotalPages() <= pageNow + 5) {
				return getTotalPages();
			} else {
				return 10;
			}
		}
	}

	// 获取后台查询开始索引
	public int getStartIndex() {
		if (pageNow > 0) {
			return pageSize * (pageNow - 1);
		} else {
			return 0;
		}
	}

	// 获取当前页
	public int getPageNow() {
		if (pageNow > 0) {
			return pageNow;
		} else {
			return 1;
		}
	}

	// 设置当前页
	public void setPageNow(int pageNow) {
		if (pageNow > 0) {
			this.pageNow = pageNow;
		} else {
			this.pageNow = 1;
		}
	}

	// 判断是不是需要分页
	public boolean isPageCount() {
		return getTotalRecords() > pageSize;
	}

	// 是否禁用首页
	public boolean disabledFirstPage() {
		return getTotalPages() == 0 || getTotalPages() == 1 || this.pageNow == 0 || this.pageNow == 1;
	}

	// 是否禁用上一页
	public boolean disabledPreviousPage() {
		return getTotalPages() == 0 || getTotalPages() == 1 || this.pageNow == 0 || this.pageNow == 1;
	}

	// 是否禁用下一页
	public boolean disabledNextPage() {
		return getTotalPages() == 0 || getTotalPages() == 1 || this.pageNow == getTotalPages();
	}

	// 是否禁用尾页
	public boolean disabledLastPage() {
		return getTotalPages() == 0 || getTotalPages() == 1 || this.pageNow == getTotalPages();
	}

	// 是否禁用go
	public boolean disabledTurnTo() {
		return getTotalPages() == 0 || getTotalPages() == 1;
	}

	// 重新计算各个属性的值
	public void computeAttributes() {

		// 单页面的数据个数
		this.pageSize = getPageSize() <= 0 ? 1 : getPageSize();

		// 数据总个数
		this.totalRecords = getTotalRecords() < 0 ? 0 : getTotalRecords();

		// 页面总个数
		if (this.totalRecords == 0) {
			this.pageSum = 0;
		} else {
			int iResidualNum = this.totalRecords % this.getPageSize();
			int iDivision = this.totalRecords / this.getPageSize();
			this.pageSum = iDivision == 0 ? 1 : iDivision + (iResidualNum == 0 ? 0 : 1);
		}

		// 当前页号
		this.pageNow = this.pageNow <= 0 ? 1 : (this.pageNow > this.pageSum ? this.pageSum : this.pageNow);
	}

	public int getPageSize() {
		return this.customPageSize == 0 ? this.pageSize : this.customPageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		this.computeAttributes();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getCustomPageSize() {
		return customPageSize;
	}

	public void setCustomPageSize(int customPageSize) {
		this.customPageSize = customPageSize;
	}

	public int getPageSum() {
		return pageSum;
	}

	public void setPageSum(int pageSum) {
		this.pageSum = pageSum;
	}

}
