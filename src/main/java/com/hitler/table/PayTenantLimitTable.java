package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayTenantLimit;

/**
 * 接入商户第三方支付限额表
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayTenantLimitTable<DTO> extends GenericTable<DTO, PayTenantLimit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8637850897536665374L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayTenantLimit> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
