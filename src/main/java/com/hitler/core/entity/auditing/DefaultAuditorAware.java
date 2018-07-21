package com.hitler.core.entity.auditing;

import org.springframework.data.domain.AuditorAware;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author  jtwise
 * @date 2016年7月19日 上午9:43:52
 * @verion 1.0
 */
public class DefaultAuditorAware implements AuditorAware<String> {

	public String getCurrentAuditor() {
		String username = RpcContext.getContext().getAttachment("username");
		return null == username ? "SYS001" : username;
	}

}
