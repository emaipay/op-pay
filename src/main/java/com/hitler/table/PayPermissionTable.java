package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayPermission;

/**
 * 接入商户表
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayPermissionTable<DTO> extends GenericTable<DTO, PayPermission> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5642889033191211201L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayPermission> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
