package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayPlatform;

/**
 * 第三方支付平台管理
 * @author  klp
 *
 * @param <DTO>
 */
public class PayPlatformTable<DTO> extends GenericTable<DTO, PayPlatform> {

	private static final long serialVersionUID = 303038776743904926L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayPlatform> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
