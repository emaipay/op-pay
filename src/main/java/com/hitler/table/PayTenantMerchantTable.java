package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayTenantMerchant;

/**
 * 接入商户对应第三方支付商户表
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayTenantMerchantTable<DTO> extends GenericTable<DTO, PayTenantMerchant> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2676197499939483047L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayTenantMerchant> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
