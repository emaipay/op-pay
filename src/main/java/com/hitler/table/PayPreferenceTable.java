package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayPreference;

/**
 * 配置各个支付平台(常量)
 * @author klp
 *
 * @param <DTO>
 */
public class PayPreferenceTable<DTO> extends GenericTable<DTO, PayPreference> {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -7352456755888559053L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayPreference> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
