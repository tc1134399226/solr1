package com.qfjy.utils;

import java.util.List;

/*
 * 分页帮助类
 */
public class PageUtils {
	/*
	 * LIMIT [OFFSET] , ROWS 1、SQL语句最后端 2、OFFSET ：偏移量 (0)不是1 3、ROWS： 返回的最大记录数
	 * 4、整数
	 * 
	 * 每页显示3条数据 当前是第1页时 0 ，3 当前是第2页时 3 , 3 当前是第3页时 6 , 3
	 * 
	 * 每页显示数据：pageSize 当前页数： currPage
	 *  OFFSET: ==》 页数 、每页显示数据 (当前页-1)*页大小
	 * 
	 */

	private int pageSize=5; // 每页大小  默认显示5条
	private int currPage=1;// 当前页数
	private int offset; // 偏移量
	private int count; // 总记录数
	private int countPage;// 总页数 count/countPage...
	private List<?> list;// ?未知的数据类型 用于存放分页显示的数据信息

	public PageUtils(int count, int pageSize, int currPage) {
		if(pageSize>1){
			this.pageSize=pageSize;
		}else{
			this.pageSize=5;
		}
		if(currPage>1){
			this.currPage=currPage;
		}else{
			this.currPage=1;
		}
		//得到总页数
		this.countPage=count%pageSize==0?count/pageSize:count/pageSize+1;
		this.count=count;//总记录数

		//偏移量
		this.offset=(this.currPage-1)*this.pageSize;

		if(currPage>countPage){
				this.currPage=countPage;
		}
	}

	public PageUtils() {

	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

}
