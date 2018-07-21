package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayRole;

/**
 * 角色管理
 * @author klp
 *
 * @param <DTO>
 */
public class PayRoleTable<DTO> extends GenericTable<DTO, PayRole> {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8974833936800063595L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayRole> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
