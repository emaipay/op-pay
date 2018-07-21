package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayPlatformType;

/**
 * 支付类型大类表
 * @author klp
 *
 * @param <DTO>
 */
public class PayPlatformTypeTable<DTO> extends GenericTable<DTO, PayPlatformType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8104883218883512668L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayPlatformType> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
