package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayBank;

/**
 * 支付平台可支付方式(银行卡)
 * @author klp
 *
 * @param <DTO>
 */
public class PayBankTable<DTO> extends GenericTable<DTO, PayBank> {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -1386767726158993073L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayBank> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
