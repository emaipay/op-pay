package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayUser;

/**
 * 用户管理
 * @author klp
 *
 * @param <DTO>
 */
public class PayUserTable<DTO> extends GenericTable<DTO, PayUser> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5185045154110125099L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
