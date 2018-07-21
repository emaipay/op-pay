package com.hitler.dto.user;

import java.io.Serializable;
import java.util.List;

public class PayPermissionTreeDTO implements Serializable {
	
	private static final long serialVersionUID = 130710525722243695L;
	
	private Integer id;
	private String text;
	private Integer deep;
	private List<PayPermissionTreeDTO> children = null;

	public PayPermissionTreeDTO(){

	}

	public PayPermissionTreeDTO(Integer id, String text, Integer deep) {
		this.id = id;
		this.text = text;
		this.deep = deep;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getDeep() {
		return deep;
	}
	public void setDeep(Integer deep) {
		this.deep = deep;
	}
	public List<PayPermissionTreeDTO> getChildren() {
		return children;
	}
	public void setChildren(List<PayPermissionTreeDTO> children) {
		this.children = children;
	}
	
}
