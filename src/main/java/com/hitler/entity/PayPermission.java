package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.enums.PersistEnum;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_permission")
public class PayPermission extends CheckableEntity<Integer> {
	 
	private static final long serialVersionUID = 1727025558539025470L;
	/**
	 * 权限名称
	 */
	@Column(name = "PERMISSION_NAME", length = 30, nullable = false)
	private String permissionName;
	/**
	 * 权限类型(0-菜单,1-权限)
	 */
	@Column(name = "PERMISSION_TYPE", columnDefinition = "tinyint", nullable=false )
	@Type(type = "com.hitler.core.enums.MyEnumType")
	private PermissionType permissionType;



	/**
	 * 跳转路径
	 */
	@Column(name = "PATH", length = 50, nullable = false)
	private String path;
	/**
	 * 权限代码（shiro授权唯一认证） 系统名称+模块+操作  如 back:user:create
	 */
	@Column(name = "CODE", length = 50, unique = true, nullable = false)
	private String code;
	/**
	 * 父权限ID
	 */
	@Column(name = "PARENT_PERMISSION_ID", columnDefinition = "INT", nullable = true)
	private Integer parentPermissionId;
	
	/**
	 * 排序（深度）
	 */
	@Column(name = "DEEP", columnDefinition = "INT", nullable = true)
	private Integer deep;
	/**
	 * 是否显示(默认0-显示，1-隐藏)
	 */
	@Column(name = "IS_DISPLAY", columnDefinition = "tinyint default 0")
	@Type(type = "com.hitler.core.enums.MyEnumType")
	private IsDisplay isDisplay=IsDisplay.显示;
	/**
	 * 图标()
	 */
	@Column(name = "ICON", columnDefinition = "varchar(60)")
	private String icon;
	/**
	 * 排序（深度）
	 */
	@Column(name = "FLOOR", columnDefinition = "INT", nullable = true)
	private Integer floor;
	
	

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getParentPermissionId() {
		return parentPermissionId;
	}

	public void setParentPermissionId(Integer parentPermissionId) {
		this.parentPermissionId = parentPermissionId;
	}



	public Integer getDeep() {
		return deep;
	}

	public void setDeep(Integer deep) {
		this.deep = deep;
	}

	public IsDisplay getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(IsDisplay isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}

	/**
	 * 权限类型
	 */
	public enum PermissionType implements PersistEnum<PayPermission.PermissionType> {
		菜单("0"), 权限("1");
		private final String value;

		PermissionType(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	

	/**
	 * 是否显示
	 */
	public enum IsDisplay implements PersistEnum<PayPermission.IsDisplay> {
		显示("0"),
		隐藏("1");
		private final String value;

		IsDisplay(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}



}
