package com.gec.bean;

/*
 *对应部门表的javabean
 * */
public class Dept implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;		// id
	private String name;	// 部门名称
	private String remark;	// 详细描述
	private Integer status; //状态
	// 无参数构造器
	public Dept() {
		super();
	}
	
	
	public Dept(Integer id) {
		super();
		this.id = id;
	}


	public Dept(String name, String remark) {
		this.name = name;
		this.remark = remark;
	}
	public Dept(String name) {
		this.name = name;
	}
	// setter和getter方法
	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return this.id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public String getRemark(){
		return this.remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Dept [id=" + id + ", name=" + name + ", remark=" + remark + "]";
	}

}