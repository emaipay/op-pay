package com.hitler.core.dto.login;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yang on 2017/3/20.
 * @version 1.0
 * @description 用户登录DTO
 */
public class AdminInfoDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2466550998849721484L;
	/**
     * 用户名
     */
    //@ApiDoc(comments = "用户名", isMust = true)
    @NotNull
    private String userName;
    /**
     * 密码
     */
    //@ApiDoc(comments = "密码", isMust = true)
    @NotNull
    private String password;
    /**
     * 验证码
     */
    //@ApiDoc(comments = "验证码", isMust = true)
    private String vCode;
    /**
     * 登录设备
     */
    //@ApiDoc(comments = "登录设备(3:其它;4:Android;5:iOS")
    private String device = "3";

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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
