package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayRolePermission;

/**
 * 权限资源管理
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayRolePermissionTable<DTO> extends GenericTable<DTO, PayRolePermission> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -930860804879987076L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayRolePermission> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
