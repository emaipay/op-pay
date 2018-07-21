package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;

public interface IPayConfigRepository extends GenericRepository<PayConfig, Integer> {
	
	/**
	 * 查询支付配置
	 * @param type
	 * @param methodType
	 * @return
	 */
	@Query("from PayConfig where platformType=:platformType and methodType=:methodType")
	public PayConfig queryConfigByType(@Param("platformType")String type,@Param("methodType")MethodType methodType);


	   @Modifying
	    @Query("DELETE FROM PayConfig  WHERE id in :ids")
	    public void deleteByIdList(@Param("ids") List<Integer> ids);
	   
}
