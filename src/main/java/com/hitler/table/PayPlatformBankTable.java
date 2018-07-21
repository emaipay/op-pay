package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayPlatformBank;

/**
 * 第三方支付平台对应支付方式配置表
 * @author klp
 *
 * @param <DTO>
 */
public class PayPlatformBankTable<DTO> extends GenericTable<DTO, PayPlatformBank> {

  	/**
	 * 
	 */
	private static final long serialVersionUID = 3441611426729342253L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayPlatformBank> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
