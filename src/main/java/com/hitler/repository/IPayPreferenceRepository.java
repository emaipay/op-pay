package com.hitler.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPreference;

public interface IPayPreferenceRepository extends GenericRepository<PayPreference, Integer> {
	/**
	 * 根据code查询常量
	 * @param code
	 * @return
	 */
	@Query("from PayPreference where code=:code ")
	public PayPreference queryPreference(@Param("code")String code);
}
