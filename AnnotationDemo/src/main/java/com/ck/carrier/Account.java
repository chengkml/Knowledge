package com.ck.carrier;

import java.util.Random;

import com.ck.anno.ConstructorAnnotationDemo;
import com.ck.anno.FieldAnnotationDemo;
import com.ck.anno.LocalVariableAnnotationDemo;
import com.ck.anno.ParameterAnnotationDemo;

/**
 * @title 账户类
 * @author Chengkai
 * @date 2018年7月3日
 * @time 下午7:34:44
 * @describe
 */
public class Account {
	/**
	 * 唯一标识
	 */
	@FieldAnnotationDemo
	private long id;
	/**
	 * 用户
	 */
	private String user;
	/**
	 * 存款
	 */
	private double balance;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 账户是否有效
	 */
	private int sevstate;
	
	public Account(@ParameterAnnotationDemo long id, String user, double balance, String remark, int sevstate) {
		super();
		if(id<=0) {
			@LocalVariableAnnotationDemo
			long temp = new Random().nextLong();
			id  = temp;
		}else {
			this.id = id;
		}
		this.user = user;
		this.balance = balance;
		this.remark = remark;
		this.sevstate = sevstate;
	}
	
	@ConstructorAnnotationDemo
	public Account() {
		
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", user=" + user + ", balance=" + balance + ", remark=" + remark + ", sevstate="
				+ sevstate + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getSevstate() {
		return sevstate;
	}
	public void setSevstate(int sevstate) {
		this.sevstate = sevstate;
	}
}
