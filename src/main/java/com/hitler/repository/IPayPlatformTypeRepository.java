package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPlatformType;

/**
 *支付类型JPA层
 * @author klp
 *
 */
public interface IPayPlatformTypeRepository extends GenericRepository<PayPlatformType, Integer> {

	   @Modifying
	    @Query("DELETE FROM PayPlatformType  WHERE id in :ids")
	    public void deleteByIdList(@Param("ids") List<Integer> ids);
}
