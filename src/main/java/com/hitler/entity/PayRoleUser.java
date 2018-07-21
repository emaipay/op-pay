package com.hitler.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.PersistableEntity;
import com.hitler.core.entity.annotion.Checked;

/**
 * 角色用户
 *
 * @author
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_role_user")
public class PayRoleUser extends PersistableEntity<Integer> {

    private static final long serialVersionUID = -6880600001541700855L;

    /**
     * 角色ID
     */
    @Checked
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "id", columnDefinition = "INT COMMENT '角色ID'", nullable = false)
    private PayRole roleId;

    /**
     * 用户ID
     */
    @Checked
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", columnDefinition = "INT COMMENT '用户ID'", nullable = false)
    private PayUser userId;

    public PayRole getRoleId() {
        return roleId;
    }

    public void setRoleId(PayRole roleId) {
        this.roleId = roleId;
    }

    public PayUser getUserId() {
        return userId;
    }

    public void setUserId(PayUser userId) {
        this.userId = userId;
    }

}
