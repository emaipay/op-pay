package com.hitler.dto.user;

import com.hitler.core.dto.support.PersistentDTO;
import com.hitler.entity.PayPermission;

/**
 * Created by yang on 2017/3/21.
 * @version 1.0
 * @description
 */
public class PayPermissionDTO extends PersistentDTO<Integer> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4784083637432128120L;
	private Integer id;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限类型(0-菜单,1-权限)
     */
    private PayPermission.PermissionType permissionType;
    /**
     * 路径
     */
    private String path;
    /**
     * 权限代码
     */
    private String code;
    /**
     * 父权限ID
     */
    private Integer parentPermissionId;
    /**
     * 所在层级
     */
    private Integer floor;
    /**
     * 排序（深度）
     */
    private Integer deep;
    /**
     * 是否显示(默认0-显示，1-隐藏)
     */
    private PayPermission.IsDisplay isDisplay;
    /**
     * 图标()
     */
    private String icon;


    public PayPermissionDTO(Integer id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

   

    public PayPermission.PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PayPermission.PermissionType permissionType) {
		this.permissionType = permissionType;
	}

	public PayPermission.IsDisplay getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(PayPermission.IsDisplay isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

