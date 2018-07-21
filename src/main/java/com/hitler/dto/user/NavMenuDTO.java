package com.hitler.dto.user;

import java.io.Serializable;
import java.util.Collection;

/**
 * 导航栏菜单DTO
 * @author yang
 * 2015-10-10 上午9:44:27
 */
public class NavMenuDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;

	/**
	 * 图标
	 */
	public String icon;

	/**
	 * 菜单名称
	 */
	public String permissionName;

	/**
	 * 跳转地址
	 */
	public String path;

	/**
	 * 子节点
	 */
	public Collection<? extends Serializable> children;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Collection<? extends Serializable> getChildren() {
		return children;
	}
	
	public void setChildren(Collection<? extends Serializable> children) {
		this.children = children;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permissionName == null) ? 0 : permissionName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavMenuDTO other = (NavMenuDTO) obj;
		if (permissionName == null) {
			if (other.permissionName != null)
				return false;
		} else if (!permissionName.equals(other.permissionName))
			return false;
		return true;
	}
	
}
