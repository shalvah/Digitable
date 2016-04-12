package com.shalvahadebayo.digitable;

/**
 * Created by ACER pc on 29/03/2016.
 */
public class Assignment {

	private int id;
	private String title;
	private String desc;

	public Assignment(int id, String title, String desc) {
		this.id = id;
		this.title = title;
		this.desc = desc;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	//for ArrayAdapter in ListView

	@Override
	public String toString() {
		return title;
	}
}
