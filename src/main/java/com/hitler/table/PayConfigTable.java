package com.hitler.table;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.hitler.core.dto.support.GenericTable;
import com.hitler.entity.PayConfig;

/**
 * 各支付平台配置表
 * @author klp
 *
 * @param <DTO>
 */
public class PayConfigTable<DTO> extends GenericTable<DTO, PayConfig> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1910305559884590176L;

	@Override
	public List<Selection<?>> buildExpression(Root<PayConfig> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
