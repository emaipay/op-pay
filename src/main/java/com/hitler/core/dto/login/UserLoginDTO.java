package com.hitler.core.dto.login;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yang on 2017/3/20.
 * @version 1.0
 * @description 用户登录DTO
 */
public class UserLoginDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2594209253635313711L;
	/**
     * 用户名
     */
    //@ApiDoc(comments = "用户名", isMust = true)
    @NotBlank(message="用户名不能为空!")
    private String userName;
    /**
     * 密码
     */
    //@ApiDoc(comments = "密码", isMust = true)
    @NotBlank(message="密码不能为空")
    private String password;
    /**
     * 验证码
     */
    //@ApiDoc(comments = "验证码", isMust = true)
    private String vCode;
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    
}
