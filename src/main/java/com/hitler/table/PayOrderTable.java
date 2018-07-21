package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayOrder;

public class PayOrderTable<DTO> extends GenericTable<DTO, PayOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1621118053861268868L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
