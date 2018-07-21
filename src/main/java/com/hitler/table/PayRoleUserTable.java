package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayRoleUser;

/**
 * 权限分配
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayRoleUserTable<DTO> extends GenericTable<DTO, PayRoleUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7617576639880539504L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayRoleUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
