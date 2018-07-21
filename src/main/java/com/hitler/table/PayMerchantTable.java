package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayMerchant;

/**
 * 第三方商户表
 * @author klp
 *
 * @param <DTO>
 */
public class PayMerchantTable<DTO> extends GenericTable<DTO, PayMerchant> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4095638370651915484L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayMerchant> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
