package com.hitler.core.dto.support;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.hitler.core.utils.BeanMapper;

/**
 * @author Lane
 *
 * 通用表格抽象类
 */
public abstract class GenericTable<DTO, E> implements IGenericTable<DTO, E> {

	private static final long serialVersionUID = 6655607942888642862L;
	
	private List<DTO> rows;
//	private List<DTO> data;
	
//	private Long recordsTotal;
	private Long total;
	
	private Long recordsFiltered;

//	@Override
//	public List<DTO> getData() {
//		return data;
//	}
//
//	@Override
//	public void setData(List<DTO> data) {
//		this.data = data;
//	}
//
//	@Override
//	public Long getRecordsTotal() {
//		return recordsTotal;
//	}
//
//	@Override
//	public void setRecordsTotal(Long recordsTotal) {
//		this.recordsTotal = recordsTotal;
//	}
 
	@Override
	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public List<DTO> getRows() {
		return rows;
	}

	public void setRows(List<DTO> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Override
	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	
	@Override
	public Map<String, Object> map() throws IllegalAccessException, InvocationTargetException {
		return BeanMapper.objectToMap(this);
	}

	
}
