package com.gec.bean;




/**
 * @ClassName PageUtils
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/2/24 18:54
 * @Version 1.0
 */

public class PageModel {
    //当前页码
    private int pageIndex;
    //每页显示数量
    public static  final int pageSize = 4;
    //总页数
    private int totalPageSum;
    //总记录数
    private int totalRecoreSum;

    public int getPageIndex() {
        //假设输入当前页码是负数
        this.pageIndex =  this.pageIndex <= 1 ? 1:this.pageIndex;
        //假设输入的页码是大于等于总页数
        this.pageIndex = this.pageIndex >= getTotalPageSum() ? getTotalPageSum(): this.pageIndex;
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public static int getPageSize() {
        return pageSize;
    }

    //总页面数的计算
    public int getTotalPageSum() {
        //总记录数%每页显示数量   能直接整除直接除 不能整除+1(不满一页当一页用）
        this.totalPageSum = getTotalRecoreSum() % getPageSize() == 0 ? getTotalRecoreSum()/getPageSize() : getTotalRecoreSum()/getPageSize()+1;
        return totalPageSum;
    }

    //起始行算法
    public int getStartRow(){
        //(getPageIndex()-1)*getPageSize() (当前页码-1)*每页显示数量
        int start = (getPageIndex() -1) >=1 ? (getPageIndex()-1)*getPageSize() : 0;
        return start;
    }

    public void setTotalPageSum(int totalPageSum) {
        this.totalPageSum = totalPageSum;
    }

    public int getTotalRecoreSum() {
        return totalRecoreSum;
    }

    public void setTotalRecoreSum(int totalRecoreSum) {
        this.totalRecoreSum = totalRecoreSum;
    }
}


