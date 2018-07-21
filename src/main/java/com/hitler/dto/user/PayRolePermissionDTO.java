package com.hitler.dto.user;

import com.hitler.core.dto.support.PersistentDTO;
import com.hitler.entity.PayPermission;
import com.hitler.entity.PayRolePermission;


/**
 * Created by yang on 2017/3/21.
 * @version 1.0
 * @description
 */
public class PayRolePermissionDTO extends PersistentDTO<Integer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5885890677113965827L;

	private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;
  
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 租户代号
     */
    private String tenantCode;
    
    private Integer floor;
    /**
     * 是否默认
     */
    private Boolean isDefault;
    /**
     * 权限id
     */
    private Integer permissionId;

    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限类型(0-菜单,1-权限)
     */
    private PayPermission.PermissionType permissionType;



    /**
     * 跳转路径
     */
    private String path;
    /**
     * 权限代码（shiro授权唯一认证） 系统名称+模块+操作  如 back:user:create
     */
    private String code;
    /**
     * 父权限ID
     */
    private Integer parentPermissionId;
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

    public PayRolePermissionDTO() {
    }
    public PayRolePermissionDTO(PayRolePermission rp) {
        this.id = rp.getId();
        this.roleId = rp.getRoleId().getId();
        this.roleName = rp.getRoleId().getRoleName();
        this.permissionId = rp.getPermissionId().getId();
        this.permissionName = rp.getPermissionId().getPermissionName();
        this.permissionType = rp.getPermissionId().getPermissionType();
        this.path = rp.getPermissionId().getPath();
        this.code = rp.getPermissionId().getCode();
        this.floor=rp.getPermissionId().getFloor();
        this.parentPermissionId = rp.getPermissionId().getParentPermissionId();
        this.deep = rp.getPermissionId().getDeep();
        this.isDisplay = rp.getPermissionId().getIsDisplay();
        this.icon = rp.getPermissionId().getIcon();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public PayPermission.PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PayPermission.PermissionType permissionType) {
        this.permissionType = permissionType;
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


    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
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

