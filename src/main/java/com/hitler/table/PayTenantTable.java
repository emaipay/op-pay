package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayTenant;

/**
 * 接入商户表
 * 
 * @author klp
 *
 * @param <DTO>
 */
public class PayTenantTable<DTO> extends GenericTable<DTO, PayTenant> {

 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8203463545800681833L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayTenant> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
