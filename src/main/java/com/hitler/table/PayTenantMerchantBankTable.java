package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayTenantMerchantBank;

/**
 * 接入商户对应商户的支付方式(银行卡)
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayTenantMerchantBankTable<DTO> extends GenericTable<DTO, PayTenantMerchantBank> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4458667900136384481L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayTenantMerchantBank> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
